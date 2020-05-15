package cn.citms.icw.Utils;

import cn.citms.icw.dto.AttachmentsDto;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springblade.common.constant.CitmsAppConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@ApiModel(description = "字典工具类")
public class CommonUtils {

    private static ReentrantLock lock = new ReentrantLock();

    /**
     * 通过 字典类型 kind和 字典编号 获取字典值
     * @param url 查询地址
     * @param kind 字典分类
     * @param dictNo 字典具体编号
     * @return
     */
    public static String getDictByKindAndDictNo(String url, Integer kind, String dictNo) {
        if(StringUtils.isBlank(dictNo)){
            return "";
        }
        String value = TimeCacheUtils.DICT_VALUE_CACHE.get(kind+":"+dictNo);
        if(StrUtil.isBlank(value)) {
            lock.lock();
            try {
                value = TimeCacheUtils.DICT_VALUE_CACHE.get(kind+":"+dictNo);
                if(StrUtil.isBlank(value)) {
                    url += "?kind=" + kind;
                    String token = CitmsAppConstant.AUTHORIZATION;
                    String json = sendGet(url, token);
                    JSONObject jsonObject = JSON.parseObject(json);
                    JSONArray results = jsonObject.getJSONArray("result");
                    for (Object obj : results) {
                        JSONObject resObject = (JSONObject) obj;
                        String key = resObject.getString("dictionaryNo");
                        String val = resObject.getString("dictionaryValue");
                        TimeCacheUtils.DICT_VALUE_CACHE.put(kind+":"+key, val);
                        if(dictNo.equals(key)){
                            value = val;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        return value;
    }

    /**
     * 通过数据源id获取附件的信息
     * @param url
     * @param sourceId
     * @return
     */
    public static List<AttachmentsDto> getAttachmentBySourceId(String url, String sourceId){
        List<AttachmentsDto> dtoList = new ArrayList<>();
        if(StringUtils.isBlank(sourceId)){
            return dtoList;
        }
        url += "?sourceId=" + sourceId;
        String token = CitmsAppConstant.AUTHORIZATION;
        String json = sendGet(url, token);
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray results = jsonObject.getJSONArray("result");
        for (Object obj : results) {
            JSONObject resObject = (JSONObject) obj;
            AttachmentsDto dto = new AttachmentsDto();
            dto.setAttachmentId(resObject.getString("attachmentId"));
            dto.setExtenson(resObject.getString("extenson"));
            dto.setName( resObject.getString("name"));
            dto.setUrl(resObject.getString("url"));
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 获取部门及子部门
     * @param url
     * @param departmentId
     * @return
     */
    public static List<String> getChildDepartmentIds(String url, String departmentId){
        url += "?departmentId="+departmentId;
        String json = sendGet(url, CitmsAppConstant.AUTHORIZATION);
        List<String> list = CollUtil.newArrayList();
        if(StrUtil.isNotBlank(json)){
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray results = jsonObject.getJSONArray("result");
            for (Object result : results) {
                JSONObject resObject = (JSONObject) result;
                list.add(resObject.getString("departmentId"));
            }
        }
        return list;
    }
    public static Map<String, Object> getChildDepartmentMap(String url, String departmentId){
        url += "?departmentId="+departmentId;
        String json = sendGet(url, CitmsAppConstant.AUTHORIZATION);
        Map<String, Object> map = CollUtil.newHashMap();
        if(StrUtil.isNotBlank(json)){
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray results = jsonObject.getJSONArray("result");
            for (Object result : results) {
                JSONObject resObject = (JSONObject) result;
                map.put(resObject.getString("buName"), resObject.get("departmentId"));
            }
        }
        return map;
    }

    /**
     * 根据kind获取字典map
     * @param url
     * @param kind
     * @return
     */
    public static Map<String, String> getDictByKind(String url, String kind) {
        return getDictJson(url, kind, 0);
    }

    /**
     * 根据kind获取字典map 类型value-no
     * @param url
     * @param kind
     * @return
     */
    public static Map<String, String> getDictValueMapByKind(String url, String kind) {
        return getDictJson(url, kind, 1);
    }

    /**
     * type=1返回value-key类型；type!=1返回key-value类型
     * @param url
     * @param kind
     * @param type
     * @return
     */
    private static Map<String, String> getDictJson(String url, String kind, final int type){
        url += "?kind=" + kind;
        String token = CitmsAppConstant.AUTHORIZATION;
        String json = sendGet(url, token);
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray results = jsonObject.getJSONArray("result");
        Map<String, String> dicts = new HashMap<>(16);
        if(results != null && results.size() > 0) {
            results.forEach(obj -> {
                JSONObject resObject = (JSONObject) obj;
                String key = resObject.getString("dictionaryNo");
                String value = resObject.getString("dictionaryValue");
                if(type == 1) {
                    dicts.put(value, key);
                } else {
                    dicts.put(key, value);
                }
            });
        }
        return dicts;
    }

    //发送get请求
    public static String sendGet(String url, String token) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (StringUtils.isNotBlank(token)) {
                connection.setRequestProperty("Authorization", token);
            }
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error("发送GET请求出现异常！", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                log.error("", e2);
            }
        }
        return result;
    }

    public static String sendPost(String url, String jsonStr) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
        httpPost.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        httpPost.setHeader("Authorization", CitmsAppConstant.AUTHORIZATION);
        CloseableHttpResponse httpResponse = null;
        try {
            httpPost.setEntity(new StringEntity(jsonStr, "UTF-8"));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    public  static String createUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("vbd-pool-%d")
            .build();
    public static final ExecutorService VBD_POOL = new ThreadPoolExecutor(CitmsAppConstant.THREAD_CORE_POOL_SIZE,
            CitmsAppConstant.THREAD_MAX_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024),
            NAMED_THREAD_FACTORY, (r, executor) -> log.error("线程错误，需要处理:Runnable{},ThreadPoolExecutor{}", r, executor));


}
