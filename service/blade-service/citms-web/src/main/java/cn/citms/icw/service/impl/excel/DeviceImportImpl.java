package cn.citms.icw.service.impl.excel;

import cn.citms.icw.Utils.ExcelUtils;
import cn.citms.icw.entity.Building;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.CommunityDevice;
import cn.citms.icw.entity.Unit;
import cn.citms.icw.mapper.BuildingMapper;
import cn.citms.icw.mapper.CommunityDeviceMapper;
import cn.citms.icw.mapper.CommunityMapper;
import cn.citms.icw.mapper.UnitMapper;
import cn.citms.icw.service.ExcelService;
import cn.citms.icw.service.impl.BaseService;
import cn.citms.icw.vo.CommunityDeviceVO;
import cn.citms.icw.vo.DeviceRequestVO;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import cn.citms.icw.factory.ExcelImportFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备excel数据导入
 * @author cyh
 */
@Component
public class DeviceImportImpl extends ExcelService implements InitializingBean {

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private BuildingMapper buildingMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private CommunityDeviceMapper communityDeviceMapper;
    @Resource
    private BaseService baseService;
    @Value("${self.tagInfo:智慧社区}")
    private String tagInfo;

    private static final int ROW_INDEX = 5;
    private static final int READ_ROW_CELL_NUM = 3;

    public DeviceImportImpl(){
        super(ROW_INDEX, READ_ROW_CELL_NUM);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExcelImportFactory.initService("device", this);
    }

    @Override
    protected Map<String, Map<String, Object>> GetExtraInfo(List<List<Object>> list) {
        Map<String, Map<String, Object>> map = new HashMap<>(8);
        GetXqmcDict(map);
        getDevice(map);
        return map;
    }

    @Override
    protected String verify(List<Object> l, Map<String, Map<String, Object>> map) {
        StringJoiner sj = new StringJoiner(",");
        String r = ExcelUtils.SelectVerify(l.get(0), "社区名称", map.get("Community_Id"), false);
        if(StrUtil.isNotBlank(r)) {
            sj.add(r);
        }
        String r1 = ExcelUtils.lddymcVerify2(l.get(1));
        if(StrUtil.isNotBlank(r1)) {
            sj.add(r1);
        }
        String r1_1 = exsitVerify(l.get(0), l.get(1), map);
        if(StrUtil.isNotBlank(r1_1)) {
            sj.add(r1_1);
        }
        String r2 = ExcelUtils.XqmcVerify(l.get(2), "", map.get("deviceCnt"), "设备编码");
        if(StrUtil.isNotBlank(r2)) {
            sj.add(r2);
        } else {
            String r3 = exsitDevice(l.get(2), map);
            sj.add(r3);
        }
        return sj.toString();
    }

    @Override
    protected String writeErrorMsg(MultipartFile fileDoc, List<String> errorMsg, String serverAddress) throws IOException {
        return ExcelUtils.writeErrorMsg(fileDoc, errorMsg, serverAddress, ROW_INDEX, READ_ROW_CELL_NUM);
    }

    @Override
    protected int saveData(List<List<Object>> list, Map<String, Map<String, Object>> map){
        Map<String, Object> map1 = map.get("Community_Id");
        Map<String, Object> map2 = map.get("building");
        Map<String, Object> map3 = map.get("unit");
        List<CommunityDevice> list1 = new ArrayList<>(list.size());
        for (int i = ROW_INDEX; i < list.size(); i++) {
            List<Object> res = list.get(i);
            CommunityDevice c = new CommunityDevice();
            c.setCommunityId((String) map1.get(res.get(0)));
            String[] cellVal = ((String) res.get(1)).split("-");
            c.setBuildId((String) map2.get(cellVal[0]));
            c.setUnitId((String) map3.get(cellVal[1]));
            c.setDeviceId((String) res.get(2));
            list1.add(c);
        }
        if(CollUtil.isNotEmpty(list1)) {
            communityDeviceMapper.batchInsert(list1);
        }
        return list1.size();
    }

