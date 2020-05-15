package cn.citms.icw.service.impl;

import cn.citms.icw.Utils.PageResult;
import cn.citms.icw.dao.EsPersonDao;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.EsAlarm;
import cn.citms.icw.entity.EsPerson;
import cn.citms.icw.service.IBaseService;
import cn.citms.icw.service.IPersonPassService;
import cn.citms.icw.vo.EsAlarmVO;
import cn.citms.icw.vo.EsPersonVO;
import cn.citms.icw.vo.PersonSearchVO;
import cn.citms.icw.vo.VehicleSearchVO;
import cn.citms.mbd.basicdatacache.service.IDeviceCacheService;
import cn.citms.mbd.basicdatacache.service.IDictionaryCacheService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.date.DatePattern;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.commons.lang.BooleanUtils;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springblade.common.constant.CitmsAppConstant;
import org.springblade.common.enums.DictEnum;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * <pre>
 *     行人通行记录 服务实现类
 * </pre>
 * @author liuyuyang
 */
@Service
public class PersonPassServiceImpl implements IPersonPassService {
    private final IDictionaryCacheService dictionaryCacheService;
    private final EsPersonDao esPersonDao;
    private final IDeviceCacheService deviceCacheService;
    private final IBaseService baseService;
    @Autowired
    public PersonPassServiceImpl(EsPersonDao esPersonDao,
                                 IDictionaryCacheService dictionaryCacheService,
                                 IDeviceCacheService deviceCacheService,
                                 IBaseService baseService){
        this.esPersonDao=esPersonDao;
        this.dictionaryCacheService = dictionaryCacheService;
        this.deviceCacheService=deviceCacheService;
        this.baseService=baseService;
    }

    @Override
    public IPage<EsPersonVO> queryPerson(PersonSearchVO vo) {
        pageConvert(vo);
        List<EsPersonVO> ls = new CopyOnWriteArrayList<>();
        Map<String, Object> map = new HashMap<>(BeanUtil.toMap(vo));
        if (ObjectUtil.isNotNull(vo.getStartTime()) && ObjectUtil.isNotNull(vo.getEndTime())) {
            map.put("startTime", DateUtil.format(DateUtil.date(vo.getStartTime().getTime()), DatePattern.NORM_DATETIME_PATTERN));
            map.put("endTime", DateUtil.format(DateUtil.date(vo.getEndTime().getTime()), DatePattern.NORM_DATETIME_PATTERN));
        }
        map.put("isValidate",StrUtil.isNotEmpty(vo.getIsValidate())
                &&!StrUtil.equalsIgnoreCase(CitmsAppConstant.ZERO,vo.getIsValidate())
                ?vo.getIsValidate():null);

        ESDatas<EsPerson> esds = esPersonDao.queryPerson(map);
        if (CollUtil.isNotEmpty(esds.getDatas())) {
            ls = esds.getDatas().parallelStream().map(
                    this::convert
            ).collect(Collectors.toList());
        }
        return new PageResult<>(vo.getPageIndex(), vo.getPageSize(), esds.getTotalSize(), ls);
    }

    /**
     * es返回给前端字段属性转换
     */
    private EsPersonVO convert(EsPerson ep) {
        EsPersonVO vo = EsPersonVO.builder().build();
        BeanUtil.copyProperties(ep,vo );
        String sex =dictionaryCacheService.getDictionaryNameByKindNo(DictEnum.XB.getIndex(),vo.getSex());
        vo.setSex(sex);
        //TODO:这里应该根据智慧社区device关联表去查询设备信息。
//        String deviceName =deviceCacheService.getDeviceNameByNo(vo.getDeviceNo());
        vo.setDeviceName(vo.getDeviceNo());
        Community c= baseService.getCommunityInfo(vo.getExt());
        if(ObjectUtil.isNotNull(c)){
            vo.setSqmc(c.getXqmc());
        }
        vo.setStationName(vo.isValidate()? "已登记" : "未登记");
        return vo;
    }

    private void pageConvert(PersonSearchVO vo) {
        if (null == vo.getPageIndex()|| null == vo.getPageSize()) {
            vo.setPageIndex(0);
            vo.setPageSize(10);
        }
        if (0 != vo.getPageIndex()) {
            vo.setPageIndex((vo.getPageIndex() - 1) * vo.getPageSize());
        }
    }

}
