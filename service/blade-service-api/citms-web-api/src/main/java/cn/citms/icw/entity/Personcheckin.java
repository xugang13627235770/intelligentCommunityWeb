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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springblade.core.mp.base.BaseEntity;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 住户信息实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@TableName("sq_personcheckin")
@ApiModel(value = "Personcheckin对象", description = "住户信息")
public class Personcheckin {

	private static final long serialVersionUID = 1L;

	/**
	* ID
	*/
		@ApiModelProperty(value = "ID")
		private String id;
	/**
	* 姓名
	*/
		@ApiModelProperty(value = "姓名")
		private String xm;
	/**
	* 性别代码
	*/
		@ApiModelProperty(value = "性别代码")
		private String xbdm;
	/**
	* 出生日期
	*/
		@ApiModelProperty(value = "出生日期")
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
		private Date csrq;
	/**
	* 人员类型
	*/
		@ApiModelProperty(value = "人员类型")
		private String rylx;
	/**
	* 人员标识
	*/
		@ApiModelProperty(value = "人员标识")
		@TableField("ry_bs")
		private String ry_Bs;
	/**
	* 联系方式
	*/
		@ApiModelProperty(value = "联系方式")
		private String lxfs;
	/**
	* 手机号码
	*/
		@ApiModelProperty(value = "手机号码")
		private String sjhm;
	/**
	* 民族代码
	*/
		@ApiModelProperty(value = "民族代码")
		private String mzdm;
	/**
	* 文化程度代码
	*/
		@ApiModelProperty(value = "文化程度代码")
		private String whcddm;
	/**
	* 婚姻状况代码
	*/
		@ApiModelProperty(value = "婚姻状况代码")
		private String hyzkdm;
	/**
	* 常用证件类型代码
	*/
		@ApiModelProperty(value = "常用证件类型代码")
		private String cyzjlxdm;
	/**
	* 证件号码
	*/
		@ApiModelProperty(value = "证件号码")
		private String zjhm;
	/**
	* 国籍代码
	*/
		@ApiModelProperty(value = "国籍代码")
		private String gjdm;
	/**
	* 国籍
	*/
		@ApiModelProperty(value = "国籍")
		private String gjdz;
	/**
	* 籍贯
	*/
		@ApiModelProperty(value = "籍贯")
		private String jgmc;
	/**
	* 户籍地址_行政区划代码
	*/
		@ApiModelProperty(value = "户籍地址_行政区划代码")
		@TableField("hjdz_xzqhdm")
		private String hjdz_Xzqhdm;
	/**
	* 户籍地址_地址名称
	*/
		@ApiModelProperty(value = "户籍地址_地址名称")
		@TableField("hjdz_dzmc")
		private String hjdz_Dzmc;
	/**
	* 办理时间
	*/
		@ApiModelProperty(value = "办理时间")
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
		private Date blsj;
	/**
	* 用户标识
	*/
		@ApiModelProperty(value = "用户标识")
		private String yhbs;
	/**
	* 与房主关系代码
	*/
		@ApiModelProperty(value = "与房主关系代码")
		private String ypzgxdm;
	/**
	* 居（暂）住事由
	*/
		@ApiModelProperty(value = "居（暂）住事由")
		private String jzsy;
	/**
	* 居住方式
	*/
		@ApiModelProperty(value = "居住方式")
		@TableField("jzfs_mc")
		private String jzfs_Mc;
	/**
	* 居住期限
	*/
		@ApiModelProperty(value = "居住期限")
		@TableField("jz_qx")
		private Double jz_Qx;
	/**
	* 入住日期时间
	*/
		@ApiModelProperty(value = "入住日期时间")
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
		@TableField("rz_rqsj")
		private Date rz_Rqsj;
	/**
	* 撤离日期时间
	*/
		@ApiModelProperty(value = "撤离日期时间")
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
		@TableField("cl_rqsj")
		private Date cl_Rqsj;
	/**
	* 存储_日期时间
	*/
		@ApiModelProperty(value = "存储_日期时间")
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
		private Date ccRqsj;
	/**
	* 职业类别代码
	*/
		@ApiModelProperty(value = "职业类别代码")
		private String zylbdm;
	/**
	* 重点人员判断标识
	*/
		@ApiModelProperty(value = "重点人员判断标识")
		@TableField("zdry_pdbs")
		private String zdry_Pdbs;
	/**
	* 紧急联系人姓名
	*/
		@ApiModelProperty(value = "紧急联系人姓名")
		@TableField("jjlxr_xm")
		private String jjlxr_Xm;
	/**
	* 紧急联系人电话
	*/
		@ApiModelProperty(value = "紧急联系人电话")
		@TableField("jjlxr_lxdh")
		private String jjlxr_Lxdh;
	/**
	* 访客姓名
	*/
		@ApiModelProperty(value = "访客姓名")
		@TableField("fk_xm")
		private String fk_Xm;
	/**
	* 访客手机号码
	*/
		@ApiModelProperty(value = "访客手机号码")
		@TableField("fk_sjhm")
		private String fk_Sjhm;
	/**
	* 访问事由简要情况
	*/
		@ApiModelProperty(value = "访问事由简要情况")
		@TableField("fwsy_jyqk")
		private String fwsy_Jyqk;
	/**
	* 访问时间
	*/
		@ApiModelProperty(value = "访问时间")
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
		@TableField("fw_rqsj")
		private Date fw_Rqsj;
	/**
	* 访问人员姓名
	*/
		@ApiModelProperty(value = "访问人员姓名")
		@TableField("fwry_xm")
		private String fwry_Xm;
	/**
	* 单元ID
	*/
		@ApiModelProperty(value = "单元ID")
		@TableField("unit_id")
		private String unit_Id;
	/**
	* 门户ID
	*/
		@ApiModelProperty(value = "门户ID")
		@TableField("house_id")
		private String house_Id;
	/**
	* 楼栋ID
	*/
		@ApiModelProperty(value = "楼栋ID")
		@TableField("building_id")
		private String building_Id;
	/**
	* 社区ID
	*/
		@ApiModelProperty(value = "社区ID")
		@TableField("community_id")
		private String community_Id;
	/**
	* 服务单位
	*/
		@ApiModelProperty(value = "服务单位")
		private String fwdw;
	/**
	* 暂住处所
	*/
		@ApiModelProperty(value = "暂住处所")
		private String zzcs;


}
