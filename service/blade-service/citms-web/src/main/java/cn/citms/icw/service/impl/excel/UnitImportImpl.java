package cn.citms.icw.service.impl.excel;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.Utils.ExcelUtils;
import cn.citms.icw.entity.Building;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.Unit;
import cn.citms.icw.mapper.BuildingMapper;
import cn.citms.icw.mapper.CommunityMapper;
import cn.citms.icw.mapper.UnitMapper;
import cn.citms.icw.service.ExcelService;
import cn.citms.icw.service.impl.BaseService;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import cn.citms.icw.factory.ExcelImportFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 单元excel数据导入
 * @author cyh
 */
@Component
public class UnitImportImpl extends ExcelService implements InitializingBean {

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private BuildingMapper buildingMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private BaseService baseService;

    private static final int ROW_INDEX = 5;
    private static final int READ_ROW_CELL_NUM = 5;

    public UnitImportImpl(){
        super(ROW_INDEX, READ_ROW_CELL_NUM);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExcelImportFactory.initService("unit", this);
    }

    @Override
    protected Map<String, Map<String, Object>> GetExtraInfo(List<List<Object>> list) {
        Map<String, Map<String, Object>> map = new HashMap<>(8);
        GetDict(map);
        GetdymhmcDict(map);
        return map;
    }

    @Override
    protected String verify(List<Object> l, Map<String, Map<String, Object>> map) {
        StringJoiner sj = new StringJoiner(",");
        String r = ExcelUtils.SelectVerify(l.get(0), "社区名称", map.get("Community_Id"), false);
        if(StrUtil.isNotBlank(r)) {
            sj.add(r);
        }
        String r1 = ExsitVerify(l.get(1), map.get("Community_Id").get(l.get(0))+"-", map);
        if(StrUtil.isNotBlank(r1)) {
            sj.add(r1);
        }
        String r2 = ExsitVerify(l.get(0), l.get(1), l.get(2), map);
        if(StrUtil.isNotBlank(r2)) {
            sj.add(r2);
        }
        String r3 = ExcelUtils.lenVerify(l.get(3), "单元标识", 40);
        if(StrUtil.isNotBlank(r3)) {
            sj.add(r3);
        }
        String r4 = ExcelUtils.NumberVerify(l.get(4), "每层户数");
        if(StrUtil.isNotBlank(r4)) {
            sj.add(r4);
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
        Map<String, Object> map2 = map.get("Building_Id");
        List<Unit> list1 = new ArrayList<>(list.size());
        for (int i = ROW_INDEX; i < list.size(); i++) {
            List<Object> res = list.get(i);
            Unit c = new Unit();
            c.setId(CommonUtils.createUUID());
            String communityId = (String) map1.get(res.get(0));
            c.setCommunity_Id(communityId);
            c.setBuilding_Id((String) map2.get(communityId + "-"+res.get(1)));
            c.setDy_Mc((String) res.get(2));
            c.setDy_Bs((String) res.get(3));
            c.setMclh_Sl((String) res.get(4));
            list1.add(c);
        }
        if(CollUtil.isNotEmpty(list1)) {
            unitMapper.batchInsert(list1);
        }
        return list1.size();
    }

    /**
     * 获取社区名称
     * @return
     */
    private void GetDict(Map<String, Map<String, Object>> map){
        Map<String, Object> map1 = new HashMap<>(16);
        Map<String, Object> map2 = new HashMap<>(16);
        Map<String, Object> map3 = new HashMap<>(16);
        Map<String, Object> map4 = new HashMap<>(16);
        List<String> departmentIds = baseService.getPermissionDepartment();
        if(CollUtil.isNotEmpty(departmentIds)) {
            List<Community> communityList = communityMapper.selectCommunityInfoByDeptIds(departmentIds);
            if(CollUtil.isNotEmpty(communityList)) {
                List<String> ids = new ArrayList<>(communityList.size());
                for (Community community : communityList) {
                    map1.put(community.getXqmc(), community.getId());

                    ids.add(community.getId());
                }
                List<Building> buildingList = buildingMapper.selectByCommunityIds(ids);
                if(CollUtil.isNotEmpty(buildingList)) {
                    for (Building building : buildingList) {
                        map2.put(building.getCommunity_Id() + "-"+building.getLdh_Mc(), building.getId());

                        List<Map<String, String>> list = (List<Map<String, String>>) map3.get(building.getCommunity_Id());
                        Map<String, String> m = new HashMap<>(2);
                        m.put(building.getId(), building.getLdh_Mc());
                        if(CollUtil.isNotEmpty(list)) {
                            list.add(m);
                        } else {
                            list = new ArrayList<>(10);
                            list.add(m);
                            map3.put(building.getCommunity_Id(), list);
                        }
                    }
                }

                List<Unit> unitList = unitMapper.selectByCommunityIds(ids);
                if(CollUtil.isNotEmpty(unitList)) {
                    for (Unit unit : unitList) {
                        List<String> bhList = (List<String>) map4.get(unit.getBuilding_Id());
                        if(CollUtil.isNotEmpty(bhList)) {
                            bhList.add(unit.getDy_Mc());
                        } else {
                            bhList = new ArrayList<>(10);
                            bhList.add(unit.getDy_Mc());
                            map4.put(unit.getCommunity_Id()+"-"+unit.getBuilding_Id(), bhList);
                        }
                    }
                }
            }
        }
        map.put("Community_Id", map1);
        map.put("Building_Id", map2);
        map.put("building", map3);
        map.put("unit", map4);
    }

    private void GetdymhmcDict(Map<String, Map<String, Object>> map){
        Map<String, Object> map1 = map.get("Community_Id");
        Map<String, Object> map2 = map.get("building");
        Map<String, Object> map3 = map.get("unit");
        if(CollUtil.isEmpty(map1)) {
            return;
        }
        Map<String, Object> dict = new HashMap<>(16);
        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            List<Map<String, String>> list2 = (List<Map<String, String>>) map2.get(entry.getValue());
            Map<String, List<String>> lddict = new HashMap<>(16);
            if(CollUtil.isEmpty(list2)) {
                continue;
            }
            for (Map<String, String> sMap : list2) {
                Set<String> set1 = sMap.keySet();
                String buildingId = set1.iterator().next();
                List<String> bhList = (List<String>) map3.get(entry.getValue()+"-"+buildingId);
                lddict.put(sMap.get(buildingId), bhList);
            }
            dict.put(entry.getKey(), lddict);
        }
        map.put("Building_Unit", dict);
    }

