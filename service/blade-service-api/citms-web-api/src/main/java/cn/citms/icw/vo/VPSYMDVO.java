package cn.citms.icw.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("车辆通行统计查询-年月日")
public class VPSYMDVO {

    @ApiModelProperty("时间区间")
    public String tjsj;

    @ApiModelProperty("车（通行数量）")
    public Integer vehilePassTotal;

    @ApiModelProperty("行人（通行数量）")
    public Integer personPassTotal;

}
