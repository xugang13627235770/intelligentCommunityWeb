package cn.citms.icw.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("ES巡更模型")
public class ESPatrollog {

    @ApiModelProperty("自动生成唯一UUID")
    public String uuid;

    @ApiModelProperty("url字段列表")
    public String ___urls;

    @ApiModelProperty("巡逻人名称")
    public String staffName;

    @ApiModelProperty("巡更点编号")
    public String pointNumber;

    @ApiModelProperty("巡更点编号")
    public String pointName;

    @ApiModelProperty("巡更时间")
    public String patrolTime;

    @ApiModelProperty("巡更设备名称")
    public String patrolName;

    @ApiModelProperty("数据来源")
    public String  source;

    @ApiModelProperty("外部字段")
    public String ext;

    @ApiModelProperty("设备id")
    public String deviceId;

    @ApiModelProperty("设备ip")
    public String deviceNo;

    @ApiModelProperty("设备名称")
    public String deviceName;

    @ApiModelProperty("经度")
    public Double  longitude;

    @ApiModelProperty(" 纬度")
    public Double latitude;

    @ApiModelProperty("数据采集时间")
    public String captureTime;

}
