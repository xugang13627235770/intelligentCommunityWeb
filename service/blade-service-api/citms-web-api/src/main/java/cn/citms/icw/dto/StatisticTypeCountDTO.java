package cn.citms.icw.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "统计一个类型对应的数量")
public class StatisticTypeCountDTO {

    /// 类型
    public String type;

    /// 数量
    public double count;

}