    /**
     * 验证楼栋存在
     * @param e
     * @param map
     * @return
     */
    private String ExsitVerify(Object e, String extra, Map<String, Map<String, Object>> map){
        String value = (String) e;
        if(StrUtil.isBlank(value)) {
            return "楼栋名称不能为空";
        }
        Map<String, Object> map2 = map.get("Building_Id");
        if(!map2.containsKey(extra + value)) {
            return "楼栋名称" + value + "不存在";
        }
        return "";
    }

    /**
     * 校验单元名称是否已存在于社区
     * @param community -社区， e- 楼栋单元门户名称
     * @return
     */
    private String ExsitVerify(Object community, Object building, Object e, Map<String, Map<String, Object>> map){
        String result = "";
        String dymc = (String) e;
        if(StrUtil.isBlank(dymc)) {
            result = "单元名称必须填写";
        } else {
            Map<String, Object> dict = map.get("Building_Unit");
            if(dict != null) {
                Map<String, List<String>> map1 = (Map<String, List<String>>) dict.get(community);
                if(map1 != null) {
                    List<String> list = map1.get(building);
                    if(list != null) {
                        if(list.contains(dymc)) {
                            result += (result.length() == 0 ? "" : ",") + community + "-" + building + "-" + dymc + "已存在";
                        }
                    } else {
                        result += (result.length() == 0 ? "" : ",") + community + "-" + building + "”不存在";
                    }
                }
            }
        }
        return result;
    }
}
