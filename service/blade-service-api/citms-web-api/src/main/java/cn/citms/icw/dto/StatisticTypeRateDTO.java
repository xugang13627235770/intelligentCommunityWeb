package cn.citms.icw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("一个类型，类型对应百分比")
public class StatisticTypeRateDTO {

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("数量")
    private String rate;

}
