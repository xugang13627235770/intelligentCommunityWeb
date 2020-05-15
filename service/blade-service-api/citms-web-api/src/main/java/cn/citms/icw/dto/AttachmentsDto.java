package cn.citms.icw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel(description = "附件信息类")
public class AttachmentsDto {

    @ApiModelProperty("附件标识符")
    private String attachmentId;

    @ApiModelProperty("路径")
    private String url;

    @ApiModelProperty("附件名称")
    private String name;

    @ApiModelProperty("绝对路径")
    private String absoluteUrl;

    @ApiModelProperty("引申")
    private String extenson;
}