    private void GetXqmcDict(Map<String, Map<String, Object>> map){
        List<String> departmentIds = baseService.getPermissionDepartment();
        Map<String, Object> map1 = new HashMap<>(16);
        Map<String, Object> map2 = new HashMap<>(16);
        Map<String, Object> map3 = new HashMap<>(16);
        Map<String, Object> map4 = new HashMap<>(16);
        Map<String, Object> map5 = new HashMap<>(16);
        if(CollUtil.isNotEmpty(departmentIds)) {
            List<Community> communityList = communityMapper.selectCommunityInfoByDeptIds(departmentIds);
            if(CollUtil.isNotEmpty(communityList)) {
                List<String> communityIds = new ArrayList<>(communityList.size());
                for (Community community : communityList) {
                    map1.put(community.getXqmc(), community.getId());
                    communityIds.add(community.getId());
                }
                List<Building> buildingList = buildingMapper.selectByCommunityIds(communityIds);
                if(CollUtil.isNotEmpty(buildingList)) {
                    for (Building building : buildingList) {
                        map2.put(building.getCommunity_Id() +"-"+building.getLdh_Mc(), building.getId());
                    }
                }
                List<Unit> unitList = unitMapper.selectByCommunityIds(communityIds);
                if(CollUtil.isNotEmpty(unitList)) {
                    for (Unit unit : unitList) {
                        map3.put(unit.getCommunity_Id()+"-"+unit.getBuilding_Id()+"-"+unit.getDy_Mc(), unit.getId());
                    }
                }
                List<CommunityDevice> deviceList = communityDeviceMapper.getByCommunityIdList(communityIds);
                if(CollUtil.isNotEmpty(deviceList)) {
                    for (CommunityDevice device : deviceList) {
                        map4.put(device.getDeviceId(), device.getDeviceId());

                        Integer i = (Integer) map5.get(device.getDeviceId());
                        if(i == null) {
                            map5.put(device.getDeviceId(), 1);
                        } else {
                            map5.put(device.getDeviceId(), i + 1);
                        }
                    }
                }
            }
        }
        map.put("Community_Id", map1);
        map.put("building", map2);
        map.put("unit", map3);
        map.put("device", map4);
        map.put("deviceCnt", map5);
    }

    /**
     * 查询设备信息
     * @param map
     */
    private void getDevice(Map<String, Map<String, Object>> map){
        DeviceRequestVO vo = new DeviceRequestVO();
        vo.setTagInfo(tagInfo);
        vo.setPageIndex(0);
        vo.setPageSize(1000);
        List<CommunityDeviceVO> result = baseService.getDevice(vo);
        Map<String, Object> map1 = result.stream().collect(Collectors.toMap(CommunityDeviceVO::getDeviceId, CommunityDeviceVO::getDeviceId));
        map.put("deviceInfo", map1);
    }

    /**
     * 校验门户名称是否已存在于社区
     * @param community -社区， e- 楼栋单元门户名称
     * @return
     */
    private String exsitVerify(Object community, Object e, Map<String, Map<String, Object>> map){
        String value = (String) e;
        if(StrUtil.isNotBlank(value)) {
            String[] cellValue = value.split("-");
            String ldmc = "", dymc = "";
            if(cellValue.length == 2) {
                String communityId = (String) map.get("Community_Id").get(community);
                if(StrUtil.isBlank(communityId)) {
                    return community + "不存在";
                }
                String buildId = "";
                if(map.get("building").containsKey(communityId+"-"+cellValue[0])){
                    ldmc = cellValue[0];
                    buildId = (String) map.get("building").get(communityId+"-"+cellValue[0]);
                }
                if (StrUtil.isBlank(ldmc)) {
                    return community + "-" + cellValue[0] + "不存在";
                }
                if(map.get("unit").containsKey(communityId+"-"+buildId+"-"+cellValue[1])){
                    dymc = cellValue[1];
                }
                if (StrUtil.isBlank(dymc)) {
                    return community + "-" + ldmc + "-" + cellValue[1] + "不存在";
                }
            }
        }
        return "";
    }

    /**
     * 验证设备存在
     * @param e
     * @param map
     * @return
     */
    private String exsitDevice(Object e, Map<String, Map<String, Object>> map){
        String result = "";
        String deviceNo = (String) e;
        if(StrUtil.isBlank(deviceNo)) {
            result = "设备编号不能为空";
        } else {
            Map<String, Object> map1 = map.get("deviceInfo");
            if(!map1.containsKey(deviceNo)) {
                result = "设备编码不存在";
            }
        }
        return result;
    }
}
