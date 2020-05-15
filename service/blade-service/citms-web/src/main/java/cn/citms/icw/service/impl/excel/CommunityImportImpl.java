package cn.citms.icw.service.impl.excel;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.Utils.ExcelUtils;
import cn.citms.icw.constant.IntelligentCommunityConstant;
import cn.citms.icw.entity.Community;
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
 * 社区excel数据导入
 * @author cyh
 */
@Component
public class CommunityImportImpl extends ExcelService implements InitializingBean {

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private BaseService baseService;

    private static final int ROW_INDEX = 5;
    private static final int READ_ROW_CELL_NUM = 18;

    public CommunityImportImpl(){
        super(ROW_INDEX, READ_ROW_CELL_NUM);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExcelImportFactory.initService("community", this);
    }

    @Override
    protected Map<String, Map<String, Object>> GetExtraInfo(List<List<Object>> list) {
        Map<String, Map<String, Object>> map = new HashMap<>(8);
        map.put("Xqmc", GetDict(list, 0));
        map.put("Xqbm", GetDict(list, 1));
        map.put("Ssxq", GetDepartment());
        map.put("Sqlx", getDict());
        return map;
    }

    @Override
    protected String verify(List<Object> l, Map<String, Map<String, Object>> map) {
        StringJoiner sj = new StringJoiner(",");
        String r = ExcelUtils.XqmcVerify(l.get(0), "", map.get("Xqmc"), "社区名称");
        if(StrUtil.isNotBlank(r)) {
            sj.add(r);
        }
        String r1 = ExcelUtils.XqmcVerify(l.get(1), "", map.get("Xqbm"), "社区编码");
        if(StrUtil.isNotBlank(r1)) {
            sj.add(r1);
        }
        String r2 = ExcelUtils.DateVerify(l.get(2), "建成时间");
        if(StrUtil.isNotBlank(r2)) {
            sj.add(r2);
        }
        String r3 = ExcelUtils.lenVerify(l.get(3), "社区地址", 100);
        if(StrUtil.isNotBlank(r3)) {
            sj.add(r3);
        }
        String r4 = ExcelUtils.SelectVerify(l.get(4), "社区类型", map.get("Sqlx"), true);
        if(StrUtil.isNotBlank(r4)) {
            sj.add(r4);
        }
        String r5 = ExcelUtils.DoubleVerify(l.get(5), "经度");
        if(StrUtil.isNotBlank(r5)) {
            sj.add(r5);
        }
        String r6 = ExcelUtils.DoubleVerify(l.get(6), "纬度");
        if(StrUtil.isNotBlank(r6)) {
            sj.add(r6);
        }
        String r7 = ExcelUtils.SelectVerify(l.get(7), "所属部门", map.get("Ssxq"), true);
        if(StrUtil.isNotBlank(r7)) {
            sj.add(r7);
        }
        String r8 = ExcelUtils.lenVerify(l.get(8), "片区民警", 100);
        if(StrUtil.isNotBlank(r8)) {
            sj.add(r8);
        }
        String r9 = ExcelUtils.NumberVerify(l.get(9), "警务室人数");
        if(StrUtil.isNotBlank(r9)) {
            sj.add(r9);
        }
        String r10 = ExcelUtils.lenVerify(l.get(10), "街道名称", 100);
        if(StrUtil.isNotBlank(r10)) {
            sj.add(r10);
        }
        String r11 = ExcelUtils.lenVerify(l.get(11), "居(村)委会名称", 100);
        if(StrUtil.isNotBlank(r11)) {
            sj.add(r11);
        }
        String r12 = ExcelUtils.DoubleVerify(l.get(12), "占地面积");
        if(StrUtil.isNotBlank(r12)) {
            sj.add(r12);
        }
        String r13 = ExcelUtils.lenVerify(l.get(13), "服务处所", 100);
        if(StrUtil.isNotBlank(r13)) {
            sj.add(r13);
        }
        String r14 = ExcelUtils.lenVerify(l.get(14), "社区物业", 100);
        if(StrUtil.isNotBlank(r14)) {
            sj.add(r14);
        }
        String r15 = ExcelUtils.lenVerify(l.get(15), "物业负责人", 100);
        if(StrUtil.isNotBlank(r15)) {
            sj.add(r15);
        }
        String r16 = ExcelUtils.lenVerify(l.get(16), "卫生服务站", 100);
        if(StrUtil.isNotBlank(r16)) {
            sj.add(r16);
        }
        String r17 = ExcelUtils.NumberVerify(l.get(17), "服务站人数");
        if(StrUtil.isNotBlank(r17)) {
            sj.add(r17);
        }
        return sj.toString();
    }

