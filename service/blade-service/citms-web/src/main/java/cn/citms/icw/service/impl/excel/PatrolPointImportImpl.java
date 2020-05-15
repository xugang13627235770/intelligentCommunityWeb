package cn.citms.icw.service.impl.excel;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.Utils.ExcelUtils;
import cn.citms.icw.dao.ESPatrollogDao;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.ESPatrollog;
import cn.citms.icw.entity.Patrolpoint;
import cn.citms.icw.mapper.CommunityMapper;
import cn.citms.icw.mapper.PatrolpointMapper;
import cn.citms.icw.service.ExcelService;
import cn.citms.icw.service.impl.BaseService;
import cn.citms.icw.vo.EsPatrollogSearchVO;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import cn.citms.icw.factory.ExcelImportFactory;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springblade.core.tool.utils.BeanUtil;
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
public class PatrolPointImportImpl extends ExcelService implements InitializingBean {

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private PatrolpointMapper patrolpointMapper;
    @Resource
    private ESPatrollogDao eSPatrollogDao;
    @Resource
    private BaseService baseService;

    private static final int ROW_INDEX = 5;
    private static final int READ_ROW_CELL_NUM = 4;

    public PatrolPointImportImpl(){
        super(ROW_INDEX, READ_ROW_CELL_NUM);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExcelImportFactory.initService("patrolpoint", this);
    }

    @Override
    protected Map<String, Map<String, Object>> GetExtraInfo(List<List<Object>> list) {
        Map<String, Map<String, Object>> map = new HashMap<>(8);
        GetXqmcDict(map, list);
        return map;
    }

    @Override
    protected String verify(List<Object> l, Map<String, Map<String, Object>> map) {
        StringJoiner sj = new StringJoiner(",");
        String r = ExcelUtils.SelectVerify(l.get(0), "社区名称", map.get("Community_Id"), false);
        if(StrUtil.isNotBlank(r)) {
            sj.add(r);
        }
        String communityId = (String) map.get("Community_Id").get(l.get(0));
        if(communityId == null) {
            communityId = "";
        }
        String r1 = ExcelUtils.XqmcVerify(l.get(1), communityId+"-", map.get("dictPoint"), "巡更点编号");
        if(StrUtil.isNotBlank(r1)) {
            sj.add(r1);
        }
        String r2 = ExcelUtils.DoubleVerify(l.get(2), "经度");
        if(StrUtil.isNotBlank(r2)) {
            sj.add(r2);
        }
        String r3 = ExcelUtils.DoubleVerify(l.get(3), "纬度");
        if(StrUtil.isNotBlank(r3)) {
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
        Map<String, Object> map2 = map.get("PointNumber");
        List<Patrolpoint> list1 = new ArrayList<>(list.size());
        for (int i = ROW_INDEX; i < list.size(); i++) {
            List<Object> res = list.get(i);
            Patrolpoint c = new Patrolpoint();
            c.setId(CommonUtils.createUUID());
            c.setCommunityId((String) map1.get(res.get(0)));
            c.setPointNumber((String) res.get(1));
            Map<String, String> map3 = (Map<String, String>) map2.get(res.get(0));
            if(map3 != null) {
                c.setPointName(map3.get(res.get(1)));
            }
            String jqjd = (String) res.get(2);
            Double jqjd2 = StrUtil.isBlank(jqjd) ? null : Double.valueOf(jqjd);
            c.setDqjd(jqjd2);
            String jqwd = (String) res.get(3);
            Double jqwd2 = StrUtil.isBlank(jqwd) ? null : Double.valueOf(jqwd);
            c.setDqwd(jqwd2);
            list1.add(c);
        }
        if(CollUtil.isNotEmpty(list1)) {
            patrolpointMapper.batchInsert(list1);
        }
        return list1.size();
    }

    private void GetXqmcDict(Map<String, Map<String, Object>> map, List<List<Object>> list){
        List<String> departmentIds = baseService.getPermissionDepartment();
        Map<String, Object> map1 = new HashMap<>(16);
        Map<String, Object> map2 = new HashMap<>(16);
        Map<String, Object> map5 = new HashMap<>(16);
        if(CollUtil.isNotEmpty(departmentIds)) {
            List<Community> communityList = communityMapper.selectCommunityInfoByDeptIds(departmentIds);
            if(CollUtil.isNotEmpty(communityList)) {
                List<String> communityIds = new ArrayList<>(communityList.size());
                List<String> noList = new ArrayList<>(10);
                Map<String, Object> map3 = new HashMap<>(16);
                for (Community community : communityList) {
                    map1.put(community.getXqmc(), community.getId());
                    communityIds.add(community.getId());
                    noList.add(community.getXqbm());
                    map3.put(community.getXqbm(), community.getXqmc());
                }

                List<Patrolpoint> patrolpointList = patrolpointMapper.selectByCommunityIds(communityIds);
                if(CollUtil.isNotEmpty(patrolpointList)) {
                    for (Patrolpoint patrolpoint : patrolpointList) {
                        String key = patrolpoint.getCommunityId() + "-" + patrolpoint.getPointNumber();
                        Integer i = (Integer) map2.get(key);
                        if(i == null) {
                            map2.put(key, 1);
                        } else {
                            map2.put(key, i + 1);
                        }
                    }
                }

                List<String> numberList = new ArrayList<>(list.size());
                for (List<Object> objects : list) {
                    String number = (String) objects.get(1);
                    if(StrUtil.isNotBlank(number)) {
                        numberList.add(number);
                    }
                }
                EsPatrollogSearchVO vo = new EsPatrollogSearchVO();
                vo.setExts(noList);
                vo.setPointNumbers(numberList);
                vo.setPageindex(0);
                vo.setPagesize(1000);
                ESDatas<ESPatrollog> esds = eSPatrollogDao.getPatrollogInfos(new HashMap<>(BeanUtil.toMap(vo)));
                List<ESPatrollog> ls = esds.getDatas();
                if (CollUtil.isNotEmpty(ls)) {
                    for (ESPatrollog l : ls) {
                        String sqmc = (String) map3.get(l.getExt());
                        if(StrUtil.isNotBlank(sqmc)) {
                            Map<String, String> map4 = (Map<String, String>) map5.get(sqmc);
                            if(map4 == null) {
                                map4 = new HashMap<>(16);
                            }
                            if(!map4.containsKey(l.getPointNumber())) {
                                map4.put(l.getPointNumber(), l.getPointName());
                            }
                            map5.put(sqmc, map4);
                        }
                    }
                }

            }
        }
        map.put("Community_Id", map1);
        map.put("dictPoint", map2);
        map.put("PointNumber", map5);
    }

}
