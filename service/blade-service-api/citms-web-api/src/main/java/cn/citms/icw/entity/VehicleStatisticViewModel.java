package cn.citms.icw.entity;

import lombok.Data;

import java.util.List;

@Data
public class VehicleStatisticViewModel {

    /// <summary>
    /// 社区名称
    /// </summary>
    public String sqmc;

    /// <summary>
    /// 登记居民人数
    /// </summary>
    public double personTotal;

    /// <summary>
    /// 登记门户总数量
    /// </summary>
    public double houseTotal;

    /// <summary>
    /// 登记车辆总数
    /// </summary>
    public double vehicleTotal;

    /// <summary>
    /// 户均车辆数
    /// </summary>
    public double vehicleperHouseTotal;

    /// <summary>
    /// 车辆构成
    /// </summary>
    public List<StatisticTypeCount> vehicleLX;

    /// <summary>
    /// 车辆号牌归属
    /// </summary>
    public List<StatisticTypeCount> vehicleHPGL;

    /// <summary>
    /// 最近12个月的车辆登记
    /// </summary>
    public List<StatisticTypeTotal> theMonVehicle;


}
