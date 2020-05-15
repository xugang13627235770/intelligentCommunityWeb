package cn.citms.icw.dto;


import cn.citms.icw.vo.VPSYMDVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "车辆通行统计查询信息显示对象")
public class VPSYMDDTO {

    @ApiModelProperty("社区名称")
    public String sqmc;

    @ApiModelProperty("车辆通行统计查询-年月日")
    public List<VPSYMDVO> data;

}
