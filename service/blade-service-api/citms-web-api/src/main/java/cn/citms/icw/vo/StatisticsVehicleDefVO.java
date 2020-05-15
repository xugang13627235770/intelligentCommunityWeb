package cn.citms.icw.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("车辆统计查询入参模型")
public class StatisticsVehicleDefVO {

    @ApiModelProperty(value = "社区标识符")
    public String communityId;

    @ApiModelProperty(value = "社区标识符集合", hidden = true)
    public List<String> communityIds;

}
