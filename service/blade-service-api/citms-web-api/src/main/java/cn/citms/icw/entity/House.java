/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package cn.citms.icw.entity;

import cn.citms.icw.utils.IntergerSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 门户信息实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@TableName("sq_house")
@ApiModel(value = "House对象", description = "门户信息")
public class House {

	private static final long serialVersionUID = 1L;

	/**
	* ID
	*/
	@ApiModelProperty(value = "id")
	private String id;
	/**
	* 房屋编号
	*/
	@NotBlank(message = "门户编号不能为空")
	@ApiModelProperty(value = "房屋编号")
	@TableField(value = "fw_bh")
	private String fw_Bh;
	/**
	* 房屋代码
	*/
	@ApiModelProperty(value = "房屋代码")
	@TableField(value = "fw_dm")
	private String fw_Dm;
	/**
	* 房屋标识
	*/
	@ApiModelProperty(value = "房屋标识")
	@TableField(value = "fw_bs")
	private String fw_Bs;
	/**
	* 房屋_状态
	*/
	@ApiModelProperty(value = "房屋_状态")
	@TableField(value = "fw_zt")
	private String fw_Zt;
	/**
	* 房屋_类型
	*/
	@ApiModelProperty(value = "房屋_类型")
	@TableField(value = "fw_lx")
	private String fw_Lx;
	/**
	* 房屋地址名称
	*/
	@ApiModelProperty(value = "房屋地址名称")
	@TableField(value = "fangwu_dzmc")
	private String fangWu_Dzmc;
	/**
	* 建筑面积_面积（平方米）
	*/
	@ApiModelProperty(value = "建筑面积_面积（平方米）")
	@TableField(value = "jzmj_mjpfm")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Double jzmj_Mjpfm;
	/**
	* 所在楼层号
	*/
	@ApiModelProperty(value = "所在楼层号")
	@TableField(value = "sz_lch")
	private String sz_Lch;
	/**
	* 房屋用途_简要情况
	*/
	@ApiModelProperty(value = "房屋用途_简要情况")
	@TableField(value = "fwyt_jyqk")
	private String fwyt_Jyqk;
	/**
	* 居住人数数量
	*/
	@ApiModelProperty(value = "居住人数数量")
	@TableField(value = "jzry_sl")
	private String jzry_Sl;
	/**
	* 委托代理人姓名
	*/
	@ApiModelProperty(value = "委托代理人姓名")
	@TableField(value = "wtdlr_xm")
	private String wtdlr_Xm;
	/**
	* 委托代理人证件类型名称
	*/
	@ApiModelProperty(value = "委托代理人证件类型名称")
	@TableField(value = "wtdlr_zjlxmc")
	private String wtdlr_Zjlxmc;
	/**
	* 委托代理人证件号码
	*/
	@ApiModelProperty(value = "委托代理人证件号码")
	@TableField(value = "wtdlr_zjhm")
	private String wtdlr_Zjhm;
	/**
	* 委托代理人联系电话
	*/
	@ApiModelProperty(value = "委托代理人联系电话")
	@TableField(value = "wtdlr_lxdh")
	private String wtdlr_Lxdh;
	/**
	* 所有权类型
	*/
	@ApiModelProperty(value = "所有权类型")
	@TableField(value = "syq_lx")
	private String syq_Lx;
	/**
	* 重点房屋判断标识
	*/
	@ApiModelProperty(value = "重点房屋判断标识")
	@TableField(value = "zdfw_pdbs")
	private String zdfw_Pdbs;
	/**
	* 租赁_状态
	*/
	@ApiModelProperty(value = "租赁_状态")
	@TableField(value = "zl_zt")
	private String zl_Zt;
	/**
	* 居住住宅_判断标识
	*/
	@ApiModelProperty(value = "居住住宅_判断标识")
	@TableField(value = "jzzz_pdbs")
	private String jzzz_Pdbs;
	/**
	* 入住日期时间
	*/
	@ApiModelProperty(value = "入住日期时间")
	@TableField(value = "rz_rqsj")
	private Date rz_Rqsj;
	/**
	* 过期日期时间
	*/
	@ApiModelProperty(value = "过期日期时间")
	@TableField(value = "gq_rqsj")
	private Date gq_Rqsj;
	/**
	* 单位ID
	*/
	@NotBlank(message = "单元不能为空")
	@ApiModelProperty(value = "单位ID")
	@TableField(value = "unit_id")
	private String unit_Id;
	/**
	* 楼栋ID
	*/
	@NotBlank(message = "楼栋不能为空")
	@ApiModelProperty(value = "楼栋ID")
	@TableField(value = "building_id")
	private String building_Id;
	/**
	* 社区ID
	*/
	@NotBlank(message = "社区不能为空")
	@ApiModelProperty(value = "社区ID")
	@TableField(value = "community_id")
	private String community_Id;
	/**
	* 房间ID（社区民警提供）
	*/
	@NotBlank(message = "房间ID不能为空")
	@ApiModelProperty(value = "房间ID（社区民警提供）")
	@TableField("fjid")
	private String fjId;

}