    @Override
    protected String writeErrorMsg(MultipartFile fileDoc, List<String> errorMsg, String serverAddress) throws IOException {
        return ExcelUtils.writeErrorMsg(fileDoc, errorMsg, serverAddress, ROW_INDEX, READ_ROW_CELL_NUM);
    }

    @Override
    protected int saveData(List<List<Object>> list, Map<String, Map<String, Object>> map){
        Map<String, Object> map1 = map.get("Sqlx");
        Map<String, Object> map2 = map.get("Ssxq");
        List<Community> list1 = new ArrayList<>(list.size());
        for (int i = ROW_INDEX; i < list.size(); i++) {
            List<Object> res = list.get(i);
            Community c = new Community();
            c.setId(CommonUtils.createUUID());
            c.setXqmc((String) res.get(0));
            c.setXqbm((String) res.get(1));
            if(StrUtil.isNotBlank((String)res.get(2))) {
                try {
                    c.setCjsj(DateUtil.parseDate((String) res.get(2)).toJdkDate());
                } catch (Exception e) {
                    c.setCjsj(DateUtil.parseDateTime((String) res.get(2)).toJdkDate());
                }
            }
            c.setXqdz((String) res.get(3));
            c.setSqlx((String) map1.get(res.get(4)));
            String dqjd = (String) res.get(5);
            Double dqjd2 = StrUtil.isBlank(dqjd) ? null : Double.valueOf(dqjd);
            c.setDqjd(dqjd2);
            String dqwd = (String) res.get(6);
            Double dqwd2 = StrUtil.isBlank(dqwd) ? null : Double.valueOf(dqwd);
            c.setDqwd(dqwd2);
            c.setSsxq((String) map2.get(res.get(7)));
            c.setZqmj((String) res.get(8));
            String jwsrs = (String) res.get(9);
            Double jwsrs2 = StrUtil.isBlank(jwsrs) ? null : Double.valueOf(jwsrs);
            c.setJwsrs(jwsrs2);
            c.setJlxmc((String) res.get(10));
            c.setSqjcwhmc((String) res.get(11));
            String zdmj = (String) res.get(12);
            Double zdmj2 = StrUtil.isBlank(zdmj) ? null : Double.valueOf(zdmj);
            c.setZdmj(zdmj2);
            c.setFwcs((String) res.get(13));
            c.setWymc((String) res.get(14));
            c.setWyfzr((String) res.get(15));
            c.setFwzmc((String) res.get(16));
            String fwzrs = (String) res.get(17);
            Double fwzrs2 = StrUtil.isBlank(fwzrs) ? null : Double.valueOf(fwzrs);
            c.setFwzrs(fwzrs2);
            list1.add(c);
        }
        if(CollUtil.isNotEmpty(list1)) {
            communityMapper.batchInsert(list1);
        }
        return list1.size();
    }

    /**
     * @param list
     * @param k 0-获取社区名称，1-获取社区编码
     * @return
     */
    private Map<String, Object> GetDict(List<List<Object>> list, int k){
        List<String> mcList = new ArrayList<>(list.size());
        for (List<Object> objectList : list) {
            mcList.add((String) objectList.get(k));
        }
        List<String> sqmcList = CollUtil.newArrayList();
        if(k == 0) {
            sqmcList = communityMapper.selectCommunityNameBySqmc(mcList);
        } else if(k == 1) {
            sqmcList = communityMapper.selectCommunityBmBySqmc(mcList);
        }
        Map<String, Object> map = new HashMap<>(16);
        if(CollUtil.isNotEmpty(sqmcList)) {
            for (String s : sqmcList) {
                Integer i = (Integer) map.get(s);
                if(i == null) {
                    map.put(s, 1);
                } else {
                    map.put(s, i + 1);
                }
            }
        }
        return map;
    }

    private Map<String, Object> GetDepartment(){
        return baseService.getPermissionDepartmentMap();
    }

    private Map<String, Object> getDict(){
        Map<Integer, Map<String, Object>> findDict = baseService.findDictValue(Collections.singletonList(IntelligentCommunityConstant.SQLX));
        if(findDict == null) {
            return new HashMap<>(0);
        }
        return findDict.get(IntelligentCommunityConstant.SQLX);
    }
}
