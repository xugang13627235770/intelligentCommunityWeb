package cn.citms.icw.service.impl.excel;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.Utils.ExcelUtils;
import cn.citms.icw.constant.IntelligentCommunityConstant;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.Vehicle;
import cn.citms.icw.mapper.CommunityMapper;
import cn.citms.icw.mapper.VehicleMapper;
import cn.citms.icw.service.ExcelService;
import cn.citms.icw.service.impl.BaseService;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import cn.citms.icw.factory.ExcelImportFactory;
import org.springblade.common.utils.UserContextHolder;
import org.springblade.common.vo.UserRightViewModel;
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
public class VehicleImportImpl extends ExcelService implements InitializingBean {

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private VehicleMapper vehicleMapper;
    @Resource
    private BaseService baseService;

    private static final int ROW_INDEX = 5;
    private static final int READ_ROW_CELL_NUM = 11;

    public VehicleImportImpl(){
        super(ROW_INDEX, READ_ROW_CELL_NUM);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExcelImportFactory.initService("Vehicle", this);
    }

    @Override
    protected Map<String, Map<String, Object>> GetExtraInfo(List<List<Object>> list) {
        Map<String, Map<String, Object>> map = new HashMap<>(16);
        GetXqmcDict(map);
        getDict(Arrays.asList(IntelligentCommunityConstant.HPYS, IntelligentCommunityConstant.HPZL,IntelligentCommunityConstant.CSYS), map);
        return map;
    }

    @Override
    protected String verify(List<Object> l, Map<String, Map<String, Object>> map) {
        StringJoiner sj = new StringJoiner(",");
        String r = ExcelUtils.SelectVerify(l.get(0), "社区名称", map.get("Community_Id"), false);
        if(StrUtil.isNotBlank(r)) {
            sj.add(r);
        }
        String r1 = ExcelUtils.XqmcVerify(l.get(1), "", map.get("PlateNo"), "号牌号码");
        if(StrUtil.isNotBlank(r1)) {
            sj.add(r1);
        }
        String r2 = ExcelUtils.SelectVerify(l.get(2), "号牌颜色", map.get("PlateColor"), false);
        if(StrUtil.isNotBlank(r2)) {
            sj.add(r2);
        }
        String r3 = ExcelUtils.SelectVerify(l.get(3), "号牌种类", map.get("PlateType"), false);
        if(StrUtil.isNotBlank(r3)) {
            sj.add(r3);
        }
        String r4 = ExcelUtils.notNullVerify(l.get(4), "登记车主", 20);
        if(StrUtil.isNotBlank(r4)) {
            sj.add(r4);
        }
        String r5 = ExcelUtils.lenVerify(l.get(5), "门牌信息", 200);
        if(StrUtil.isNotBlank(r5)) {
            sj.add(r5);
        }
        String r6 = ExcelUtils.telVerify(l.get(6), "联系电话");
        if(StrUtil.isNotBlank(r6)) {
            sj.add(r6);
        }
        String r7_1 = ExcelUtils.notNullVerify(l.get(7), "身份证", 50);
        if(StrUtil.isBlank(r7_1)) {
            String r7 = ExcelUtils.CardNoVerify(l.get(7), "身份证");
            if(StrUtil.isNotBlank(r7)) {
                sj.add(r7);
            }
        } else {
            sj.add(r7_1);
        }
        String r8 = ExcelUtils.lenVerify(l.get(8), "车主地址", 500);
        if(StrUtil.isNotBlank(r8)) {
            sj.add(r8);
        }
        String r9 = ExcelUtils.SelectVerify(l.get(9), "车身颜色", map.get("VehicleBodyColor"), true);
        if(StrUtil.isNotBlank(r9)) {
            sj.add(r9);
        }
        String r10 = ExcelUtils.lenVerify(l.get(10), "车辆品牌", 500);
        if(StrUtil.isNotBlank(r10)) {
            sj.add(r10);
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
        Map<String, Object> map2 = map.get("PlateColor");
        Map<String, Object> map3 = map.get("PlateType");
        Map<String, Object> map4 = map.get("VehicleBodyColor");
        List<Vehicle> list1 = new ArrayList<>(list.size());
        for (int i = ROW_INDEX; i < list.size(); i++) {
            List<Object> res = list.get(i);
            Vehicle c = new Vehicle();
            c.setVehicleId(CommonUtils.createUUID());
            c.setCreatedTime(new Date());
            UserRightViewModel user = UserContextHolder.getUserInfo();
            if(user != null) {
                c.setCreator(user.getUserCode());
            }
            c.setCommunityId((String) map1.get(res.get(0)));
            c.setPlateNo((String) res.get(1));
            c.setPlateColor((String) map2.get(res.get(2)));
            c.setPlateType((String) map3.get(res.get(3)));
            c.setOwnerName((String) res.get(4));
            c.setRoomNo((String) res.get(5));
            c.setPhone((String) res.get(6));
            c.setIdCardNo((String) res.get(7));
            c.setOwnerAddress((String) res.get(8));
            c.setVehicleBodyColor((String) map4.get(res.get(9)));
            c.setVehicleBrand((String) res.get(10));
            list1.add(c);
        }
        if(CollUtil.isNotEmpty(list1)) {
            vehicleMapper.batchInsert(list1);
        }
        return list1.size();
    }

    private void GetXqmcDict(Map<String, Map<String, Object>> map){
        List<String> departmentIds = baseService.getPermissionDepartment();
        Map<String, Object> map1 = new HashMap<>(16);
        Map<String, Object> map2 = new HashMap<>(16);
        if(CollUtil.isNotEmpty(departmentIds)) {
            List<Community> communityList = communityMapper.selectCommunityInfoByDeptIds(departmentIds);
            if(CollUtil.isNotEmpty(communityList)) {
                List<String> communityIds = new ArrayList<>(communityList.size());
                for (Community community : communityList) {
                    map1.put(community.getXqmc(), community.getId());
                    communityIds.add(community.getId());
                }

                List<Vehicle> vehicleList = vehicleMapper.selectByCommunityIds(communityIds);
                if(CollUtil.isNotEmpty(vehicleList)) {
                    for (Vehicle vehicle : vehicleList) {
                        String key = vehicle.getCommunityId() + "-" + vehicle.getPlateNo();
                        Integer i = (Integer) map2.get(key);
                        if(i == null) {
                            map2.put(key, 1);
                        } else {
                            map2.put(key, i + 1);
                        }
                    }
                }
            }
        }
        map.put("Community_Id", map1);
        map.put("PlateNo", map2);
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
        map.put("PlateColor", findDict.get(IntelligentCommunityConstant.HPYS));
        map.put("PlateType", findDict.get(IntelligentCommunityConstant.HPZL));
        map.put("VehicleBodyColor", findDict.get(IntelligentCommunityConstant.CSYS));
    }

}
