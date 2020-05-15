package cn.citms.icw.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "统计一个类型对应的数量 试图展示")
public class StatisticTypeViewCountDTO {

    /// 类型
    public String type;

    /// 数量
    public String count;

}
