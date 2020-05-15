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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@TableName("sq_onestandardthreefacts")
@ApiModel(value = "Onestandardthreefacts对象", description = "Onestandardthreefacts对象")
public class Onestandardthreefacts{

	private static final long serialVersionUID = 1L;

	/**
	* ID
	*/
	@ApiModelProperty(value = "ID")
	private Integer id;
	/**
	* 职业类别代码
	*/
	@ApiModelProperty(value = "职业类别代码")
	private String zylbdm;
	/**
	* 籍贯省市县代码
	*/
	@ApiModelProperty(value = "籍贯省市县代码")
	private String jgssxdm;
	/**
	* 暂住处所代码
	*/
	@ApiModelProperty(value = "暂住处所代码")
	private String zzcsdm;
	/**
	* 门牌号
	*/
	@ApiModelProperty(value = "门牌号")
	private String mph;
	/**
	* 性别代码
	*/
	@ApiModelProperty(value = "性别代码")
	private String xbdm;
	/**
	* 暂住事由
	*/
	@ApiModelProperty(value = "暂住事由")
	private String zzsy;
	/**
	* 户籍地址_地址名称
	*/
	@ApiModelProperty(value = "户籍地址_地址名称")
	@TableField(value = "hjdz_dzmc")
	private String hjdz_Dzmc;
	/**
	* 文化程度代码
	*/
	@ApiModelProperty(value = "文化程度代码")
	private String whcddm;
	/**
	* 姓名
	*/
	@ApiModelProperty(value = "姓名")
	private String xm;
	/**
	* 民族代码
	*/
	@ApiModelProperty(value = "民族代码")
	private String mzdm;
	/**
	* 用户标识
	*/
	@ApiModelProperty(value = "用户标识")
	private String yhbs;
	/**
	* 身份证号码
	*/
	@ApiModelProperty(value = "身份证号码")
	private String sfzhm;
	/**
	* 业务类型
	*/
	@ApiModelProperty(value = "业务类型")
	private String ywlx;
	/**
	* 所属责任区代码
	*/
	@ApiModelProperty(value = "所属责任区代码")
	private String sszrqdm;
	/**
	* 服务处所
	*/
	@ApiModelProperty(value = "服务处所")
	private String fwcs;
	/**
	* 标准地址代码
	*/
	@ApiModelProperty(value = "标准地址代码")
	private String bzdzdm;
	/**
	* 婚姻状况代码
	*/
	@ApiModelProperty(value = "婚姻状况代码")
	private String hyzkdm;
	/**
	* 乡镇（街道）代码
	*/
	@ApiModelProperty(value = "乡镇（街道）代码")
	private String xzjddm;
	/**
	* 联系电话
	*/
	@ApiModelProperty(value = "联系电话")
	private String lxdh;
	/**
	* 户籍地_行政区划代码
	*/
	@ApiModelProperty(value = "户籍地_行政区划代码")
	@TableField(value = "hjd_xzqhdm")
	private String hjd_Xzqhdm;
	/**
	* 出生日期
	*/
	@ApiModelProperty(value = "出生日期")
	private Date csrq;
	/**
	* 登记日期
	*/
	@ApiModelProperty(value = "登记日期")
	private Date djrq;
	/**
	* 照相_日期时间
	*/
	@ApiModelProperty(value = "照相_日期时间")
	@TableField(value = "zx_rqsj")
	private Date zx_Rqsj;
	/**
	* 地球经度
	*/
	@ApiModelProperty(value = "地球经度")
	private Double dqjd;
	/**
	* 地球纬度
	*/
	@ApiModelProperty(value = "地球纬度")
	private Double dqwd;

}
