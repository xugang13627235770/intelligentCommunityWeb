package cn.citms.icw.service.impl.excel;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.Utils.ExcelUtils;
import cn.citms.icw.constant.IntelligentCommunityConstant;
import cn.citms.icw.entity.Building;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.House;
import cn.citms.icw.entity.Unit;
import cn.citms.icw.mapper.BuildingMapper;
import cn.citms.icw.mapper.CommunityMapper;
import cn.citms.icw.mapper.HouseMapper;
import cn.citms.icw.mapper.UnitMapper;
import cn.citms.icw.service.ExcelService;
import cn.citms.icw.service.impl.BaseService;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import cn.citms.icw.factory.ExcelImportFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 门户excel数据导入
 * @author cyh
 */
@Component
public class HouseImportImpl extends ExcelService implements InitializingBean {

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private BuildingMapper buildingMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private HouseMapper houseMapper;
    @Resource
    private BaseService baseService;

    private static final int ROW_INDEX = 5;
    private static final int READ_ROW_CELL_NUM = 15;

    public HouseImportImpl(){
        super(ROW_INDEX, READ_ROW_CELL_NUM);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExcelImportFactory.initService("house", this);
    }

    @Override
    protected Map<String, Map<String, Object>> GetExtraInfo(List<List<Object>> list) {
        Map<String, Map<String, Object>> map = new HashMap<>(16);
        GetXqmcDict(map);
        getDict(Arrays.asList(IntelligentCommunityConstant.FWZT, IntelligentCommunityConstant.FWLX,IntelligentCommunityConstant.FWSYQLX, IntelligentCommunityConstant.ZJLX), map);
        return map;
    }

