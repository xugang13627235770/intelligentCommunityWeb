package cn.citms.icw.controller;

import cn.citms.icw.dto.FirstPageDeviceOnlineRateStatisticDTO;
import cn.citms.icw.dto.FirstPagePatrolStatisticDTO;
import cn.citms.icw.dto.FirstPageStatisticDTO;
import cn.citms.icw.dto.VPSYMDDTO;
import cn.citms.icw.entity.VehicleStatisticViewModel;
import cn.citms.icw.service.IStatisticService;
import cn.citms.icw.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/statistic")
@Api(value = "统计查询", tags = "统计查询接口")
public class StatisticController {

    @Resource
    private IStatisticService statisticService;



    /**
     * 首页统计（根据小区类型统计设备在线率）
     */
    @PostMapping("/QuerySYDeviceOnlineRateStatistic")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "首页统计（根据小区类型统计设备在线率）")
    public R<FirstPageDeviceOnlineRateStatisticDTO> QuerySYDeviceOnlineRateStatistic(@RequestBody StatisticDefVO vo) {
        return R.data(statisticService.QuerySYDeviceOnlineRateStatistic(vo));
    }

    /**
     * 首页统计（巡更记录）
     */
    @PostMapping("/QuerySYPatrolStatistic")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "首页统计（巡更记录）")
    public R<FirstPagePatrolStatisticDTO> QuerySYPatrolStatistic(@RequestBody StatisticDefVO vo) {
        return R.data(statisticService.QuerySYPatrolStatistic(vo));
    }

    /**
     * 统计车辆数据:住户登记总数、门户登记总数、车辆登记总数、根据登记车辆构成统计车辆数量、根据登记车辆归属地统计车辆数量
     */
    @PostMapping("/QueryVehicleStatistic")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "统计车辆数据:住户登记总数、门户登记总数、车辆登记总数、根据登记车辆构成统计车辆数量、根据登记车辆归属地统计车辆数量")
    public R<VehicleStatisticViewModel> QueryVehicleStatistic(@RequestBody StatisticsVehicleDefVO vo) {
        return R.data(statisticService.QueryVehicleStatistic(vo));
    }

    /**
     * 统计住户数据：住户登记总数、门户登记总数、户主家属数量、租客数量、根据住户构成的住户数量、根据房屋用途统计的房屋数量
     */
    @PostMapping("/QueryPersonStatistic")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "统计住户数据：住户登记总数、门户登记总数、户主家属数量、租客数量、根据住户构成的住户数量、根据房屋用途统计的房屋数量")
    public R<List<PCIStatisticVO>> QueryPersonStatistic(@RequestBody StatisticDefVO vo) {
        return R.data(statisticService.QueryPersonStatistic(vo));
    }

    @PostMapping("/QueryAlarmStatistic")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "检测预警统计查询")
    public R<List<AlarmStatisticVO>> queryAlarmStatistic(@RequestBody StatisticsDefSearchVO vo){
        return R.data(statisticService.queryAlarmStatistic(vo));
    }

    /**
     * 首页统计（根据小区类型统计小区数量，以及根据小区设备类型统计设备数量,特殊人群，行车、车辆通行总流量）
     */
    @PostMapping("/QuerySYStatistic")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "首页统计（根据小区类型统计小区数量，以及根据小区设备类型统计设备数量,特殊人群，行车、车辆通行总流量）")
    public R<FirstPageStatisticDTO> QuerySYStatistic(@RequestBody StatisticDefVO vo) {
        return R.data(statisticService.QuerySYStatistic(vo));
    }

    /**
     * 车辆通行统计查询-年月日
     */
    @PostMapping("/QueryVehiclePassStatistic_YMD")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "车辆通行统计查询-年月日")
    public R<VPSYMDDTO> QueryVehiclePassStatistic_YMD(@RequestBody StatisticDefVO vo) {
        return R.data(statisticService.QueryVehiclePassStatistic_YMD(vo));
    }

    /**
     * 车辆通行统计查询
     */
    @PostMapping("/QueryVehiclePassStatistic")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "车辆通行统计查询")
    public R<List<VehiclePassStatisticVO>> queryVehiclePassStatistic(@RequestBody  StatisticsDefSearchVO vo) {
        return R.data(statisticService.queryVehiclePassStatistic(vo));
    }


}
