package cn.citms.icw.vo;

import cn.citms.icw.entity.ESPatrollog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *      检测预警数据VO
 * </pre>
 *
 * @author liuyuyang
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class EsPatrollogVO extends ESPatrollog {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("社区名称")
    public String sqmc;

    @ApiModelProperty("社区ID")
    public String communityId;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String phone;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idCard;


}
