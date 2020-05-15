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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 社区车辆信息实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@TableName("sq_vehicle")
@ApiModel(value = "Vehicle对象", description = "社区车辆信息")
public class Vehicle {

	private static final long serialVersionUID = 1L;

	/**
	* 车辆Id
	*/
		@ApiModelProperty(value = "车辆Id")
		@TableId("vehicleId")
	private     String        vehicleId;
	/**
	* 社区Id
	*/
		@ApiModelProperty(value = "社区Id")
		@TableField("communityId")
	private String communityId;
	/**
	* 号牌号码
	*/
		@ApiModelProperty(value = "号牌号码")
		@TableField("plateNo")
	private String plateNo;
	/**
	* 号牌颜色
	*/
		@ApiModelProperty(value = "号牌颜色")
		@TableField("plateColor")
	private String plateColor;
	/**
	* 号牌种类
	*/
		@ApiModelProperty(value = "号牌种类")
		@TableField("plateType")
	private String plateType;
	/**
	* 身份证号码
	*/
		@ApiModelProperty(value = "身份证号码")
		@TableField("idCardNo")
	private     String        idCardNo;
	/**
	* 车主姓名
	*/
		@ApiModelProperty(value = "车主姓名")
		@TableField("ownerName")
	private     String        ownerName;
	/**
	* 联系方式
	*/
		@ApiModelProperty(value = "联系方式")
		private String        phone;
	/**
	* 车主地址
	*/
		@ApiModelProperty(value = "车主地址")
		@TableField("ownerAddress")
	private     String        ownerAddress;
	/**
	* 创建人
	*/
		@ApiModelProperty(value = "创建人")
		private String        creator;
	/**
	* 创建日期
	*/
		@ApiModelProperty(value = "创建日期")
		@TableField("createdTime")
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private     Date          createdTime;
	/**
	* 修改人
	*/
		@ApiModelProperty(value = "修改人")
		private String        modifier;
	/**
	* 修改日期
	*/
		@ApiModelProperty(value = "修改日期")
		@TableField("modifiedTime")
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private     Date modifiedTime;
	/**
	* 车身颜色
	*/
		@ApiModelProperty(value = "车身颜色")
		@TableField("vehicleBodyColor")
	private     String        vehicleBodyColor;
	/**
	* 车辆品牌
	*/
		@ApiModelProperty(value = "车辆品牌")
		@TableField("vehicleBrand")
	private     String        vehicleBrand;
	/**
	* 门牌号
	*/
		@ApiModelProperty(value = "门牌号")
		@TableField("roomNo")
	private     String        roomNo;


}
