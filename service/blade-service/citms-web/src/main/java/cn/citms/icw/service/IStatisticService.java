package cn.citms.icw.service;

import cn.citms.icw.dto.FirstPageDeviceOnlineRateStatisticDTO;
import cn.citms.icw.dto.FirstPageStatisticDTO;
import cn.citms.icw.dto.VPSYMDDTO;
import cn.citms.icw.entity.VehicleStatisticViewModel;
import cn.citms.icw.dto.FirstPagePatrolStatisticDTO;
import cn.citms.icw.vo.*;
import org.springblade.core.tool.api.R;

import java.util.List;

public interface IStatisticService{

    FirstPageDeviceOnlineRateStatisticDTO QuerySYDeviceOnlineRateStatistic(StatisticDefVO vo);

    FirstPagePatrolStatisticDTO QuerySYPatrolStatistic(StatisticDefVO vo);

    VehicleStatisticViewModel QueryVehicleStatistic(StatisticsVehicleDefVO vo);

    /**
     * 统计住户数据：住户登记总数、门户登记总数、户主家属数量、租客数量、根据住户构成的住户数量、根据房屋用途统计的房屋数量
     * @param vo
     * @return
     */
    List<PCIStatisticVO> QueryPersonStatistic(StatisticDefVO vo);


    /**
     * 统计预警数据
     * @param vo {@link StatisticsDefSearchVO} 查询参数
     * @return List {@link List} 返回参数
     */
    List<AlarmStatisticVO> queryAlarmStatistic(StatisticsDefSearchVO vo);

    FirstPageStatisticDTO QuerySYStatistic(StatisticDefVO vo);

    VPSYMDDTO QueryVehiclePassStatistic_YMD(StatisticDefVO vo);

    /**
     * 车辆通行统计查询
     * @param vo  {@link StatisticsDefSearchVO} 参数
     * @return list {@link VehiclePassStatisticVO} 返回值
     */
    List<VehiclePassStatisticVO> queryVehiclePassStatistic(StatisticsDefSearchVO vo);
}
