package cn.citms.icw.vo;

import cn.citms.icw.entity.Building;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 楼栋信息视图- 响应对象
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BuildingVO对象", description = "楼栋信息")
public class BuildingVO extends Building {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询条件
	 */
	@ApiModelProperty(value = "建成时间-开始")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date startTime;

	@ApiModelProperty(value = "建成时间-结束")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date endTime;

	@ApiModelProperty(value = "楼栋图片")
	private List<AttachmentVO> files;



	@ApiModelProperty(hidden = true)
	private List<String> communityIds;

	@ApiModelProperty(value = "社区名称")
	private String sqmc;

	@ApiModelProperty(value = "建筑性质-字典名称")
	private String dic_Jzxz;

	@ApiModelProperty(value = "建成时间")
	private String dic_Cjsj;


	private Integer pageSize;
	private Integer pageIndex;
}
