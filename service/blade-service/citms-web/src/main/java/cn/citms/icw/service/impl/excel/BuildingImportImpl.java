package cn.citms.icw.service.impl.excel;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.Utils.ExcelUtils;
import cn.citms.icw.constant.IntelligentCommunityConstant;
import cn.citms.icw.entity.Building;
import cn.citms.icw.entity.Community;
import cn.citms.icw.mapper.BuildingMapper;
import cn.citms.icw.mapper.CommunityMapper;
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
 * 楼栋excel数据导入
 * @author cyh
 */
@Component
public class BuildingImportImpl extends ExcelService implements InitializingBean {

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private BuildingMapper buildingMapper;
    @Resource
    private BaseService baseService;

    private static final int ROW_INDEX = 5;
    private static final int READ_ROW_CELL_NUM = 11;

    public BuildingImportImpl(){
        super(ROW_INDEX, READ_ROW_CELL_NUM);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExcelImportFactory.initService("building", this);
    }

    @Override
    protected Map<String, Map<String, Object>> GetExtraInfo(List<List<Object>> list) {
        Map<String, Map<String, Object>> map = new HashMap<>(8);
        map.put("Community_Id", GetXqmcDict());
        map.put("Ldh_Mc", GetLdmcDict(map.get("Community_Id")));
        map.put("Jz_Xz", GetSingleDict());
        return map;
    }

    @Override
    protected String verify(List<Object> l, Map<String, Map<String, Object>> map) {
        StringJoiner sj = new StringJoiner(",");
        String r0 = ExcelUtils.SelectVerify(l.get(0), "社区名称", map.get("Community_Id"), false);
        if(StrUtil.isNotBlank(r0)) {
            sj.add(r0);
        }
        String r1 = ExcelUtils.XqmcVerify(l.get(1), map.get("Community_Id").get(l.get(0))+"-", map.get("Ldh_Mc"), "楼栋名称");
        if(StrUtil.isNotBlank(r1)) {
            sj.add(r1);
        }
        String r2 = ExcelUtils.DateVerify(l.get(2), "建成时间");
        if(StrUtil.isNotBlank(r2)) {
            sj.add(r2);
        }
        String r3 = ExcelUtils.DoubleVerify(l.get(3), "经度");
        if(StrUtil.isNotBlank(r3)) {
            sj.add(r3);
        }
        String r4 = ExcelUtils.DoubleVerify(l.get(4), "纬度");
        if(StrUtil.isNotBlank(r4)) {
            sj.add(r4);
        }
        String r5 = ExcelUtils.DoubleVerify(l.get(5), "占地面积");
        if(StrUtil.isNotBlank(r5)) {
            sj.add(r5);
        }
        String r6 = ExcelUtils.lenVerify(l.get(6), "建筑编码", 100);
        if(StrUtil.isNotBlank(r6)) {
            sj.add(r6);
        }
        String r7 = ExcelUtils.SelectVerify(l.get(7), "建筑性质", map.get("Jz_Xz"), true);
        if(StrUtil.isNotBlank(r7)) {
            sj.add(r7);
        }
        String r8 = ExcelUtils.NumberVerify(l.get(8), "地面楼层");
        if(StrUtil.isNotBlank(r8)) {
            sj.add(r8);
        }
        String r9 = ExcelUtils.NumberVerify(l.get(9), "地下楼层");
        if(StrUtil.isNotBlank(r9)) {
            sj.add(r9);
        }
        String r10 = ExcelUtils.NumberVerify(l.get(10), "地下居住层");
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
    protected int saveData(List<List<Object>> list, Map<String, Map<String, Object>> map) {
        Map<String, Object> map1 = map.get("Community_Id");
        Map<String, Object> map2 = map.get("Jz_Xz");
        List<Building> list1 = new ArrayList<>(list.size());
        for (int i = ROW_INDEX; i < list.size(); i++) {
            List<Object> res = list.get(i);
            Building b = new Building();
            b.setId(CommonUtils.createUUID());
            b.setCommunity_Id((String) map1.get(res.get(0)));
            b.setLdh_Mc((String) res.get(1));
            if(StrUtil.isNotBlank((String)res.get(2))) {
                try {
                    b.setCjsj(DateUtil.parseDate((String) res.get(2)).toJdkDate());
                } catch (Exception e) {
                    b.setCjsj(DateUtil.parseDateTime((String) res.get(2)).toJdkDate());
                }
            }
            String dqjd = (String) res.get(3);
            Double dqjd2 = StrUtil.isBlank(dqjd) ? null : Double.valueOf(dqjd);
            b.setDqjd(dqjd2);
            String dqwd = (String) res.get(4);
            Double dqwd2 = StrUtil.isBlank(dqwd) ? null : Double.valueOf(dqwd);
            b.setDqwd(dqwd2);
            String zdmj = (String) res.get(5);
            Double zdmj2 = StrUtil.isBlank(zdmj) ? null : Double.valueOf(zdmj);
            b.setMjpfm(zdmj2);
            b.setJz_Bm((String) res.get(6));
            b.setJz_Xz((String) map2.get(res.get(7)));
            String dmcs = (String) res.get(8);
            Integer dmcs2 = StrUtil.isBlank(dmcs) ? null : Integer.valueOf(dmcs);
            b.setDm_Cs(dmcs2);
            String dxcs = (String) res.get(9);
            Integer dxcs2 = StrUtil.isBlank(dxcs) ? null : Integer.valueOf(dxcs);
            b.setDx_Cs(dxcs2);
            String dxcj = (String) res.get(10);
            Double dxcj2 = StrUtil.isBlank(dxcj) ? null : Double.valueOf(dxcj);
            b.setDxcjz_Cs(dxcj2);
            list1.add(b);
        }
        if(CollUtil.isNotEmpty(list1)) {
            buildingMapper.batchInsert(list1);
        }
        return list1.size();
    }

    private Map<String, Object> GetXqmcDict(){
        List<String> departmentIds = baseService.getPermissionDepartment();
        Map<String, Object> map = new HashMap<>(16);
        if(CollUtil.isNotEmpty(departmentIds)) {
            List<Community> communityList = communityMapper.selectCommunityInfoByDeptIds(departmentIds);
            if(CollUtil.isNotEmpty(communityList)) {
                for (Community community : communityList) {
                    map.put(community.getXqmc(), community.getId());
                }
            }
        }
        return map;
    }

    private Map<String, Object> GetLdmcDict(Map<String, Object> communityMap) {
        if(CollUtil.isNotEmpty(communityMap)) {
            Map<String, Object> map = new HashMap<>(16);
            List<String> communityIds = CollUtil.newArrayList();
            for (Object value : communityMap.values()) {
                communityIds.add(String.valueOf(value));
            }
            List<Building> buildingList = buildingMapper.selectByCommunityIds(communityIds);
            if(CollUtil.isNotEmpty(buildingList)) {
                for (Building building : buildingList) {
                    String key = building.getCommunity_Id() + "-" + building.getLdh_Mc();
                    Integer i = (Integer) map.get(key);
                    if(i == null) {
                        map.put(key, 1);
                    } else {
                        map.put(key, i + 1);
                    }
                }
            }
            return map;
        }
        return new HashMap<>(0);
    }

    private Map<String, Object> GetSingleDict(){
        Map<Integer, Map<String, Object>> findDict = baseService.findDictValue(Collections.singletonList(IntelligentCommunityConstant.JZXZ));
        if(findDict == null) {
            return new HashMap<>(0);
        }
        return findDict.get(IntelligentCommunityConstant.JZXZ);
    }
}
