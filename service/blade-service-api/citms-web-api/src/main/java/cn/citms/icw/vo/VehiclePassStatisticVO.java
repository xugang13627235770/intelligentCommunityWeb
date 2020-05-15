package cn.citms.icw.vo;

import cn.citms.icw.entity.StatisticTypeCount;
import lombok.Data;

import java.util.List;

/**
 * <pre>
 *     车辆通行统计查询信息显示对象
 * </pre>
 * @author liuyuyang
 */
@Data
public class VehiclePassStatisticVO  {

    /**
    * 社区名称
    */
    public String sqmc ;

    /**
    * 累计行人通行数量
    */
    public double personPassTotal ;

    /**
    * 累计车辆通行数量
    */
    public double vehiclePassTotal ;

    /**
    * 今日行人通行数量
    */
    public double dayPersonTotal ;

    /**
    * 今日车辆通行数量
    */
    public double dayVehicleTotal ;

    /**
    * 行人流量构成
    */
    public List<StatisticTypeCount> vPPerson ;

    /**
    * 车流量构成
    */
    public List<StatisticTypeCount> vPVehicle ;
}
