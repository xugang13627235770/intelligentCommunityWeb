package cn.citms.icw.service.impl;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.vo.AttachmentVO;
import cn.citms.icw.vo.CommunityDeviceVO;
import cn.citms.icw.vo.DeaviceCheckLogVO;
import cn.citms.icw.vo.DeviceRequestVO;
import cn.citms.mbd.basicdatacache.service.impl.ComponentUtil;
import com.alibaba.fastjson.JSONArray;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.constant.CitmsAppConstant;
import org.springblade.common.utils.UserContextHolder;
import org.springblade.common.vo.UserRightViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author cyh
 */
@Slf4j
@Component
public class BaseService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dgaRpc.attachmentUpdateOrRemoveUrl}")
    private String attachmentUpdateOrRemoveUrl;

    /**
     * /Common/Attachment/FindBySourceId
     * 此接口返回数据为：sourceId-listValue形式
     */
    @Value("${dgaRpc.getAttachmentBySourceIdUrl}")
    private String getAttachmentBySourceIdsUrl;

    @Value("${dgaRpc.getChildDepartmentIds}")
    private String getChildDepartmentIdsUrl;

    @Value("${dgaRpc.getDeviceUrl}")
    private String getDeviceUrl;

    @Value("${dgaRpc.getSomeDeviceLogUrl}")
    private String getSomeDeviceLogUrl;

    @Value("${dgaRpc.getSomeDictsUrl}")
    private String findAllDictApiUrl;
    @Autowired
    private ComponentUtil componentUtil;

    /**
     * 更新或移除数据源对应附件信息
     * @param sourceId
     * @param attachmentIds
     * @return
     */
    public boolean attachmentUpdateOrRemove(String sourceId, List<String> attachmentIds){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CitmsAppConstant.AUTHORIZATION);
        HttpEntity<List<String>> entity = new HttpEntity<>(attachmentIds, headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(attachmentUpdateOrRemoveUrl + "?sourceId=" + sourceId,
                HttpMethod.POST, entity, Object.class);
        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        Integer code = jsonObject.getInt("code");
        return code == 0;
    }

    /**
     * 获取所有的附件集
     */
    public Map<String, List<AttachmentVO>> getAttachmentBySourceIds(List<String> sourceIds){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CitmsAppConstant.AUTHORIZATION);
        HttpEntity<List<String>> entity = new HttpEntity<>(sourceIds, headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(getAttachmentBySourceIdsUrl,
                HttpMethod.POST, entity, Object.class);
        LinkedHashMap jsonObject = (LinkedHashMap) responseEntity.getBody();
        Integer code = (Integer) jsonObject.get("code");
        Map<String, List<AttachmentVO>> map = CollUtil.newHashMap();
        if(code == 0) {
            LinkedHashMap sourceMap = (LinkedHashMap) jsonObject.get("result");
            if(CollUtil.isNotEmpty(sourceMap)) {
                for (String sourceId : sourceIds) {
                    ArrayList list = (ArrayList) sourceMap.get(sourceId);
                    if(CollUtil.isNotEmpty(list)) {
                        List<AttachmentVO> list1 = new ArrayList<>(list.size());
                        for (Object o : list) {
                            LinkedHashMap lMap = (LinkedHashMap) o;
                            AttachmentVO vo = new AttachmentVO();
                            vo.setName(String.valueOf(lMap.get("name")));
                            vo.setAttachmentId(String.valueOf(lMap.get("attachmentId")));
                            vo.setUrl(String.valueOf(lMap.get("url")));
                            vo.setExtenson(String.valueOf(lMap.get("extenson")));
                            list1.add(vo);
                        }
                        map.put(sourceId, list1);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 通过community_id获取部门下的集合
     */
    public List<String> getAllIdsByCommunityId(String communityId){
        List<String> departmentIds = CollUtil.newArrayList();
        if(StrUtil.isNotBlank(communityId)) {
            UserRightViewModel userInfo = UserContextHolder.getUserInfo();
            if(userInfo == null) {
                return departmentIds;
            }
            String departmentId = userInfo.getDepartmentId();
            departmentIds = CommonUtils.getChildDepartmentIds(getChildDepartmentIdsUrl, departmentId);
        }
        return departmentIds;
    }

    /**
     * 获取当前用户能看到的所有部门
     */
    public List<String> getPermissionDepartment(){
        List<String> departmentIds = CollUtil.newArrayList();
        UserRightViewModel userInfo = UserContextHolder.getUserInfo();
        if(userInfo == null || StringUtils.isBlank(userInfo.getDepartmentId())) {
            return departmentIds;
        }
        departmentIds = CommonUtils.getChildDepartmentIds(getChildDepartmentIdsUrl, userInfo.getDepartmentId());
        return departmentIds;
    }
    public Map<String, Object> getPermissionDepartmentMap(){
        UserRightViewModel userInfo = UserContextHolder.getUserInfo();
        if(userInfo == null || StringUtils.isBlank(userInfo.getDepartmentId())) {
            return new HashMap<>(0);
        }
        return CommonUtils.getChildDepartmentMap(getChildDepartmentIdsUrl, userInfo.getDepartmentId());
    }

    /**
     * 查询设备信息
     * @param vo
     * @return
     */
    public List<CommunityDeviceVO> getDevice(DeviceRequestVO vo){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CitmsAppConstant.AUTHORIZATION);
        HttpEntity<JSONObject> entity = new HttpEntity<>(new JSONObject(vo), headers);
        Object response = restTemplate.postForObject(getDeviceUrl, entity, Object.class);
        Map<String, Object> objectMap = (LinkedHashMap<String, Object>) response;
        int code = Integer.parseInt(String.valueOf(Objects.requireNonNull(objectMap).get("code")));
        if(code == 0) {
            List<Object> arr = (ArrayList<Object>) objectMap.get("result");
            if(arr != null) {
                List<CommunityDeviceVO> list = new ArrayList<>(arr.size());
                for (Object o : arr) {
                    list.add(new JSONObject(o).toBean(CommunityDeviceVO.class, true));
                }
                return list;
            }
        }
        return new ArrayList<>(0);
    }

    // 获取设备的检测信息
    public List<DeaviceCheckLogVO> getSomeDeviceLog(String getSomeDeviceLogUrl, List<String> deviceList) {
        ArrayList<DeaviceCheckLogVO> deaviceCheckLogVOS = new ArrayList<>();
        if(CollectionUtils.isEmpty(deviceList)){
            return deaviceCheckLogVOS;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", CitmsAppConstant.AUTHORIZATION);
            HttpEntity<String> entity = new HttpEntity(deviceList, headers);
            com.alibaba.fastjson.JSONObject resp = restTemplate.postForObject(getSomeDeviceLogUrl, entity, com.alibaba.fastjson.JSONObject.class);
            log.info("获取设备检测信息响应结果：" + resp.toString());
            if (resp != null && resp.getIntValue("code") != 0) {
                return deaviceCheckLogVOS;
            } else {
                JSONArray jsonArray = resp.getJSONArray("result");
                for (Object o : jsonArray) {
                    com.alibaba.fastjson.JSONObject  jsonObject =  (com.alibaba.fastjson.JSONObject)o;
                    DeaviceCheckLogVO checkLogVO = jsonObject.toJavaObject(DeaviceCheckLogVO.class);
                    deaviceCheckLogVOS.add(checkLogVO);
                }
            }
        } catch (Exception e) {
            log.error("获取设备检测信息失败" + e.getMessage());
            e.printStackTrace();
        }
        return deaviceCheckLogVOS;
    }

    /**
     * 查询字典
     * @param kinds
     * @return
     */
    private com.alibaba.fastjson.JSONObject findAllDictionary(List<Integer> kinds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", componentUtil.getRedisDefaultToken());
        HttpEntity<String> entity = new HttpEntity(kinds, headers);
        com.alibaba.fastjson.JSONObject resp = restTemplate.postForObject(findAllDictApiUrl, entity, com.alibaba.fastjson.JSONObject.class);
        if (resp != null && resp.getIntValue("code") != 0) {
            return null;
        } else {
            com.alibaba.fastjson.JSONObject resultArray = resp.getJSONObject("result");
            return resultArray;
        }
    }
    public Map<Integer, Map<String, String>> findDict(List<Integer> kinds){
        com.alibaba.fastjson.JSONObject json = findAllDictionary(kinds);
        if(json == null) {
            return null;
        }
        Map<Integer, Map<String, String>> list = new HashMap<>((int)(kinds.size() / 0.75 + 1));
        for (Integer kind : kinds) {
            com.alibaba.fastjson.JSONArray arr = json.getJSONArray(String.valueOf(kind));
            if(arr == null || arr.size() == 0) {
                continue;
            }
            Map<String, String> map = new HashMap<>((int)(arr.size() / 0.75 + 1));
            for (int i = 0; i < arr.size(); i++) {
                com.alibaba.fastjson.JSONObject obj = arr.getJSONObject(i);
                map.put(obj.getString("dictionaryNo"), obj.getString("dictionaryValue"));
            }
            list.put(kind, map);
        }
        return list;
    }
    public Map<Integer, Map<String, Object>> findDictValue(List<Integer> kinds){
        com.alibaba.fastjson.JSONObject json = findAllDictionary(kinds);
        if(json == null) {
            return null;
        }
        Map<Integer, Map<String, Object>> list = new HashMap<>((int)(kinds.size() / 0.75 + 1));
        for (Integer kind : kinds) {
            com.alibaba.fastjson.JSONArray arr = json.getJSONArray(String.valueOf(kind));
            if(arr == null || arr.size() == 0) {
                continue;
            }
            Map<String, Object> map = new HashMap<>((int)(arr.size() / 0.75 + 1));
            for (int i = 0; i < arr.size(); i++) {
                com.alibaba.fastjson.JSONObject obj = arr.getJSONObject(i);
                map.put(obj.getString("dictionaryValue"), obj.get("dictionaryNo"));
            }
            list.put(kind, map);
        }
        return list;
    }

}