    @Override
    protected String verify(List<Object> l, Map<String, Map<String, Object>> map) {
        StringJoiner sj = new StringJoiner(",");
        String r = ExcelUtils.SelectVerify(l.get(0), "社区名称", map.get("Community_Id"), false);
        if(StrUtil.isNotBlank(r)) {
            sj.add(r);
        }
        String r1 = ExcelUtils.lddymcVerify(l.get(1));
        if(StrUtil.isNotBlank(r1)) {
            sj.add(r1);
        }
        String r1_1 = ExsitVerify(l.get(0), l.get(1), map);
        if(StrUtil.isNotBlank(r1_1)) {
            sj.add(r1_1);
        }
        String r2 = ExcelUtils.SelectVerify(l.get(2), "所有权类型", map.get("Syq_Lx"), true);
        if(StrUtil.isNotBlank(r2)) {
            sj.add(r2);
        }
        String r31 = ExcelUtils.SelectVerify(l.get(3), "房屋状态", map.get("Fw_Zt"), true);
        if(StrUtil.isNotBlank(r31)) {
            sj.add(r31);
        }
        String r4 = ExcelUtils.SelectVerify(l.get(4), "房屋类型", map.get("Fw_Lx"), true);
        if(StrUtil.isNotBlank(r4)) {
            sj.add(r4);
        }
        String r5 = ExcelUtils.lenVerify(l.get(5), "房间ID", 100);
        if(StrUtil.isNotBlank(r5)) {
            sj.add(r5);
        }
        String r6 = ExcelUtils.NumberVerify(l.get(6), "所在楼层号");
        if(StrUtil.isNotBlank(r6)) {
            sj.add(r6);
        }
        String r7 = ExcelUtils.DateVerify(l.get(7), "入住时间");
        if(StrUtil.isNotBlank(r7)) {
            sj.add(r7);
        }
        String r8 = ExcelUtils.DateVerify(l.get(8), "过期时间");
        if(StrUtil.isNotBlank(r8)) {
            sj.add(r8);
        }
        String r9 = ExcelUtils.DoubleVerify(l.get(9), "房屋面积");
        if(StrUtil.isNotBlank(r9)) {
            sj.add(r9);
        }
        String r10 = ExcelUtils.lenVerify(l.get(10), "房屋地址", 100);
        if(StrUtil.isNotBlank(r10)) {
            sj.add(r10);
        }
        String r11 = ExcelUtils.lenVerify(l.get(11), "委托代理人姓名", 50);
        if(StrUtil.isNotBlank(r11)) {
            sj.add(r11);
        }
        String r12 = ExcelUtils.lenVerify(l.get(12), "委托代理人证件类型", 100);
        if(StrUtil.isNotBlank(r12)) {
            sj.add(r12);
        }
        String r13 = ExcelUtils.CardNoVerify(l.get(13), "委托代理人证件号码");
        if(StrUtil.isNotBlank(r13)) {
            sj.add(r13);
        }
        String r14 = ExcelUtils.telVerify(l.get(14), "委托代理人电话");
        if(StrUtil.isNotBlank(r14)) {
            sj.add(r14);
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
        Map<String, Object> map4 = map.get("wtdlr_Zjlxmc");
        Map<String, Object> map5 = map.get("Fw_Zt");
        Map<String, Object> map6 = map.get("Fw_Lx");
        Map<String, Object> map7 = map.get("Syq_Lx");
        List<House> list1 = new ArrayList<>(list.size());
        for (int i = ROW_INDEX; i < list.size(); i++) {
            List<Object> res = list.get(i);
            House c = new House();
            c.setId(CommonUtils.createUUID());
            String communityId = (String) map1.get(res.get(0));
            c.setCommunity_Id(communityId);
            String[] cellVal = ((String) res.get(1)).split("-");
            String buildId = (String) map2.get(communityId+"-"+cellVal[0]);
            c.setBuilding_Id(buildId);
            c.setUnit_Id((String) map3.get(communityId+"-"+buildId+"-"+cellVal[1]));
            c.setFw_Bh(cellVal[2]);
            c.setSyq_Lx((String) map7.get(res.get(2)));
            c.setFw_Zt((String) map5.get(res.get(3)));
            c.setFw_Lx((String) map6.get(res.get(4)));
            c.setFjId((String) res.get(5));
            c.setSz_Lch((String) res.get(6));
            if(StrUtil.isNotBlank((String)res.get(7))) {
                try {
                    c.setRz_Rqsj(DateUtil.parseDate((String) res.get(7)).toJdkDate());
                } catch (Exception e) {
                    c.setRz_Rqsj(DateUtil.parseDateTime((String) res.get(7)).toJdkDate());
                }
            }
            if(StrUtil.isNotBlank((String)res.get(8))) {
                try {
                    c.setGq_Rqsj(DateUtil.parseDate((String) res.get(8)).toJdkDate());
                } catch (Exception e) {
                    c.setGq_Rqsj(DateUtil.parseDateTime((String) res.get(8)).toJdkDate());
                }
            }
            String mjpfm = (String) res.get(9);
            Double mjpfm2 = StrUtil.isBlank(mjpfm) ? null : Double.valueOf(mjpfm);
            c.setJzmj_Mjpfm(mjpfm2);
            c.setFangWu_Dzmc((String) res.get(10));
            c.setWtdlr_Xm((String) res.get(11));
            c.setWtdlr_Zjlxmc((String) map4.get(res.get(12)));
            c.setWtdlr_Zjhm((String) res.get(13));
            c.setWtdlr_Lxdh((String) res.get(14));
            list1.add(c);
        }
        if(CollUtil.isNotEmpty(list1)) {
            houseMapper.batchInsert(list1);
        }
        return list1.size();
    }

    private void GetXqmcDict(Map<String, Map<String, Object>> map){
        List<String> departmentIds = baseService.getPermissionDepartment();
        Map<String, Object> map1 = new HashMap<>(16);
        Map<String, Object> map2 = new HashMap<>(16);
        Map<String, Object> map3 = new HashMap<>(16);
        Map<String, Object> map4 = new HashMap<>(16);
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
                        map2.put(building.getCommunity_Id()+"-"+building.getLdh_Mc(), building.getId());
                    }
                }
                List<Unit> unitList = unitMapper.selectByCommunityIds(communityIds);
                if(CollUtil.isNotEmpty(unitList)) {
                    for (Unit unit : unitList) {
                        map3.put(unit.getCommunity_Id()+"-"+unit.getBuilding_Id()+"-"+unit.getDy_Mc(), unit.getId());
                    }
                }
                List<House> houseList = houseMapper.selectByCommunityIds(communityIds);
                if(CollUtil.isNotEmpty(houseList)) {
                    for (House house : houseList) {
                        List<String> bhList = (List<String>) map4.get(house.getUnit_Id());
                        if(CollUtil.isNotEmpty(bhList)) {
                            bhList.add(house.getFw_Bh());
                        } else {
                            bhList = new ArrayList<>(10);
                            bhList.add(house.getFw_Bh());
                            map4.put(house.getUnit_Id(), bhList);
                        }
                    }
                }
            }
        }
        map.put("Community_Id", map1);
        map.put("building", map2);
        map.put("unit", map3);
        map.put("house", map4);
    }

    /**
     * 查询字典
     * @return
     */
    private void getDict(List<Integer> list, Map<String, Map<String, Object>> map){
        Map<Integer, Map<String, Object>> findDict = baseService.findDictValue(list);
        if(findDict == null) {
            return;
        }
        map.put("Fw_Zt", findDict.get(IntelligentCommunityConstant.FWZT));
        map.put("Fw_Lx", findDict.get(IntelligentCommunityConstant.FWLX));
        map.put("Syq_Lx", findDict.get(IntelligentCommunityConstant.FWSYQLX));
        map.put("wtdlr_Zjlxmc", findDict.get(IntelligentCommunityConstant.ZJLX));
    }

    /**
     * 校验门户名称是否已存在于社区
     * @param community -社区， e- 楼栋单元门户名称
     * @return
     */
    private String ExsitVerify(Object community, Object e, Map<String, Map<String, Object>> map){
        String value = (String) e;
        if(StrUtil.isNotBlank(value)) {
            String[] cellValue = value.split("-");
            String ldmc = "", dymc = "", mhmc = "";
            if (cellValue.length == 3) {
                String communityId = (String) map.get("Community_Id").get(community);
                if (StrUtil.isBlank(communityId)) {
                    return community + "不存在";
                }
                String buildingId = "";
                if (map.get("building").containsKey(communityId + "-" + cellValue[0])) {
                    ldmc = cellValue[0];
                    buildingId = (String) map.get("building").get(communityId + "-" + cellValue[0]);
                }
                if (StrUtil.isBlank(ldmc)) {
                    return community + "-" + cellValue[0] + "不存在";
                }
                String unitId = "";
                if (map.get("unit").containsKey(communityId + "-" + buildingId + "-" + cellValue[1])) {
                    dymc = cellValue[1];
                    unitId = (String) map.get("unit").get(communityId + "-" + buildingId + "-" + cellValue[1]);
                }
                if (StrUtil.isBlank(dymc)) {
                    return community + "-" + ldmc + "-" + cellValue[1] + "不存在";
                }
                mhmc = cellValue[2];
                List<String> houseList = (List<String>) map.get("house").get(unitId);
                if (StrUtil.isNotBlank(mhmc) && CollUtil.isNotEmpty(houseList)) {
                    if (houseList.contains(mhmc)) {
                        return community + "-" + ldmc + "-" + dymc + "-" + mhmc + "已存在";
                    }
                }
            }
        }
        return "";
    }
}
