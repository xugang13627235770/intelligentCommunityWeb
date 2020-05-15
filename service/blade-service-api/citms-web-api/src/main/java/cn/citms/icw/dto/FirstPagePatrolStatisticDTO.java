package cn.citms.icw.dto;

import cn.citms.icw.vo.EsPatrollogVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "巡更统计模型")
public class FirstPagePatrolStatisticDTO {

    @ApiModelProperty("巡更记录总数")
    public double patrollogTotal;

    @ApiModelProperty("巡更记录今日数量")
    public double patrolTodayCount;

    @ApiModelProperty("巡更记录")
    public List<EsPatrollogVO> esPatrollog;

}
