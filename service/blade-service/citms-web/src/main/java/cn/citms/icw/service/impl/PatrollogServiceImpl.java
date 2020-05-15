package cn.citms.icw.service.impl;

import cn.citms.icw.Utils.PageResult;
import cn.citms.icw.dao.ESPatrollogDao;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.ESPatrollog;
import cn.citms.icw.entity.Patrolpoint;
import cn.citms.icw.entity.Patroltask;
import cn.citms.icw.service.*;
import cn.citms.icw.vo.EsPatrollogSearchVO;
import cn.citms.icw.vo.EsPatrollogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.date.DatePattern;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * <pre>
 *     报警数据 服务类实现类
 * </pre>
 *
 * @author liuyuyang
 */
@Service
public class PatrollogServiceImpl implements IPatrollogService {

    @Resource
    private ESPatrollogDao eSPatrollogDao;
    @Autowired
    private IBaseService   baseService;
    @Autowired
    private IPatroltaskService patroltaskService;
    @Autowired
    private IPatrolpointService patrolpointService;
    @Autowired
    private ICommunityService communityService;

    /**
     * 查询巡更记录-分页
     * @param vo
     * @return
     */
    @Override
    public IPage<EsPatrollogVO> queryPatrollog(EsPatrollogSearchVO vo) {
        List<EsPatrollogVO> ls = new CopyOnWriteArrayList<>();

//        if(StringUtils.isNotEmpty(vo.getSqmc())){
//            Community communityCon = new Community();
//            communityCon.setXqmc(vo.getSqmc());
//            Community communityResult = communityService.getOne(Condition.getQueryWrapper(communityCon));
//            if(communityResult != null){
//                vo.setExt(communityResult.getXqbm());
//            }
//        }

        if(StringUtils.isNotEmpty(vo.getCommunityId())){
            Community communityResult = communityService.getById(vo.getCommunityId());
            if(communityResult != null){
                vo.setExt(communityResult.getXqbm());
            }
        }

        Map<String, Object> map = new HashMap<>(BeanUtil.toMap(vo));
        if (ObjectUtil.isNotNull(vo.getStartTime()) && ObjectUtil.isNotNull(vo.getEndTime())) {
            map.put("startTime", DateUtil.format(DateUtil.date(vo.getStartTime().getTime()), DatePattern.NORM_DATETIME_PATTERN));
            map.put("endTime", DateUtil.format(DateUtil.date(vo.getEndTime().getTime()), DatePattern.NORM_DATETIME_PATTERN));
        }
        ESDatas<ESPatrollog> esds = eSPatrollogDao.getPatrollogInfos(map);
        if (CollUtil.isNotEmpty(esds.getDatas())) {
            ls = esds.getDatas().parallelStream().map(
                    this::convert
            ).collect(Collectors.toList());
        }
        return new PageResult<>(vo.getPageindex(), vo.getPagesize(), esds.getTotalSize(), ls);
    }

    /**
     * es返回给前端字段属性转换
     */
    private EsPatrollogVO convert(ESPatrollog ea) {
        EsPatrollogVO vo = EsPatrollogVO.builder().build();
        BeanUtil.copy(ea, vo);
        Community c = baseService.getCommunityInfo(ea.getExt());
        if (ObjectUtil.isNotNull(c)) {
            vo.setSqmc(c.getXqmc());
            vo.setCommunityId(c.getId());
        }

        //巡更人信息
        if(StringUtils.isNotEmpty(ea.getDeviceNo()) && StringUtils.isNotEmpty(vo.getCommunityId())){
            Patroltask patrolTaskCon = new Patroltask();
            patrolTaskCon.setDeviceNo(ea.getDeviceNo());
            patrolTaskCon.setCommunityId(vo.getCommunityId());
            Patroltask patrolTaskResult = patroltaskService.getOne(Condition.getQueryWrapper(patrolTaskCon));

            if(patrolTaskResult != null){
                vo.setStaffName(patrolTaskResult.getName());
                vo.setIdCard(patrolTaskResult.getIdCard());
                vo.setPhone(patrolTaskResult.getPhone());
            }
        }

        //巡更点信息
        if(vo.getCommunityId() != null && StringUtils.isNotEmpty(vo.getPointNumber())){
            Patrolpoint patrolpointCon = new Patrolpoint();
            patrolpointCon.setCommunityId(vo.getCommunityId());
            patrolpointCon.setPointNumber(vo.getPointNumber());
            Patrolpoint patrolpointResult = patrolpointService.getOne(Condition.getQueryWrapper(patrolpointCon));
            if(patrolpointResult != null){
                vo.setLongitude(patrolpointResult.getDqjd());
                vo.setLatitude(patrolpointResult.getDqwd());
            }
        }
        return vo;
    }

    /**
     * 查询巡更记录详情
     * @param id
     * @return
     */
    @Override
    public EsPatrollogVO findById(String id) {
        Map<String,Object> map = new HashMap<>(1);
        map.put("uuid",id);
        ESDatas<ESPatrollog> esds = eSPatrollogDao.getPatrollogById(map);
        if(CollUtil.isNotEmpty(esds.getDatas())){
            return  convert(esds.getDatas().get(0));
        }
        return EsPatrollogVO.builder().build();
    }
}
