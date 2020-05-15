package cn.citms.icw.service.impl.excel;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.Utils.ExcelUtils;
import cn.citms.icw.constant.IntelligentCommunityConstant;
import cn.citms.icw.entity.*;
import cn.citms.icw.mapper.*;
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
public class PersonCheckInImportImpl extends ExcelService implements InitializingBean {

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private BuildingMapper buildingMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private HouseMapper houseMapper;
    @Resource
    private PersoncheckinMapper personcheckinMapper;
    @Resource
    private BaseService baseService;

    private static final int ROW_INDEX = 5;
    private static final int READ_ROW_CELL_NUM = 25;

    public PersonCheckInImportImpl(){
        super(ROW_INDEX, READ_ROW_CELL_NUM);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExcelImportFactory.initService("person", this);
    }

    @Override
    protected Map<String, Map<String, Object>> GetExtraInfo(List<List<Object>> list) {
        Map<String, Map<String, Object>> map = new HashMap<>(16);
        GetXqmcDict(map);
        getDict(Arrays.asList(IntelligentCommunityConstant.XB, IntelligentCommunityConstant.RYLX,IntelligentCommunityConstant.YHBS,
                IntelligentCommunityConstant.MZ, IntelligentCommunityConstant.WHCD,IntelligentCommunityConstant.HYZK,
                IntelligentCommunityConstant.ZYLB,IntelligentCommunityConstant.JZSY,IntelligentCommunityConstant.ZZCS), map);
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
        String r2 = ExcelUtils.notNullVerify(l.get(2), "姓名", 50);
        if(StrUtil.isNotBlank(r2)) {
            sj.add(r2);
        }
        String r3 = ExcelUtils.SelectVerify(l.get(3), "人员类型", map.get("Rylx"), false);
        if(StrUtil.isNotBlank(r3)) {
            sj.add(r3);
        }
        String r4 = ExcelUtils.SelectVerify(l.get(4), "性别", map.get("Xbdm"), true);
        if(StrUtil.isNotBlank(r4)) {
            sj.add(r4);
        }
        String r5_1 = ExcelUtils.notNullVerify(l.get(5), "证件号码", 20);
        if(StrUtil.isBlank(r5_1)) {
            String r5 = ExcelUtils.CardNoVerify(l.get(5), "证件号码");
            if(StrUtil.isNotBlank(r5)) {
                sj.add(r5);
            } else {
                Set<String> cardNoList = (Set<String>) map.get("cardNo").get("cardNo");
                if(CollUtil.isNotEmpty(cardNoList) && cardNoList.contains(l.get(5))) {
                    sj.add("证件号码" + l.get(5)+"已存在");
                }
            }
        } else {
            sj.add(r5_1);
        }
        String r6 = ExcelUtils.DateVerify(l.get(6), "出生日期");
        if(StrUtil.isNotBlank(r6)) {
            sj.add(r6);
        }
        String r7 = ExcelUtils.SelectVerify(l.get(7), "民族", map.get("Mzdm"), true);
        if(StrUtil.isNotBlank(r7)) {
            sj.add(r7);
        }
        String r8 = ExcelUtils.SelectVerify(l.get(8), "文化程度", map.get("Whcddm"), true);
        if(StrUtil.isNotBlank(r8)) {
            sj.add(r8);
        }
        String r9 = ExcelUtils.SelectVerify(l.get(9), "婚姻状况", map.get("Hyzkdm"), true);
        if(StrUtil.isNotBlank(r9)) {
            sj.add(r9);
        }
        String r10 = ExcelUtils.lenVerify(l.get(10), "国籍", 50);
        if(StrUtil.isNotBlank(r10)) {
            sj.add(r10);
        }
        String r11 = ExcelUtils.lenVerify(l.get(11), "籍贯", 50);
        if(StrUtil.isNotBlank(r11)) {
            sj.add(r11);
        }
        String r12 = ExcelUtils.lenVerify(l.get(12), "户籍地址", 100);
        if(StrUtil.isNotBlank(r12)) {
            sj.add(r12);
        }
        String r13 = ExcelUtils.SelectVerify(l.get(13), "用户标识", map.get("Yhbs"), true);
        if(StrUtil.isNotBlank(r13)) {
            sj.add(r13);
        }
        String r14 = ExcelUtils.lenVerify(l.get(14), "服务单位", 100);
        if(StrUtil.isNotBlank(r14)) {
            sj.add(r14);
        }
        String r15 = ExcelUtils.SelectVerify(l.get(15), "职业类别", map.get("Zylb"), true);
        if(StrUtil.isNotBlank(r15)) {
            sj.add(r15);
        }
        String r16 = ExcelUtils.SelectVerify(l.get(16), "居(暂)住事由", map.get("Jzsy"), true);
        if(StrUtil.isNotBlank(r16)) {
            sj.add(r16);
        }
        String r17 = ExcelUtils.SelectVerify(l.get(17), "暂住处所", map.get("Zzcs"), true);
        if(StrUtil.isNotBlank(r17)) {
            sj.add(r17);
        }
        String r18 = ExcelUtils.telVerify(l.get(18), "联系方式");
        if(StrUtil.isNotBlank(r18)) {
            sj.add(r18);
        }
        String r19 = ExcelUtils.DateVerify(l.get(19), "入住时间");
        if(StrUtil.isNotBlank(r19)) {
            sj.add(r19);
        }
        String r20 = ExcelUtils.DateVerify(l.get(20), "撤离时间");
        if(StrUtil.isNotBlank(r20)) {
            sj.add(r20);
        }
        String r21 = ExcelUtils.lenVerify(l.get(21), "紧急联系人姓名", 50);
        if(StrUtil.isNotBlank(r21)) {
            sj.add(r21);
        }
        String r22 = ExcelUtils.telVerify(l.get(22), "紧急联系人电话");
        if(StrUtil.isNotBlank(r22)) {
            sj.add(r22);
        }
        String r23 = ExcelUtils.DateVerify(l.get(23), "访问时间");
        if(StrUtil.isNotBlank(r23)) {
            sj.add(r23);
        }
        String r24 = ExcelUtils.lenVerify(l.get(24), "访问人姓名", 50);
        if(StrUtil.isNotBlank(r24)) {
            sj.add(r24);
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
        Map<String, Object> map4 = map.get("house");
        Map<String, Object> map5 = map.get("Rylx");
        Map<String, Object> map6 = map.get("Xbdm");
        Map<String, Object> map7 = map.get("Mzdm");
        Map<String, Object> map8 = map.get("Whcddm");
        Map<String, Object> map9 = map.get("Hyzkdm");
        Map<String, Object> map10 = map.get("Yhbs");
        Map<String, Object> map11 = map.get("Zylb");
        Map<String, Object> map12 = map.get("Jzsy");
        Map<String, Object> map13 = map.get("Zzcs");
        List<Personcheckin> list1 = new ArrayList<>(list.size());
        for (int i = ROW_INDEX; i < list.size(); i++) {
            List<Object> res = list.get(i);
            Personcheckin c = new Personcheckin();
            c.setId(CommonUtils.createUUID());
            String communityId = (String) map1.get(res.get(0));
            c.setCommunity_Id(communityId);
            String[] cellVal = ((String) res.get(1)).split("-");
            String buildId = (String) map2.get(communityId+"-"+cellVal[0]);
            c.setBuilding_Id(buildId);
            String unitId = (String) map3.get(communityId+"-"+buildId+"-"+cellVal[1]);
            c.setUnit_Id(unitId);
            c.setHouse_Id((String) map4.get(communityId+"-"+buildId+"-"+unitId+"-"+cellVal[2]));
            c.setXm((String) res.get(2));
            c.setRylx((String) map5.get(res.get(3)));
            c.setXbdm((String) map6.get(res.get(4)));
            c.setZjhm((String) res.get(5));
            if(StrUtil.isNotBlank((String)res.get(6))) {
                try {
                    c.setCsrq(DateUtil.parseDate((String) res.get(6)).toJdkDate());
                } catch (Exception e) {
                    c.setCsrq(DateUtil.parseDateTime((String) res.get(6)).toJdkDate());
                }
            }
            c.setMzdm((String) map7.get(res.get(7)));
            c.setWhcddm((String) map8.get(res.get(8)));
            c.setHyzkdm((String) map9.get(9));
            c.setGjdz((String) res.get(10));
            c.setJgmc((String) res.get(11));
            c.setHjdz_Dzmc((String) res.get(12));
            c.setYhbs((String) map10.get(13));
            c.setFwdw((String) res.get(14));
            c.setZylbdm((String) map11.get(15));
            c.setJzsy((String) map12.get(16));
            c.setZzcs((String) map13.get(17));
            c.setLxfs((String) res.get(18));
            if(StrUtil.isNotBlank((String)res.get(19))) {
                try {
                    c.setRz_Rqsj(DateUtil.parseDate((String) res.get(19)).toJdkDate());
                } catch (Exception e) {
                    c.setRz_Rqsj(DateUtil.parseDateTime((String) res.get(19)).toJdkDate());
                }
            }
            if(StrUtil.isNotBlank((String)res.get(20))) {
                try {
                    c.setCl_Rqsj(DateUtil.parseDate((String) res.get(20)).toJdkDate());
                } catch (Exception e) {
                    c.setCl_Rqsj(DateUtil.parseDateTime((String) res.get(20)).toJdkDate());
                }
            }
            c.setJjlxr_Xm((String) res.get(21));
            c.setJjlxr_Lxdh((String) res.get(22));
            if(StrUtil.isNotBlank((String)res.get(23))) {
                try {
                    c.setFw_Rqsj(DateUtil.parseDate((String) res.get(23)).toJdkDate());
                } catch (Exception e) {
                    c.setFw_Rqsj(DateUtil.parseDateTime((String) res.get(23)).toJdkDate());
                }
            }
            c.setFwry_Xm((String) res.get(24));
            c.setBlsj(new Date());
            list1.add(c);
        }
        if(CollUtil.isNotEmpty(list1)) {
            personcheckinMapper.batchInsert(list1);
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
        Map<String, Object> map6 = new HashMap<>(16);
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
                        map4.put(house.getCommunity_Id()+"-"+house.getBuilding_Id()+"-"+house.getUnit_Id()+"-"+house.getFw_Bh(), house.getId());
                    }
                }
                List<Personcheckin> personList = personcheckinMapper.selectPersonByCommunityIds(communityIds);
                if(CollUtil.isNotEmpty(personList)) {
                    Set<String> cardNoList = new HashSet<>(10);
                    for (Personcheckin personcheckin : personList) {
                        cardNoList.add(personcheckin.getZjhm());

                        List<String> pList = (List<String>) map5.get(personcheckin.getHouse_Id());
                        if(CollUtil.isNotEmpty(pList)) {
                            pList.add(personcheckin.getZjhm());
                        } else {
                            pList = new ArrayList<>(10);
                            pList.add(personcheckin.getZjhm());
                            map5.put(personcheckin.getHouse_Id(), pList);
                        }
                    }
                    map6.put("cardNo", cardNoList);
                }
            }
        }
        map.put("Community_Id", map1);
        map.put("building", map2);
        map.put("unit", map3);
        map.put("house", map4);
        map.put("person", map5);
        map.put("cardNo", map6);
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
        map.put("Xbdm", findDict.get(IntelligentCommunityConstant.XB));
        map.put("Rylx", findDict.get(IntelligentCommunityConstant.RYLX));
        map.put("Yhbs", findDict.get(IntelligentCommunityConstant.YHBS));
        map.put("Mzdm", findDict.get(IntelligentCommunityConstant.MZ));
        map.put("Whcddm", findDict.get(IntelligentCommunityConstant.WHCD));
        map.put("Hyzkdm", findDict.get(IntelligentCommunityConstant.HYZK));
        map.put("Zylb", findDict.get(IntelligentCommunityConstant.ZYLB));
        map.put("Jzsy", findDict.get(IntelligentCommunityConstant.JZSY));
        map.put("Zzcs", findDict.get(IntelligentCommunityConstant.ZZCS));
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
                if(map.get("house").containsKey(communityId + "-" + buildingId + "-"+unitId+"-"+ cellValue[2])) {
                    mhmc = cellValue[2];
                }
                if (StrUtil.isBlank(mhmc)) {
                    return community + "-" + ldmc+ "-" + dymc + "-" + cellValue[2] + "不存在";
                }
            }
        }
        return "";
    }
}
