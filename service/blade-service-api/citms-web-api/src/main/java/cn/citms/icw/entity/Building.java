package cn.citms.icw.entity;

import cn.citms.icw.utils.IntergerSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 楼栋信息实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@TableName("sq_building")
@ApiModel(value = "Building对象", description = "楼栋信息")
public class Building  {

	private static final long serialVersionUID = 1L;

	/**
	* ID
	*/
	@ApiModelProperty(value = "ID")
	private String id;
	/**
	* 建筑代码ID
	*/
	@ApiModelProperty(value = "建筑代码ID")
	@TableField(value="jzdm_id")
	private String jzdm_Id;
	/**
	* 建筑编码
	*/
	@ApiModelProperty(value = "建筑编码")
	@TableField(value="jz_bm")
	private String jz_Bm;
	/**
	* 建筑物名称
	*/
	@ApiModelProperty(value = "建筑物名称")
	private String jzwmc;
	/**
	* 门户数量
	*/
	@ApiModelProperty(value = "门户数量")
	@TableField(value="mh_sl")
	private String mh_Sl;
	/**
	* 楼栋号名称
	*/
	@ApiModelProperty(value = "楼栋号名称")
	@NotBlank(message="请输入楼栋名称")
	@TableField(value="ldh_mc")
	private String ldh_Mc;
	/**
	* 楼栋标识
	*/
	@ApiModelProperty(value = "楼栋标识")
	@TableField(value="ld_bs")
	private String ld_Bs;
	/**
	* 建筑性质
	*/
	@ApiModelProperty(value = "建筑性质-字典编码")
	@TableField(value="jz_xz")
	private String jz_Xz;
	/**
	* 修建日期时间
	*/
	@ApiModelProperty(value = "修建日期时间")
	@TableField(value="xj_rqsj")
	private Date xj_Rqsj;
	/**
	* 住宅单元数量
	*/
	@ApiModelProperty(value = "住宅单元数量")
	@TableField(value="zzdy_sl")
	private String zzdy_Sl;
	/**
	* 地面层数
	*/
	@ApiModelProperty(value = "地面层数")
	@TableField(value="dm_cs")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Integer dm_Cs;
	/**
	* 地下层数
	*/
	@ApiModelProperty(value = "地下层数")
	@TableField(value="dx_cs")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Integer dx_Cs;
	/**
	* 面积（平方米）
	*/
	@ApiModelProperty(value = "面积（平方米）")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Double mjpfm;
	/**
	* 排序编号
	*/
	@ApiModelProperty(value = "排序编号")
	@TableField(value="px_bh")
	private String px_Bh;
	/**
	* 扫码入住实施有效期
	*/
	@ApiModelProperty(value = "扫码入住实施有效期")
	@TableField(value="smrzss_yxq")
	private String smrzss_Yxq;
	/**
	* 地下层居住层数
	*/
	@ApiModelProperty(value = "地下层居住层数")
	@TableField(value="dxcjz_cs")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Double dxcjz_Cs;
	/**
	* 创建时间
	*/
	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date cjsj;
	/**
	* 更新时间
	*/
	@ApiModelProperty(value = "更新时间")
	private Date gxsj;
	/**
	* 经度纬度详细情况
	*/
	@ApiModelProperty(value = "经度纬度详细情况")
	@TableField(value="jdwd_xxqk")
	private String jdwd_Xxqk;
	/**
	* 社区代码ID
	*/
	@ApiModelProperty(value = "社区代码ID")
	@NotBlank(message="请选择社区名称")
	@TableField(value="community_id")
	private String community_Id;
	/**
	* 地球经度
	*/
	@ApiModelProperty(value = "地球经度")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Double dqjd;
	/**
	* 地球纬度
	*/
	@ApiModelProperty(value = "地球纬度")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Double dqwd;

}
