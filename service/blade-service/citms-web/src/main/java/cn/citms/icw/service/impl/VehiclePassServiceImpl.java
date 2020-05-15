package cn.citms.icw.service.impl;

import cn.citms.icw.Utils.PageResult;
import cn.citms.icw.dao.EsPersonDao;
import cn.citms.icw.dao.EsVehicleDao;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.EsPerson;
import cn.citms.icw.entity.EsVehicle;
import cn.citms.icw.entity.Vehicle;
import cn.citms.icw.service.IBaseService;
import cn.citms.icw.service.IVehiclePassService;
import cn.citms.icw.service.IVehicleService;
import cn.citms.icw.vo.*;
import cn.citms.mbd.basicdatacache.service.IDictionaryCacheService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.date.DatePattern;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
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
 *     车辆通行记录 服务实现类
 * </pre>
 *
 * @author liuyuyang
 */
@Service
public class VehiclePassServiceImpl implements IVehiclePassService {
    private final IDictionaryCacheService dictionaryCacheService;
    private final EsVehicleDao esVehicleDao;
    private final IVehicleService vehicleService;
    private final IBaseService baseService;

    @Autowired
    public VehiclePassServiceImpl(EsVehicleDao esVehicleDao,
                                  IDictionaryCacheService dictionaryCacheService,
                                  IVehicleService vehicleService,
                                  IBaseService baseService) {
        this.esVehicleDao = esVehicleDao;
        this.dictionaryCacheService = dictionaryCacheService;
        this.vehicleService = vehicleService;
        this.baseService=baseService;
    }


    @Override
    public IPage<EsVehicleVO> queryVehicle(VehicleSearchVO vo) {
        pageConvert(vo);
        List<EsVehicleVO> ls = new CopyOnWriteArrayList<>();
        Map<String, Object> map = new HashMap<>(BeanUtil.toMap(vo));
        //TODO:人员数据 要根据人名去数据库搜索
        if (ObjectUtil.isNotNull(vo.getStartTime()) && ObjectUtil.isNotNull(vo.getEndTime())) {
            map.put("startTime", DateUtil.format(DateUtil.date(vo.getStartTime().getTime()), DatePattern.NORM_DATETIME_PATTERN));
            map.put("endTime", DateUtil.format(DateUtil.date(vo.getEndTime().getTime()), DatePattern.NORM_DATETIME_PATTERN));
        }
        if (!StrUtil.equalsIgnoreCase(vo.getVehicleSource(), "0")) {
            Map<String, List<VehicleVO>> vmap = vehicleService.findAll().parallelStream().collect(Collectors.groupingBy(VehicleVO::getSqbm));
            List<String> sqLists = new CopyOnWriteArrayList<>();
            List<String> plateNos = new CopyOnWriteArrayList<>();
            vmap.keySet().parallelStream().forEach(v -> {
                List<VehicleVO> vs = vmap.get(v);
                if (CollUtil.isNotEmpty(vs)) {
                    sqLists.add(v);
                    plateNos.addAll(vs.parallelStream().map(Vehicle::getPlateNo).collect(Collectors.toList()));
                }
            });
            if (StrUtil.equalsIgnoreCase(vo.getVehicleSource(), "登记车辆")) {
                if (CollUtil.isNotEmpty(sqLists)) {
                    map.put("xqbh", sqLists);
                }
                if (CollUtil.isNotEmpty(plateNos)) {
                    map.put("platNos", plateNos);
                }
            }
            if (StrUtil.equalsIgnoreCase(vo.getVehicleSource(), "外来车辆")) {
                if (CollUtil.isNotEmpty(plateNos)) {
                    map.put("platNos", plateNos);
                }
            }
        }

        ESDatas<EsVehicle> esds = esVehicleDao.queryVehicle(map);
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
    private EsVehicleVO convert(EsVehicle ep) {
        EsVehicleVO vo = EsVehicleVO.builder().build();
        BeanUtil.copyProperties(ep, vo);
        vo.setPlateColorCn(dictionaryCacheService.getDictionaryNameByKindNo(DictEnum.HPYS.getIndex(), vo.getPlate_color()));
        vo.setPlateTypeCn(dictionaryCacheService.getDictionaryNameByKindNo(DictEnum.HPZL.getIndex(), vo.getPlate_type()));
        vo.setVehicleColorCn(dictionaryCacheService.getDictionaryNameByKindNo(DictEnum.CSYS.getIndex(), vo.getVehicle_color()));
        vo.setVehicleTypeCn(dictionaryCacheService.getDictionaryNameByKindNo(DictEnum.CLLX.getIndex(), vo.getVehicle_type()));
        //TODO:设备转换
        Community c= baseService.getCommunityInfo(vo.getExt());
        if(ObjectUtil.isNotNull(c)){
            vo.setSqmc(c.getXqmc());
        }
        return vo;
    }

    private void pageConvert(VehicleSearchVO vo) {
        if (null == vo.getPageIndex()|| null == vo.getPageSize()) {
            vo.setPageIndex(0);
            vo.setPageSize(10);
        }
        if (0 != vo.getPageIndex()) {
            vo.setPageIndex((vo.getPageIndex() - 1) * vo.getPageSize());
        }
    }
}
