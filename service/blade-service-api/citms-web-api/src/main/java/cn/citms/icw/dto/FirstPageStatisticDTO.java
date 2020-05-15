package cn.citms.icw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "首页各类统计返回模型")
public class FirstPageStatisticDTO {

    @ApiModelProperty("智慧社区总数量")
    public Integer sqTotal;

    @ApiModelProperty("示范小区数量")
    public Integer sfSqCount;

    @ApiModelProperty("普通小区数量")
    public Integer ptSqCount;

    @ApiModelProperty("社区名称")
    public String sqmc;

    @ApiModelProperty("登记居民总人数")
    public Integer personTotal;

    @ApiModelProperty("登记楼栋总数量")
    public Integer buildingTotal;

    @ApiModelProperty("登记门户总数量")
    public Integer houseTotal;

    @ApiModelProperty("登记车辆总数")
    public Integer vehicleTotal;

    @ApiModelProperty("登记设备总数")
    public Integer deviceTotal;

    @ApiModelProperty("行人通行总数量")
    public Integer personPassTotal;

    @ApiModelProperty("车辆通行总数量")
    public Integer vehiclePassTotal;

    @ApiModelProperty("特殊人员总数")
    public Integer tsryTotal;

    @ApiModelProperty("特殊人员最多所占百分比")
    public StatisticTypeRateDTO ZbzdRate;

    @ApiModelProperty("巡更记录总数")
    public Integer patrollogTotal;

    @ApiModelProperty("巡更记录今日数量")
    public Integer patrolTodayCount;

    @ApiModelProperty("住户组成")
    public List<StatisticTypeCountDTO> zhzc;

    @ApiModelProperty("房屋用途")
    public List<StatisticTypeCountDTO> fwyt;

    @ApiModelProperty("特殊人员分布")
    public List<StatisticTypeCountDTO> tsryFb;

    @ApiModelProperty("设备统计")
    public List<StatisticTypeViewCountDTO> device;

}
