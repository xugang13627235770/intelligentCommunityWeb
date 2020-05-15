package org.springblade.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.util.ObjectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springblade.common.constant.CitmsAppConstant;
import org.springblade.common.enums.DictEnum;
import org.springblade.common.vo.DeviceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 服务配置信息工具类
 */
@Data
@Component
@Slf4j
public class CommonComponent implements ApplicationListener<WebServerInitializedEvent> {

    /**
     * 服务端口号
     */
    private int serverPort;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取本服务完整服务地址
     *
     * @return
     */
    public String getServerAddress() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://" + address.getHostAddress() + ":" + this.serverPort;
    }

    /**
     * 获取本服务Host
     *
     * @return
     */
    public String getServerHost() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address.getHostAddress();
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        serverPort = event.getWebServer().getPort();
    }

    /**
     * 通过key取对应的redis数据
     *
     * @param key
     * @param toke
     * @return
     */
    public String getRedisData(String key, String toke) {
        String str = redisTemplate.opsForValue().get(key + toke);
        if (!"".equals(str) && str != null) {
            return str;
        }
        return "";
    }

    /**
     * 获取默认token
     */
    public String getRedisDefaultToken() {
        Set<String> keySet = redisTemplate.keys(CitmsAppConstant.DEFAULTTOKEN + "*");
        if (CollectionUtils.isNotEmpty(keySet)) {
            for (String key : keySet) {
                return key.replaceAll(CitmsAppConstant.DEFAULTTOKEN, "");
            }
        }
        return "";
    }


    /**
     * 通过key获取字典项（车）value数据
     *
     * @return
     */
    public String getDictValue(JSONObject respJsonObj, String kind, String key) {
        String dictionaryValue = "";
        if (ObjectUtil.isNotNull(respJsonObj)) {
            JSONObject resultJson = respJsonObj.getJSONObject("result");
            if (ObjectUtil.isNotNull(resultJson)) {
                JSONArray jsonArray = resultJson.getJSONArray(kind);
                for (int k = 0; k < jsonArray.size(); k++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(k);
                    if (ObjectUtil.isNotNull(jsonObject)) {
                        if (key.equals(jsonObject.getString("dictionaryNo"))) {
                            dictionaryValue = jsonObject.getString("dictionaryValue");
                        }
                    }
                }
            }
        }
        return dictionaryValue;
    }

    /**
     * 通过value获取字典项（车）key数据
     *
     * @return
     */
    public String getDictKey(JSONObject respJsonObj, String kind, String value) {
        String dictionaryNo = "";
        if (ObjectUtil.isNotNull(respJsonObj)) {
            JSONObject resultJson = respJsonObj.getJSONObject("result");
            if (ObjectUtil.isNotNull(resultJson)) {
                JSONArray jsonArray = resultJson.getJSONArray(kind);
                for (int k = 0; k < jsonArray.size(); k++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(k);
                    if (ObjectUtil.isNotNull(jsonObject)) {
                        if (value.equals(jsonObject.getString("dictionaryValue"))) {
                            dictionaryNo = jsonObject.getString("dictionaryNo");
                        }
                    }
                }
            }
        }
        return dictionaryNo;
    }

    /**
     * 获取数据字典车辆相关基础信息
     *
     * @return
     */
    public JSONObject getDictRequestData() {
        String url = this.getServerAddress() + "/smw/Dict/GetSomeDicts";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + this.getRedisDefaultToken());
        Integer[] data = new Integer[DictEnum.values().length];
        int i = 0;
        JSONObject respJsonObj = null;
        if (data.length > 0) {
            for (DictEnum dictEnum : DictEnum.values()) {
                i++;
                String s = dictEnum.getIndex();
                data[i - 1] = Integer.valueOf(s);
            }
            // 将请求头和请求参数设置到HttpEntity中
            HttpEntity httpEntity = new HttpEntity(data, headers);
            // 发送请求
            respJsonObj = restTemplate.postForObject(url, httpEntity, JSONObject.class);
            //log.info("查询字典项中结果：" + respJsonObj.toJSONString());

        }
        return respJsonObj;
    }

    /**
     * 获取相关编号设备信息
     */
    public Map<String, DeviceModel> getSomeDeviceByNo(List<String> deviceNos) {
        String url = this.getServerAddress() + "/bdm/Device/GetSomeDeviceByNo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + this.getRedisDefaultToken());
        // 将请求头和请求参数设置到HttpEntity中
        HttpEntity httpEntity = new HttpEntity(deviceNos, headers);
        // 发送请求
        JSONObject respJsonObj = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        Map<String, DeviceModel> map = new HashMap<>(5);
        DeviceModel deviceModel = null;
        if (ObjectUtil.isNotNull(respJsonObj)) {
            JSONArray resultJson = respJsonObj.getJSONArray("result");
            if (ObjectUtil.isNotNull(resultJson)) {
                for (int i = 0; i < resultJson.size(); i++) {
                    JSONObject jsonObject = resultJson.getJSONObject(i);
                    String deviceNo = jsonObject.getString("deviceNo");
                    String deviceName = jsonObject.getString("deviceName");
                    String longitude = jsonObject.getString("longitude");
                    String latitude = jsonObject.getString("latitude");

                    deviceModel = new DeviceModel();
                    deviceModel.setDeviceName(deviceName);
                    deviceModel.setDeviceNo(deviceNo);
                    BigDecimal b1 = new BigDecimal(latitude);
                    BigDecimal b2 = new BigDecimal(longitude);
                    deviceModel.setLatitude(b1);
                    deviceModel.setLongitude(b2);
                    map.put(deviceNo, deviceModel);
                }
            }
        }
        return map;
    }

    /**
     * 获取中台字典数据
     */
    public JSONObject getZtDictRequestData(String cateCode) {
        String url = this.getServerAddress() + "/citms-dataaccess-platform/key/selectDict";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + this.getRedisDefaultToken());
        Map<String, Object> param = new HashMap<>();
        param.put("cateCode", cateCode);
        ResponseEntity<String> response = restTemplate.exchange(
                url + "?cateCode={cateCode}",
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                String.class,
                param);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        return jsonObject;
    }

    /**
     * 获取中台字典转换后的数据
     */
    public String getConvertDictData(JSONObject jsonObject, String fieldCode, String dictValue) {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int g = 0; g < jsonArray.size(); g++) {
                JSONObject JSONObject = jsonArray.getJSONObject(g);
                JSONArray jsonArray3 = JSONObject.getJSONArray("fields");
                for (int j = 0; j < jsonArray3.size(); j++) {
                    JSONObject jsonObject3 = jsonArray3.getJSONObject(j);
                    String fieldCodeValue = jsonObject3.getString("fieldCode");
                    if (fieldCode.equals(fieldCodeValue)) {
                        JSONArray dictArray = jsonObject3.getJSONArray("dict");
                        if (ObjectUtil.isNotNull(dictArray)) {
                            for (int k = 0; k < dictArray.size(); k++) {
                                JSONObject jsonObject4 = dictArray.getJSONObject(k);
                                String dictKey = jsonObject4.getString("dictKey");
                                String value = jsonObject4.getString("dictValue");
                                if (dictValue.equals(value)) {
                                    return dictKey;
                                }
                            }
                        }
                    }
                }
            }
        return "";
    }


}
