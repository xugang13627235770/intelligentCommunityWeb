package cn.citms.icw.service.impl;

import cn.citms.icw.Utils.PageResult;
import cn.citms.icw.dao.EsAlarmDao;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.EsAlarm;
import cn.citms.icw.service.IAlarmService;
import cn.citms.icw.service.IBaseService;
import cn.citms.icw.vo.*;
import cn.citms.mbd.basicdatacache.service.IDictionaryCacheService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.date.DatePattern;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springblade.common.enums.DictEnum;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
public class AlarmServiceImpl implements IAlarmService {

    private final EsAlarmDao esAlarmDao;
    private final IDictionaryCacheService dictionaryCacheService;
    private final IBaseService baseService;

    @Autowired
    public AlarmServiceImpl(EsAlarmDao esAlarmDao,
                            IDictionaryCacheService dictionaryCacheService,
                            IBaseService baseService) {
        this.esAlarmDao = esAlarmDao;
        this.dictionaryCacheService = dictionaryCacheService;
        this.baseService = baseService;
    }

    @Override
    public IPage<EsAlarmVO> queryAlarm(EsAlarmSearchVO vo) {
        pageConvert(vo);
        List<EsAlarmVO> ls = new CopyOnWriteArrayList<>();
        Map<String, Object> map = new HashMap<>(BeanUtil.toMap(vo));
        if (ObjectUtil.isNotNull(vo.getStartTime()) && ObjectUtil.isNotNull(vo.getEndTime())) {
            map.put("startTime", DateUtil.format(DateUtil.date(vo.getStartTime().getTime()), DatePattern.NORM_DATETIME_PATTERN));
            map.put("endTime", DateUtil.format(DateUtil.date(vo.getEndTime().getTime()), DatePattern.NORM_DATETIME_PATTERN));
        }
        ESDatas<EsAlarm> esds = esAlarmDao.getAlarmInfos(map);
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
    private EsAlarmVO convert(EsAlarm ea) {
        EsAlarmVO vo = EsAlarmVO.builder().captureTimeFormart(ea.getCaptureTime())
                .alarmTypeName(dictionaryCacheService.getDictionaryNameByKindNo(
                        DictEnum.YJLX.getIndex(), ea.getAlarmType())
                )
                .jwd(StrUtil.format("{},{}", ea.getLongitude(), ea.getLatitude())).build();
        BeanUtil.copy(ea, vo);
        Community c = baseService.getCommunityInfo(ea.getExt());
        if (ObjectUtil.isNotNull(c)) {
            vo.setSqmc(c.getXqmc());
            vo.setCommunityId(c.getId());
        }
        return vo;
    }

    @Override
    public R<EsAlarmVO> findById(String id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("uuid", id);
        ESDatas<EsAlarm> esds = esAlarmDao.findById(map);
        if (CollUtil.isNotEmpty(esds.getDatas())) {
            return R.data(convert(esds.getDatas().get(0)));
        }
        return R.data(EsAlarmVO.builder().build());
    }


    private void pageConvert(EsAlarmSearchVO vo) {
        if (null == vo.getPageindex()|| null == vo.getPagesize()) {
            vo.setPageindex(0);
             vo.setPagesize(10);
        }
        if (0 != vo.getPageindex()) {
            vo.setPageindex((vo.getPageindex() - 1) * vo.getPagesize());
        }
    }


}
