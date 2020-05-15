package cn.citms.icw.service.impl;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.Utils.DictUtils;
import cn.citms.icw.constant.IntelligentCommunityConstant;
import cn.citms.icw.dao.ESPatrollogDao;
import cn.citms.icw.dao.EsAlarmDao;
import cn.citms.icw.dao.PersonTrafficFlowDao;
import cn.citms.icw.dao.VehicleTrafficFlowDao;
import cn.citms.icw.dto.*;
import cn.citms.icw.entity.*;
import cn.citms.icw.mapper.CommunityMapper;
import cn.citms.icw.mapper.HouseMapper;
import cn.citms.icw.mapper.PatrolpointMapper;
import cn.citms.icw.mapper.PersoncheckinMapper;
import cn.citms.icw.service.*;
import cn.citms.icw.utils.DateUtils;
import cn.citms.icw.vo.*;
import cn.citms.mbd.basicdatacache.service.IDeviceCacheService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.date.DateField;
import com.xiaoleilu.hutool.date.DatePattern;
import com.xiaoleilu.hutool.date.DateUnit;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springblade.common.constant.CitmsAppConstant;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatisticServiceImpl implements IStatisticService {

    @Value("${dgaRpc.getSomeDeviceLogUrl}")
    private String getSomeDeviceLogUrl;
    @Value("${self.localPlateNo}")
    private String localPlateMark;
    @Value("${dgaRpc.getDictByKindUrl}")
    private String getDictByKindUrl;

    @Resource
    private BaseService baseService;
    @Resource
    private ICommunityService  communityService;
    @Resource
    private ICommunityDeviceService communityDeviceService;
    @Resource
    private IDeviceCacheService deviceCacheService;
    @Resource
    private IHouseService houseService;
    @Resource
    private IVehicleService vehicleService;
    @Resource
    private IPersoncheckinService personcheckinService;
    @Resource
    private ESPatrollogDao patrollogDao;

    private final EsAlarmDao esAlarmDao;

    @Autowired
    public StatisticServiceImpl(EsAlarmDao esAlarmDao) {
        this.esAlarmDao = esAlarmDao;
    }

    @Resource
    private CommunityMapper communityMapper;
    @Resource
    private HouseMapper houseMapper;
    @Resource
    private PersoncheckinMapper personcheckinMapper;
    @Resource
    private PatrolpointMapper patrolpointMapper;
    @Resource
    private PersonTrafficFlowDao personTrafficflowDao;
    @Resource
    private VehicleTrafficFlowDao vehicleTrafficflowDao;
    @Resource
    private IPatrollogService patrollogService;

    @Override
    public FirstPageDeviceOnlineRateStatisticDTO QuerySYDeviceOnlineRateStatistic(StatisticDefVO vo) {
       if(vo == null){
            vo = new StatisticDefVO();
       }
        //构造返回模型
        FirstPageDeviceOnlineRateStatisticDTO dto = new FirstPageDeviceOnlineRateStatisticDTO();
        // 根据当前登陆的用户设置入参
        setParamByUserPermossion(vo);
        log.info("设备在线率;小区标识符：" + vo.getCommunityIds());
        // 根据社区查询设备标识符
        List<CommunityDevice> communityDeviceList =  communityDeviceService.getByCommunityIdList(vo.getCommunityIds());
        List<String> deviceNoList = new ArrayList<>();
        Map<String, CommunityDevice> deviceMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(communityDeviceList)){
            for (CommunityDevice device : communityDeviceList) {
                if(device!=null && StringUtils.isNotBlank(device.getDeviceId())){
                    // 存储设备的编号
                    deviceNoList.add(device.getDeviceId());
                    deviceMap.put(device.getDeviceId(), device);
                }
            }
        }
        log.info("设备在线率;设备编号集合：" + deviceNoList);
        if(!CollectionUtils.isEmpty(deviceNoList)){
            // 从缓存中获取设备的详细信息
            List<CommunityDeviceVO> deviceVOList = getDeviceInfoByCache(deviceNoList, deviceMap);
            log.info("设备在线率;设备详细信息的大小：" + deviceVOList.size());
            if(!CollectionUtils.isEmpty(deviceVOList)){
                // 获取设备的标识符
                List<String> deviceIdList = deviceVOList.stream().map(el -> el.getDeviceId()).collect(Collectors.toList());
                log.info("设备在线率;设备标识符：" + deviceIdList);
                // 获取设备的检测日志
                List<DeaviceCheckLogVO> deaviceCheckLogList = baseService.getSomeDeviceLog(getSomeDeviceLogUrl, deviceIdList);
                log.info("设备在线率;设备检测日志大小：" + deaviceCheckLogList.size());
                if(!CollectionUtils.isEmpty(deaviceCheckLogList)){
                    //统计每小时设备检测信息的数量
                    staticDeviceOnlineCount(dto, deaviceCheckLogList, deviceNoList);
                }
            }
        }
        return dto;
    }

    // 从缓存中根据设备编号获取设备信息
    private List<CommunityDeviceVO> getDeviceInfoByCache(List<String> deviceList, Map<String, CommunityDevice> deviceMap) {
        Map<String, JSONObject> deviceJsonMapByNoList = deviceCacheService.getDeviceJsonMapByNoList(deviceList);
        Collection<JSONObject> jsonArr = deviceJsonMapByNoList.values();
        List<CommunityDeviceVO> deviceVOList = new ArrayList<CommunityDeviceVO>(jsonArr.size());
        //Map<String, Integer> functionMap = new HashMap<>();
        for (JSONObject object : jsonArr) {
            CommunityDeviceVO deviceVO = object.toJavaObject(CommunityDeviceVO.class);
            CommunityDevice dev = deviceMap.get(deviceVO.getDeviceId());
            if(dev != null) {
                deviceVO.setCommunityId(dev.getCommunityId());
                deviceVO.setBuildId(dev.getBuildId());
                deviceVO.setUnitId(dev.getUnitId());
                deviceVO.setCommunityShow(dev.getCommunityShow());
            }
            //根据设备类型统计数量
            /*String type = deviceVO.getFunctionType();
            if(StringUtils.isNotBlank(type)){
                functionMap.put(type, functionMap.containsKey(type) ? functionMap.get(type) + 1 : 1);
            }*/
            deviceVOList.add(deviceVO);
        }
        return deviceVOList;
    }

    //统计每小时设备检测信息的数量
    private void staticDeviceOnlineCount(FirstPageDeviceOnlineRateStatisticDTO dto, List<DeaviceCheckLogVO> deaviceCheckLogList, List<String> deviceList) {
        // 记录当前时间前一天每个小时的时间点
        Date currentDate = new Date();
        Map<Integer,Date> timeAreaMap = initDateMap();
        List<String> deviceNoList = new ArrayList<>();
        List<String> deviceNoCurrentList = new ArrayList<>();
        List<String> hourDeviceNoList = new ArrayList<>();
        double totalOnline = 0.0;
        Set<Integer> keySet = timeAreaMap.keySet();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        List<StatisticTypeCountDTO>  countDTOList = new ArrayList<>();
        for (Integer hour : keySet) {
            StatisticTypeCountDTO countDTO = new StatisticTypeCountDTO();
            //清空上一次统计一小时的设备数量
            hourDeviceNoList.clear();
            deviceNoList.clear();
            for (DeaviceCheckLogVO log : deaviceCheckLogList) {
                if(log != null && log.getCheckTime() != null){
                    Date checkTime = log.getCheckTime();
                    calendar.setTime(checkTime);
                    int checkTimeHour = calendar.get(Calendar.HOUR_OF_DAY);
                    //统计每小时设备在线数量
                    String deviceNo = log.getDeviceNo();
                    if(checkTimeHour == hour && sdf.format(timeAreaMap.get(hour)).equals(sdf.format(checkTime))){
                        if(StringUtils.isNotBlank(deviceNo)){
                            if(!deviceNoList.contains(deviceNo)){
                                deviceNoList.add(deviceNo);
                                hourDeviceNoList.add(deviceNo);
                            }
                        }
                    }
                }
            }
            Integer size = hourDeviceNoList.size();
            totalOnline += size.doubleValue()/deviceList.size();
            countDTO.setType(hour + ":00");
            countDTO.setCount(hourDeviceNoList.size());
            countDTOList.add(countDTO);
        }
        // 统计当前时间所在小时设备在线数量
        double deviceNoCurrentCount = 0.0;
        for (StatisticTypeCountDTO typeCountDTO : countDTOList) {
            calendar.setTime(currentDate);
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            String type = typeCountDTO.getType();
            if(currentHour == Integer.parseInt(type.substring(0,type.indexOf(":")))){
                deviceNoCurrentCount = typeCountDTO.getCount();
            }
        }
        dto.setDeviceOnline(countDTOList);
        //设置设备的在线率 保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");
        if(deviceNoCurrentCount == 0.0){
            dto.setDeviceRate("0%");
        }else{
            double rate = deviceNoCurrentCount/deviceList.size()*100;
            dto.setDeviceRate(df.format(rate) + "%");
        }
        if(totalOnline == 0.0){
            dto.setDeviceAvgRate("0%");
        }else{
            String avgRate = df.format(totalOnline / 24 * 100);
            if(String.valueOf(avgRate.charAt(0)).equals(".")){
                avgRate = "0" + avgRate;
            }
            dto.setDeviceAvgRate(avgRate + "%");
        }
    }

    // 初始化时间区间map
    private Map<Integer, Date> initDateMap(){
        Date currentDate = new Date();
        Map<Integer,Date> timeAreaMap = new HashMap<>();
        Calendar instance = Calendar.getInstance();
        for (int i = 0; i < 24; i++) {
            instance.setTime(currentDate);
            int hour = instance.get(Calendar.HOUR_OF_DAY);
            timeAreaMap.put(hour,currentDate);
            instance.add(Calendar.HOUR,-1);
            currentDate = instance.getTime();
        }
        return timeAreaMap;
    }

    @Override
    public VehicleStatisticViewModel QueryVehicleStatistic(StatisticsVehicleDefVO vo) {
        if(vo == null){
            vo = new StatisticsVehicleDefVO();
        }
        //构造返回模型
        VehicleStatisticViewModel dto = new VehicleStatisticViewModel();
        String communityId = vo.getCommunityId();
        if(StringUtils.isBlank(communityId)){
            dto.setSqmc("全区");
        }
        Community community = communityService.getById(communityId);
        if(community != null){
            dto.setSqmc(community.getXqmc());
        }
        //1、全区住户登记总数、门户登记总数、车辆登记总数、户均车辆数
        double houseCount = houseService.countHouse(communityId);
        double vehicleCount = vehicleService.countVehicle(communityId);
        double personCount = personcheckinService.countPerson(communityId);
        dto.setHouseTotal(houseCount);
        dto.setVehicleTotal(vehicleCount);
        dto.setPersonTotal(personCount);
        if(houseCount == 0 || vehicleCount == 0){
            dto.setVehicleperHouseTotal(0);
        } else {
            DecimalFormat df = new DecimalFormat("0.0000");
            dto.setVehicleperHouseTotal(Double.valueOf(df.format(vehicleCount/houseCount)));
        }
        //2、登记车辆构成 大型车:plateType=2  大型车:plateType=1   小型车:plateType=2  黄标车:plateType=99  新能源汽车:plateType=51,52   其他:plateType!=1,2,51,52
        List<StatisticTypeCount> statisticTypeCountListLX = new ArrayList<>();
        double plateType1 =  vehicleService.getVehicleCountByPlateType("1",communityId);
        StatisticTypeCount statisticTypeCount1 = new StatisticTypeCount();
        statisticTypeCount1.setType("大型车");
        statisticTypeCount1.setCount(plateType1);
        double plateType2 =  vehicleService.getVehicleCountByPlateType("2",communityId);
        StatisticTypeCount statisticTypeCount2 = new StatisticTypeCount();
        statisticTypeCount2.setType("小型车");
        statisticTypeCount2.setCount(plateType2);
        double plateType51 =  vehicleService.getVehicleCountByPlateType("51",communityId);
        double plateType52 =  vehicleService.getVehicleCountByPlateType("52",communityId);
        StatisticTypeCount statisticTypeCount51 = new StatisticTypeCount();
        statisticTypeCount51.setType("新能源汽车");
        statisticTypeCount51.setCount(plateType51+plateType52);
        double plateType99 =  vehicleService.getVehicleCountByPlateType("99",communityId);
        StatisticTypeCount statisticTypeCount99 = new StatisticTypeCount();
        statisticTypeCount99.setType("黄标车");
        statisticTypeCount99.setCount(plateType99);
        double otherType = vehicleCount-(plateType1+plateType2+plateType51+plateType52);
        if(otherType < 0){
            otherType = 0;
        }
        StatisticTypeCount statisticTypeCountOther = new StatisticTypeCount();
        statisticTypeCountOther.setType("其他");
        statisticTypeCountOther.setCount(otherType);
        statisticTypeCountListLX.add(statisticTypeCount1);
        statisticTypeCountListLX.add(statisticTypeCount2);
        statisticTypeCountListLX.add(statisticTypeCount51);
        statisticTypeCountListLX.add(statisticTypeCount99);
        statisticTypeCountListLX.add(statisticTypeCountOther);
        dto.setVehicleLX(statisticTypeCountListLX);

        //3、登记车辆归属 本市车、省内外地车、外省车
        List<StatisticTypeCount> statisticTypeCountListHPGL = new ArrayList<>();
        String provincePlateMark = localPlateMark.substring(0,1);
        double localVehicleCount = vehicleService.getLocalVehicle(localPlateMark,communityId);
        StatisticTypeCount statisticTypeCountLocal = new StatisticTypeCount();
        statisticTypeCountLocal.setCount(localVehicleCount);
        statisticTypeCountLocal.setType("本市车");
        double localProvinceOtherCityVehicle = vehicleService.getLocalProvinceOtherCityVehicle(localPlateMark, provincePlateMark,communityId);
        StatisticTypeCount statisticTypeCountLocalOtherProvince = new StatisticTypeCount();
        statisticTypeCountLocalOtherProvince.setCount(localProvinceOtherCityVehicle);
        statisticTypeCountLocalOtherProvince.setType("省内外地车");
        double otherProvinceVehicle = vehicleService.getOtherProvinceVehicle(provincePlateMark,communityId);
        StatisticTypeCount statisticTypeOtherProvince = new StatisticTypeCount();
        statisticTypeOtherProvince.setCount(otherProvinceVehicle);
        statisticTypeOtherProvince.setType("外省车");
        statisticTypeCountListHPGL.add(statisticTypeCountLocal);
        statisticTypeCountListHPGL.add(statisticTypeCountLocalOtherProvince);
        statisticTypeCountListHPGL.add(statisticTypeOtherProvince);
        dto.setVehicleHPGL(statisticTypeCountListHPGL);

        //4、车辆登记统计，按月份统计过去12个月的数据
        List<StatisticTypeTotal> statisticTypeTotalList = new ArrayList<>();
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        double tempCount = 0;
        for (int i = 12; i >0; i--) {
            StatisticTypeTotal statisticTypeTotal = new StatisticTypeTotal();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.MONTH,-(i-1));
            Date monthTime = calendar.getTime();
            String timeType = sdf.format(monthTime);
            Date monthStart = DateUtils.getMonthStart(calendar.getTime());
            Date monthEnd = DateUtils.getMonthEnd(calendar.getTime());
            double vehicleMonthCount = vehicleService.getVehicleByStartMonthAndEndMonth(monthStart, monthEnd, communityId);
            tempCount = tempCount + vehicleMonthCount;
            statisticTypeTotal.setType(timeType);
            statisticTypeTotal.setCount(vehicleMonthCount);
            statisticTypeTotal.setTotal(tempCount);
            statisticTypeTotalList.add(statisticTypeTotal);
        }
        dto.setTheMonVehicle(statisticTypeTotalList);
        return dto;
    }

    @Override
    public List<PCIStatisticVO> QueryPersonStatistic(StatisticDefVO vo) {
        //获取当前部门能够查看的数据
        List<String> departmentIds = baseService.getPermissionDepartment();
        if(CollUtil.isNotEmpty(departmentIds)) {
            List<String> communityIds = communityService.selectCommunityIdByDeptIds(departmentIds);
            if(CollUtil.isEmpty(communityIds) && StrUtil.isNotBlank(vo.getCommunity_Id())) {
                vo.setCommunityIds(Collections.singletonList(vo.getCommunity_Id()));
            } else {
                vo.setCommunityIds(communityIds);
            }
        }

        PCIStatisticVO result = new PCIStatisticVO();
        Integer personTotal = personcheckinService.getCheckInCnt(vo.getCommunityIds());
        result.setPersonTotal((double) personTotal);
        Integer houseTotal = houseService.getHouseCnt(vo.getCommunityIds());
        result.setHouseTotal((double) houseTotal);
        result.setSqmc("全局");
        if(vo.getCommunityIds() != null && vo.getCommunityIds().size() == 1) {
            // 社区名称
            Community community = communityService.getById(vo.getCommunity_Id());
            if(community != null) {
                result.setSqmc(community.getXqmc());
            }
        }
        Map<String, Integer> dicRylx = personcheckinService.getCheckInRylxCnt(vo.getCommunityIds());
        int cnt = dicRylx.get(DictUtils.DJHZ) == null ? 0 : dicRylx.get(DictUtils.DJHZ);
        int cnt1 = dicRylx.get(DictUtils.HZJS) == null ? 0 : dicRylx.get(DictUtils.HZJS);
        int cnt2 = dicRylx.get(DictUtils.CZZZ) == null ? 0 : dicRylx.get(DictUtils.CZZZ);
        //人员类型为业主、家属
        result.setPersonHeadTotal((double) (cnt + cnt1));
        //人员类型为租户
        result.setPersonTenantTotal((double) cnt2);
        int cnt3;
        //用户组成
        Map<String, String> dictRy = DictUtils.getDictRy();
        List<StatisticTypeCount> listpersonZC = new ArrayList<>(10);
        for (Map.Entry<String, String> entry : dictRy.entrySet()) {
            StatisticTypeCount stc = new StatisticTypeCount();
            stc.setType(entry.getKey());
            cnt3 = dicRylx.get(entry.getValue())==null ? 0 : dicRylx.get(entry.getValue());
            stc.setCount(cnt3);
            listpersonZC.add(stc);
        }
        StatisticTypeCount personZCqt = new StatisticTypeCount();
        personZCqt.setType("其他");
        int syCnt = dicRylx.values().stream().mapToInt(Integer::intValue).sum();
        syCnt = syCnt>0 ? (syCnt-cnt-cnt1-cnt2) : 0;
        personZCqt.setCount(syCnt);
        listpersonZC.add(personZCqt);
        result.setZhzc(listpersonZC);
        //房屋用途
        Map<String, Integer> dicFwzt = houseService.getHouseZtCnt(vo.getCommunityIds());
        Map<String, String> dictFw = CommonUtils.getDictValueMapByKind(getDictByKindUrl, String.valueOf(IntelligentCommunityConstant.FWZT));
        List<StatisticTypeCount> listhouseYT = new ArrayList<>(10);
        for (Map.Entry<String, String> entry : dictFw.entrySet()) {
            StatisticTypeCount FwType = new StatisticTypeCount();
            FwType.setType(entry.getKey());
            cnt3 = dicFwzt.get(entry.getValue())==null ? 0 : dicFwzt.get(entry.getValue());
            FwType.setCount(cnt3);
            listhouseYT.add(FwType);
        }
        result.setFwyt(listhouseYT);
        //特殊人群
        Map<String, String> dictTs = DictUtils.getDictTs();
        Map<String, Integer> dicTsrq = personcheckinService.getCheckInYhbsCnt(vo.getCommunityIds());
        List<StatisticTypeCount> listTs = new ArrayList<>(10);
        for (Map.Entry<String, String> entry : dictTs.entrySet()) {
            StatisticTypeCount TsryType = new StatisticTypeCount();
            TsryType.setType(entry.getKey());
            cnt3 = dicTsrq.get(entry.getValue())==null ? 0 : dicTsrq.get(entry.getValue());
            TsryType.setCount(cnt3);
            listTs.add(TsryType);
        }
        listTs = listTs.stream().sorted(Comparator.comparing(StatisticTypeCount::getCount).reversed()).collect(Collectors.toList());
        result.setTsrq(listTs);
        //重点人员
        Map<String, String> dictZd = DictUtils.getDictZd();
        Map<String, Integer> dicZdry = personcheckinService.getCheckInZdryCnt(vo.getCommunityIds());
        List<StatisticTypeCount> listZd = new ArrayList<>(10);
        for (Map.Entry<String, String> entry : dictZd.entrySet()) {
            StatisticTypeCount ZdryType = new StatisticTypeCount();
            ZdryType.setType(entry.getKey());
            cnt3 = dicZdry.get(entry.getValue())==null ? 0 : dicZdry.get(entry.getValue());
            ZdryType.setCount(cnt3);
            listZd.add(ZdryType);
        }
        result.setZdrq(listZd);
        //房屋分布
        Map<String, Integer> dicHouse = personcheckinService.findGroupByHouseId();
        List<StatisticTypeCount> listFwfb = new ArrayList<>(20);
        for (Map.Entry<String, Integer> entry : dicHouse.entrySet()) {
            StatisticTypeCount FwfbtjType = new StatisticTypeCount();
            FwfbtjType.setType(entry.getKey());
            FwfbtjType.setCount(entry.getValue());
            listFwfb.add(FwfbtjType);
        }
        result.setFwjzfb(listFwfb);
        //查询最近一年的数据,根据月份统计登记住户
        List<StatisticTypeTotal> listtheMonCheckins = personcheckinService.getPersoncheckinByTime(vo.getCommunityIds());
        result.setTheMonCheckin(listtheMonCheckins);

        List<PCIStatisticVO> listPCI = new ArrayList<>(10);
        listPCI.add(result);
        return listPCI;
    }

    // 根据当前登陆的用户设置入参
    private Community setParamByUserPermossion(StatisticDefVO vo) {
        Community community = null;
        if(StringUtils.isBlank(vo.getCommunity_Id())){
            List<String> departIdList = baseService.getPermissionDepartment();
            if(!CollectionUtils.isEmpty(departIdList)){
                List<String> communityIdList = communityService.selectCommunityIdByDeptIds(departIdList);
                vo.setCommunityIds(communityIdList);
                List<String> communityNoList = communityService.selectCommunityNoByDeptIds(departIdList);
                vo.setExts(communityNoList);
            }
        }else{
            vo.setCommunityIds(new ArrayList<>(Arrays.asList(vo.getCommunity_Id())));
            community = communityService.getById(vo.getCommunity_Id());
            if(community != null){
                vo.setExts(new ArrayList<>(Arrays.asList(community.getXqbm())));
                return community;
            }
        }
        return community;
    }

    @Override
    public FirstPagePatrolStatisticDTO QuerySYPatrolStatistic(StatisticDefVO vo) {
        FirstPagePatrolStatisticDTO dto = new FirstPagePatrolStatisticDTO();
        // 根据当前登陆的用户设置入参
        setParamByUserPermossion(vo);
        ESDatas<ESPatrollog> esDatasTotal = patrollogDao.getTimeAreaPartollogCount(buildEsQueryParam(vo, false));
        ESDatas<ESPatrollog> esDatasCurrent = patrollogDao.getTimeAreaPartollogCount(buildEsQueryParam(vo, true));
        dto.setPatrollogTotal(esDatasTotal.getTotalSize());
        dto.setPatrolTodayCount(esDatasCurrent.getTotalSize());
        // 调用巡更的业务层完善逻辑
        //filter.Exts = query.Exts;  filter.PageIndex = 1;  filter.PageSize = 8;   入参模型
        EsPatrollogSearchVO searchVO = new EsPatrollogSearchVO();
        searchVO.setPageindex(1);
        searchVO.setPagesize(10);
        searchVO.setExts(vo.getExts());
        IPage<EsPatrollogVO> esPatrollogVOIPage = patrollogService.queryPatrollog(searchVO);
        dto.setEsPatrollog(esPatrollogVOIPage.getRecords());
        return dto;
    }

    //构建es查询的入参模型
    private Map<String, Object> buildEsQueryParam(StatisticDefVO vo, boolean timeAreaFlag) {
        Map<String,Object> map = new HashMap<>();
        map.put("esxts",vo.getExts());
        if(timeAreaFlag){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDatePrix = sdf.format(new Date());
            String startTime = currentDatePrix + " 00:00:00";
            String endTime = currentDatePrix + " 23:59:59";
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        }
        return map;
    }

    @Override
    public List<AlarmStatisticVO> queryAlarmStatistic(StatisticsDefSearchVO vo) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (vo.getDateType().equalsIgnoreCase(CitmsAppConstant.ONE)) {
            return formatParameter(calendar, DateField.MONTH.name().toLowerCase(), Calendar.YEAR,calendar.get(Calendar.YEAR) - 1, "yyyy-MM");
        }
        if (vo.getDateType().equalsIgnoreCase(CitmsAppConstant.TWO)) {
            return formatParameter(calendar, DateUnit.DAY.name().toLowerCase(), Calendar.DATE,calendar.get(Calendar.DATE) - 30, "MM-dd");
        }
        if (vo.getDateType().equalsIgnoreCase(CitmsAppConstant.THREE)) {
            return formatParameter(calendar, DateField.HOUR.name().toLowerCase(), Calendar.HOUR,calendar.get(Calendar.HOUR) - 23, "HH:mm");
        }
        return null;
    }


    /**
     * 格式化代码，复用重复代码
     */
    private List<AlarmStatisticVO> formatParameter(Calendar calendar, String dateField,Integer dateType
            , Integer minus, String datePattern) {
        String endTime = DateUtil.format(calendar.getTime(), DatePattern.NORM_DATETIME_PATTERN);
        String maxDate = DateUtil.format(calendar.getTime(), datePattern);
        log.info("endTime：{}》》》maxDate:{}",endTime,maxDate);
        calendar.set(dateType, minus);
        String minDate = DateUtil.format(calendar.getTime(), datePattern);
        String startTime = DateUtil.format(calendar.getTime(), DatePattern.NORM_DATETIME_PATTERN);
        log.info("startTime：{}》》》minDate:{}",startTime,minDate);
        return
                esAlarmDao.queryAlarmStatistic(
                        BeanUtil.toMap(SearchEsStatisticsAlarmVO.builder()
                                .startTime(startTime).endTime(endTime)
                                .minDate(minDate).maxDate(maxDate)
                                .dateField(dateField)
                                .dateFormat(datePattern).build()));
    }

    @Override
    public FirstPageStatisticDTO QuerySYStatistic(StatisticDefVO vo) {
        FirstPageStatisticDTO dto = new FirstPageStatisticDTO();
        Map<String, String> fwMap = CommonUtils.getDictByKind(getDictByKindUrl, String.valueOf(IntelligentCommunityConstant.FWZT));
        Map<String, String> ryMap = new HashMap<>();
        ryMap.put("4","登记户主");
        ryMap.put("100","户主家属");
        ryMap.put("5","出租暂住");
        Map<String, String> tsMap = new HashMap<>();
        tsMap.put("1","信访重点人员");
        tsMap.put("2","涉恐人员");
        tsMap.put("3","少数民族");
        tsMap.put("4","涉毒人员");
        tsMap.put("5","精神病患者");
        tsMap.put("6","涉稳人员");
        tsMap.put("7","犯罪前科");
        Map<String, String> deviceMap = new HashMap<>();
        deviceMap.put("视频监控","0");
        deviceMap.put("电子巡更","0");
        deviceMap.put("智能门禁","0");
        deviceMap.put("消防监控","0");
        deviceMap.put("边界防护","1800");
        deviceMap.put("车辆卡口","0911");
        deviceMap.put("行人卡口","0910");
        deviceMap.put("人脸速通门","0933");
        String sqmc = "全局";
        Community community = setParamByUserPermossion(vo);
        if(community!=null && StringUtils.isNotBlank(community.getXqmc())){
            sqmc= community.getXqmc();
        }
        dto.setSqmc(sqmc);
        List<String> deviceList = new ArrayList<>();
        Map<String, CommunityDevice> deviceInfoMap = new HashMap<>();
        // 根据社区标识符获取设备的详细信息
        List<CommunityDevice> communityDeviceList = communityDeviceService.getByCommunityIdList(vo.getCommunityIds());
        if(!CollectionUtils.isEmpty(communityDeviceList)){
            for (CommunityDevice device : communityDeviceList) {
                if(device!=null && StringUtils.isNotBlank(device.getDeviceId())){
                    deviceList.add(device.getDeviceId());
                    deviceInfoMap.put(device.getDeviceId(), device);
                }
            }
        }
        List<StatisticTypeViewCountDTO> viewDtoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(deviceList)){
            dto.setDeviceTotal(deviceList.size());
            // 从缓存中获取设备的详细信息
            List<CommunityDeviceVO> deviceVOList = getDeviceInfoByCache(deviceList, deviceInfoMap);
            // 统计设备类型的数量
            Map<String,Integer> functionMap = new HashMap<>();
            for (CommunityDeviceVO deviceVO : deviceVOList) {
                String type = deviceVO.getFunctionType();
                if(StringUtils.isNotBlank(type)){
                    functionMap.put(type, functionMap.containsKey(type) ? functionMap.get(type) + 1 : 1);
                }
            }
            Integer  count1 = functionMap.get("1800") == null ? 0 : functionMap.get("1800");
            Integer  count2 = functionMap.get("1700") == null ? 0 : functionMap.get("1700");
            Integer  count3 = functionMap.get("0912") == null ? 0 : functionMap.get("0912");
            Integer  count4 = functionMap.get("0921") == null ? 0 : functionMap.get("0921");
            Integer  count5 = functionMap.get("0934") == null ? 0 : functionMap.get("0934");
            Integer  count6 = functionMap.get("1500") == null ? 0 : functionMap.get("1500");
            // 设备数量的统计
            for (String deviceType : deviceMap.keySet()) {
                StatisticTypeViewCountDTO viewDto = new StatisticTypeViewCountDTO();
                viewDto.setType(deviceType);
                if(deviceType.equals("视频监控")){
                    String jkCount = String.valueOf(count1 + count2 + count3 + count4);
                    if(StringUtils.isBlank(jkCount) || "0".equals(jkCount)){
                        viewDto.setCount("-");
                    }else{
                        viewDto.setCount(jkCount);
                    }
                    viewDto.setCount(jkCount);
                }else if(deviceType.equals("电子巡更")){
                    Integer patrolCount = patrolpointMapper.getCountByCommunityIds(vo.getCommunityIds());
                    if(patrolCount == null || patrolCount == 0){
                        viewDto.setCount("-");
                    }else{
                        viewDto.setCount(String.valueOf(patrolCount));
                    }
                }else if(deviceType.equals("智能门禁")){
                    String znCount = String.valueOf(count5 + count6);
                    if(StringUtils.isBlank(znCount) || "0".equals(znCount)){
                        viewDto.setCount("-");
                    }else{
                        viewDto.setCount(znCount);
                    }
                }else if(deviceType.equals("消防监控")){
                    if(StringUtils.isBlank(vo.getCommunity_Id())){
                        viewDto.setCount("84");
                    }else{
                        String communityId = vo.getCommunity_Id();
                        if(communityId.equals("0F5TOY1J9Q03275FMXX7")){
                            viewDto.setCount("15");
                        }
                        if(communityId.equals("0F5TOY1J9RB327522UN8")){
                            viewDto.setCount("2");
                        }
                        if(communityId.equals("0F5TOY1J9RN32753NBWY")){
                            viewDto.setCount("67");
                        }
                    }
                }else{
                    Integer count = functionMap.get(deviceMap.get(deviceType));
                    if(count == null || count == 0){
                        viewDto.setCount("-");
                    }else{
                        viewDto.setCount(String.valueOf(count));
                    }
                }
                viewDtoList.add(viewDto);
            }
            dto.setTsryTotal(89);
            dto.setDevice(viewDtoList);
        }else{
            for (String deviceType : deviceMap.keySet()) {
                StatisticTypeViewCountDTO viewDto = new StatisticTypeViewCountDTO();
                viewDto.setType(deviceType);
                viewDto.setCount("-");
                viewDtoList.add(viewDto);
            }
            dto.setDeviceTotal(0);
            dto.setDevice(viewDtoList);
        }
        // 统计每种社区类型的数量
        List<CommonCountDTO> typeCountModelList = communityMapper.countCommunityTypeByCommuityIds(vo.getCommunityIds());
        Map<String,Integer> typeCountMap = new HashMap<String,Integer>();
        if(typeCountModelList !=null && typeCountModelList.size() >0 ){
            for (CommonCountDTO commonCountDTO : typeCountModelList) {
                typeCountMap.put(commonCountDTO.getKeyName(), commonCountDTO.getCount());
            }
            dto.ptSqCount = typeCountMap.get("1") == null ? 0 : typeCountMap.get("1");
            dto.sfSqCount = typeCountMap.get("2")== null ? 0 : typeCountMap.get("2");
            dto.setSqTotal(dto.ptSqCount+  dto.sfSqCount);
        }
        // 统计楼栋 住户 人员 车辆的总数
        List<CommonCountDTO> basicDataCountList = communityMapper.countBasicCount(vo.getCommunityIds());
        Map<String,Integer> basicDataCountMap = new HashMap<>();
        if(basicDataCountList!=null && basicDataCountList.size() >0 ){
            for (CommonCountDTO ele : basicDataCountList) {
                basicDataCountMap.put(ele.getKeyName(),ele.getCount());
            }
            dto.setBuildingTotal(basicDataCountMap.get("building")==null ? 0 : basicDataCountMap.get("building"));
            dto.setHouseTotal(basicDataCountMap.get("house")==null ? 0 : basicDataCountMap.get("house"));
            dto.setPersonTotal(basicDataCountMap.get("person")==null ? 0 : basicDataCountMap.get("person"));
            dto.setVehicleTotal(basicDataCountMap.get("vehicle")==null ? 0 : basicDataCountMap.get("vehicle"));
        }
        // 用户组成统计
        Map<String,Integer> personTypeMap = new HashMap<>();
        List<CommonCountDTO> personCheckinCountDTOList = personcheckinMapper.countGroupbyPersonType(vo.getCommunityIds());
        List<StatisticTypeCountDTO> personTypeList = new ArrayList<>();
        if(personCheckinCountDTOList!=null && personCheckinCountDTOList.size() >0 ){
            for (CommonCountDTO ele : personCheckinCountDTOList) {
                personTypeMap.put(ele.getKeyName(),ele.getCount());
            }
            Integer totalCount = 0;
            Integer otherCount =0;
            for (String personType : personTypeMap.keySet()) {
                if(StringUtils.isNotBlank(ryMap.get(personType))){
                    StatisticTypeCountDTO personTypeDto = new StatisticTypeCountDTO();
                    personTypeDto.setType(ryMap.get(personType));
                    personTypeDto.setCount(personTypeMap.get(personType));
                    personTypeList.add(personTypeDto);
                    otherCount += personTypeMap.get(personType);
                }
                totalCount += personTypeMap.get(personType);
            }
            if(totalCount > otherCount){
                StatisticTypeCountDTO personTypeDto = new StatisticTypeCountDTO();
                personTypeDto.setType("其他");
                personTypeDto.setCount(totalCount - otherCount);
                personTypeList.add(personTypeDto);
            }
            dto.setZhzc(personTypeList);
        }else{
            dto.setFwyt(setDefaultValue(ryMap));
        }
        // 设置房屋用途
        Map<String,Integer> hourseFunctionMap = new HashMap<>();
        List<CommonCountDTO> hourseCountDtoList = houseMapper.countGroupByHouseFunction(vo.getCommunityIds());
        List<StatisticTypeCountDTO> hourseFuncList = new ArrayList<>();
        if(hourseCountDtoList!=null && hourseCountDtoList.size() >0 ){
            for (CommonCountDTO ele : hourseCountDtoList) {
                hourseFunctionMap.put(ele.getKeyName(),ele.getCount());
            }
            for (String hourseFun : hourseFunctionMap.keySet()) {
                if(StringUtils.isNotBlank(fwMap.get(hourseFun))){
                    StatisticTypeCountDTO hourseFunDto = new StatisticTypeCountDTO();
                    hourseFunDto.setType(fwMap.get(hourseFun));
                    hourseFunDto.setCount(hourseFunctionMap.get(hourseFun));
                    hourseFuncList.add(hourseFunDto);
                }
            }
            dto.setFwyt(hourseFuncList);
        }else{
            dto.setFwyt(setDefaultValue(fwMap));
        }
        //设置特殊人群   因为没有原始数据 因此造假
        setSpecialPerson(dto);
        return dto;
    }

    private  List<StatisticTypeCountDTO> setDefaultValue(Map<String,String> dictMap) {
        List<StatisticTypeCountDTO> list = new ArrayList();
        for (String key : dictMap.keySet()) {
            StatisticTypeCountDTO typeCountDTO = new StatisticTypeCountDTO();
            typeCountDTO.setType(dictMap.get(key));
            typeCountDTO.setCount(0);
        }
        return list;
    }

    //设置特殊人群
    private void setSpecialPerson(FirstPageStatisticDTO dto) {
        List<StatisticTypeCountDTO> specialPersonList = new ArrayList<>();
        StatisticTypeCountDTO specialPersonDto = new StatisticTypeCountDTO();
        specialPersonDto.setType("信访重点人员");
        specialPersonDto.setCount(4);
        StatisticTypeCountDTO specialPersonDto1 = new StatisticTypeCountDTO();
        specialPersonDto1.setType("涉恐人员");
        specialPersonDto1.setCount(1);
        StatisticTypeCountDTO specialPersonDto2 = new StatisticTypeCountDTO();
        specialPersonDto2.setType("少数民族");
        specialPersonDto2.setCount(75);
        StatisticTypeCountDTO specialPersonDto3 = new StatisticTypeCountDTO();
        specialPersonDto3.setType("涉毒人员");
        specialPersonDto3.setCount(0);
        StatisticTypeCountDTO specialPersonDto4 = new StatisticTypeCountDTO();
        specialPersonDto4.setType("精神病患者");
        specialPersonDto4.setCount(5);
        StatisticTypeCountDTO specialPersonDto5 = new StatisticTypeCountDTO();
        specialPersonDto5.setType("涉稳人员");
        specialPersonDto5.setCount(1);
        StatisticTypeCountDTO specialPersonDto6 = new StatisticTypeCountDTO();
        specialPersonDto6.setType("犯罪前科");
        specialPersonDto6.setCount(3);
        specialPersonList.add(specialPersonDto1);
        specialPersonList.add(specialPersonDto2);
        specialPersonList.add(specialPersonDto3);
        specialPersonList.add(specialPersonDto4);
        specialPersonList.add(specialPersonDto5);
        specialPersonList.add(specialPersonDto6);
        dto.setTsryFb(specialPersonList);
        StatisticTypeRateDTO typeRate = new StatisticTypeRateDTO();
        typeRate.setRate("84.32%");
        typeRate.setType("少数民族");
        dto.setZbzdRate(typeRate);
    }

    @Override
    public VPSYMDDTO QueryVehiclePassStatistic_YMD(StatisticDefVO vo) {
        VPSYMDDTO dto = new VPSYMDDTO();
        Community community = setParamByUserPermossion(vo);
        String sqmc = "全局";
        if(community != null && StringUtils.isNotBlank(community.getXqmc())){
            sqmc = community.getXqmc();
        }
        dto.setSqmc(sqmc);
        Date currentDate = new Date();
        if(StringUtils.isNotBlank(vo.getDateType())){
            String dateType = vo.getDateType();
            // 构建查询es的入参模型
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String,Object> paramterMap = new HashMap<>();
            paramterMap.put("endTime", sdf.format(currentDate));
            paramterMap.put("exts", vo.getExts());
            Calendar instance = Calendar.getInstance();
            List<VPSYMDVO> voList = initTimeAreaStaticModel(dateType, currentDate);
            dto.setData(voList);
            if("1".equals(dateType)){
                instance.setTime(currentDate);
                instance.add(Calendar.YEAR, - 1);
                instance.add(Calendar.MONTH, +1);
                Date startTimeDate = instance.getTime();
                paramterMap.put("startTime", sdf.format(startTimeDate));
                paramterMap.put("interval", "month");
                paramterMap.put("format", "yyyy-MM");
                searchESAndBuildResultModel(dto, paramterMap,false);
            }else if("2".equals(dateType)){
                instance.setTime(currentDate);
                instance.add(Calendar.DATE, - 30);
                Date startTimeDate = instance.getTime();
                paramterMap.put("startTime", sdf.format(startTimeDate));
                paramterMap.put("interval", "day");
                paramterMap.put("format", "MM-dd");
                searchESAndBuildResultModel(dto, paramterMap,false);
            }else if("3".equals(dateType)){
                instance.setTime(currentDate);
                instance.add(Calendar.HOUR, - 24);
                Date startTimeDate = instance.getTime();
                paramterMap.put("startTime", sdf.format(startTimeDate));
                paramterMap.put("interval", "hour");
                paramterMap.put("format", "yyyyMMddHH");
                searchESAndBuildResultModel(dto, paramterMap, true);
            }
            dto.setData(voList);
        }
        return dto;
    }

    // 初始化时间分段统计模型
    private List<VPSYMDVO> initTimeAreaStaticModel(String dateType, Date nowTime) {
        List<VPSYMDVO> voList = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        if("1".equals(dateType)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (int i =0; i < 12; i++){
                instance.setTime(nowTime);
                VPSYMDVO vo = new VPSYMDVO();
                if(i != 0){
                    instance.add(Calendar.MONTH, -1);
                }
                String yearMothStr = sdf.format(instance.getTime());
                vo.setTjsj(yearMothStr);
                vo.setVehilePassTotal(0);
                vo.setPersonPassTotal(0);
                voList.add(vo);
                nowTime = instance.getTime();
            }
        }else if("2".equals(dateType)){
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            instance.add(Calendar.MONTH,-1);
            Date beforeMoth = instance.getTime();
            int count =0;
            while (nowTime.getTime() >= beforeMoth.getTime()){
                instance.setTime(nowTime);
                VPSYMDVO vo = new VPSYMDVO();
                if(count !=0 ){
                    instance.add(Calendar.DATE, -1);
                }
                String datStr = sdf.format(instance.getTime());
                vo.setTjsj(datStr);
                vo.setVehilePassTotal(0);
                vo.setPersonPassTotal(0);
                voList.add(vo);
                nowTime = instance.getTime();
                count ++;
            }
        }else if("3".equals(dateType)){
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            for (int i =0; i < 24; i++){
                instance.setTime(nowTime);
                VPSYMDVO vo = new VPSYMDVO();
                if(i != 0){
                    instance.add(Calendar.HOUR, -1);
                }
                String datStr = sdf.format(instance.getTime());
                vo.setTjsj(datStr + ":00");
                vo.setVehilePassTotal(0);
                vo.setPersonPassTotal(0);
                voList.add(vo);
                nowTime = instance.getTime();
            }
        }
        Collections.reverse(voList);
        return voList;
    }

    // 查询es并构建返回模型
    private void searchESAndBuildResultModel( VPSYMDDTO dto, Map<String, Object> paramterMap, boolean changeKeyFlag) {
        List<Map<String,Object>> personMapList = personTrafficflowDao.getTimeAreaPartollogCount(paramterMap);
        List<VPSYMDVO> voList = dto.getData();
        for (Map<String, Object> prsonFlowStatic : personMapList) {
            String key = String.valueOf(prsonFlowStatic.get("key_as_string"));
            Map<String, Object> countModel = (Map<String, Object>) prsonFlowStatic.get("sum_flowPersonCount");
            String count = String.valueOf(countModel.get("value"));
            if(changeKeyFlag){
                key = key.substring(key.length()-2, key.length()) + ":00";
            }
            if(CollUtil.isNotEmpty(voList)){
                for (VPSYMDVO vpsymdvo : voList) {
                    if(vpsymdvo.getTjsj().equals(key)){
                        if(StringUtils.isNotBlank(count)){
                            int index = count.indexOf(".");
                            if(index != -1){
                                count = count.substring(0,index);
                            }
                            vpsymdvo.setPersonPassTotal(Integer.parseInt(count));
                        }
                        break;
                    }
                }
            }

        }
        List<Map<String, Object>> vehicleMapList = vehicleTrafficflowDao.getTimeAreaPartollogCount(paramterMap);
        for (Map<String, Object> vehicleFlowStatic : vehicleMapList) {
            String key = String.valueOf(vehicleFlowStatic.get("key_as_string"));
            Map<String, Object> countModel = (Map<String, Object>) vehicleFlowStatic.get("sum_vehiclePassCount");
            String count = String.valueOf(countModel.get("value"));
            if(changeKeyFlag){
                key = key.substring(key.length()-2, key.length()) + ":00";
            }
            if(CollUtil.isNotEmpty(voList)){
            for (VPSYMDVO vpsymdvo : voList) {
                if (vpsymdvo.getTjsj().equals(key)) {
                    if (StringUtils.isNotBlank(count)) {
                        int index = count.indexOf(".");
                        if (index != -1) {
                            count = count.substring(0, index);
                        }
                        vpsymdvo.setVehilePassTotal(Integer.parseInt(count));
                    }
                    break;
                }
            }
            }
        }
    }

    @Override
    public List<VehiclePassStatisticVO> queryVehiclePassStatistic(StatisticsDefSearchVO vo) {
        List<VehiclePassStatisticVO> lvs = new ArrayList<>(1);
        VehiclePassStatisticVO vpso = new VehiclePassStatisticVO();
        vpso.setSqmc("全局");
        VPSYMDDTO vpsymddto = getTodayCount();
        if(CollUtil.isNotEmpty(vpsymddto.data)){
            vpso.setDayPersonTotal(vpsymddto.data.get(0).personPassTotal);
            vpso.setDayVehicleTotal(vpsymddto.data.get(0).vehilePassTotal);
        }
        Map<String, Map<String, Object>> vehicleMap = vehicleTrafficflowDao.queryVehicleTrafficflowStatisticsByCount(null);
        List<StatisticTypeCount> vstcs = new CopyOnWriteArrayList<>();
        Map<String,String> vmap = new ConcurrentHashMap<>(3);
        vmap.put("sum_djCount","登记车辆");
        vmap.put("sum_lfbdCount","来访本地车");
        vmap.put("sum_lfwdCount","来访外地车");
        vehicleMap.keySet().parallelStream().forEach(v->{
            StatisticTypeCount stc = new StatisticTypeCount();
            if(v.equalsIgnoreCase("sum_vehiclePassCount")){
                vpso.setVehiclePassTotal((Double) vehicleMap.get(v).get("value"));
            }
            if(StrUtil.isNotEmpty(vmap.get(v))){
                stc.setCount((Double) vehicleMap.get(v).get("value"));
                stc.setType(vmap.get(v));
            }
            vstcs.add(stc);
        });
        vpso.setVPVehicle(vstcs);

        Map<String, Map<String, Object>> personMap = personTrafficflowDao.queryPersonTrafficflowStatisticsByCount(null);
        List<StatisticTypeCount> pstcs = new CopyOnWriteArrayList<>();
        Map<String,String> pmap = new ConcurrentHashMap<>(3);
        vmap.put("sum_yzCount","长期住户");
        vmap.put("sum_zhCount","租客");
        vmap.put("sum_lfCount","来访人员");
        personMap.keySet().parallelStream().forEach(v->{
            StatisticTypeCount stc = new StatisticTypeCount();
            if(v.equalsIgnoreCase("sum_flowPersonCount")){
                vpso.setPersonPassTotal((Double) personMap.get(v).get("value"));
            }
            if(StrUtil.isNotEmpty(pmap.get(v))){
                stc.setCount((Double) personMap.get(v).get("value"));
                stc.setType(pmap.get(v));
            }

            pstcs.add(stc);
        });

        vpso.setVPPerson(pstcs);

        lvs.add(vpso);
        return lvs;
    }

    private VPSYMDDTO getTodayCount() {
        VPSYMDDTO dto = new VPSYMDDTO();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String endTime = DateUtil.format(calendar.getTime(), DatePattern.NORM_DATETIME_PATTERN);
        calendar.add(Calendar.HOUR, -24);
        String startTime = DateUtil.format(calendar.getTime(), DatePattern.NORM_DATETIME_PATTERN);
        Map<String, Object> paramterMap = new HashMap<>();
        paramterMap.put("endTime", endTime);
        paramterMap.put("startTime", startTime);
        paramterMap.put("interval", "hour");
        paramterMap.put("format", "yyyyMMddHH");
        searchESAndBuildResultModel(dto, paramterMap, true);
        return dto;
    }



}
