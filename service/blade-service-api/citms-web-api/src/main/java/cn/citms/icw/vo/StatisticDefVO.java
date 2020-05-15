package cn.citms.icw.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("统计查询入参模型")
public class StatisticDefVO {

    @ApiModelProperty(value = "社区标识符")
    private String community_Id;

    @ApiModelProperty(value = "楼栋标识符")
    private String building_Id;

    @ApiModelProperty(value = "单元标识符")
    private String unit_Id;

    @ApiModelProperty(value = "门户标识符")
    private String house_Id;

    @ApiModelProperty(value = "查询类型（年：1；月：2；日：3）")
    private String dateType;

    @ApiModelProperty(value = "社区标识符集合", hidden = true)
    private List<String> communityIds;

    @ApiModelProperty(value = "社区编号集合",  hidden = true)
    private List<String> exts;

}
