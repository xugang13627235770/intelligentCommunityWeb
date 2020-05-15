package cn.citms.icw.vo;

import cn.citms.icw.entity.StatisticTypeCount;
import cn.citms.icw.entity.StatisticTypeTotal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("住户统计查询信息显示对象")
public class PCIStatisticVO {

    @ApiModelProperty(value = "社区名称")
    private String sqmc;

    @ApiModelProperty(value = "登记居民人数")
    private Double personTotal;

    @ApiModelProperty(value = "登记门户总数量")
    private Double houseTotal;

    @ApiModelProperty(value = "户主家属人数")
    private Double personHeadTotal;

    @ApiModelProperty(value = "租客人数")
    private Double personTenantTotal;

    @ApiModelProperty(value = "特殊人群组成")
    private List<StatisticTypeCount> tsrq;

    @ApiModelProperty(value = "重点人员")
    private List<StatisticTypeCount> zdrq;

    @ApiModelProperty(value = "住户组成")
    private List<StatisticTypeCount> zhzc;

    @ApiModelProperty(value = "房屋用途")
    private List<StatisticTypeCount> fwyt;

    @ApiModelProperty(value = "房屋居住人数分布")
    private List<StatisticTypeCount> fwjzfb;

    @ApiModelProperty(value = "最近12个月的登记人数")
    private List<StatisticTypeTotal> theMonCheckin;

}
