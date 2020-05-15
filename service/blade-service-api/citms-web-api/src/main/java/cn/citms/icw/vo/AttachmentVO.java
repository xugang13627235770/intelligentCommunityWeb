package cn.citms.icw.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 楼栋信息视图- 响应对象
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@ApiModel(value = "AttachmentVO对象", description = "附件信息")
public class AttachmentVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "附件ID")
    private String attachmentId;

    @ApiModelProperty(value = "附件路径")
    private String url;

    @ApiModelProperty(value = "附件绝对地址")
    private String absoluteUrl;

    @ApiModelProperty(value = "附件名")
    private String name;

    @ApiModelProperty(value = "附件扩展名")
    private String extenson;

}
