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

import com.baomidou.mybatisplus.annotation.TableName;
import org.springblade.core.mp.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@TableName("sq_patroltask")
@ApiModel(value = "Patroltask对象", description = "巡逻任务对象")
public class Patroltask {

	private static final long serialVersionUID = 1L;

	/**
	* id
	*/
		@ApiModelProperty(value = "id")
		private String id;
	/**
	* 社区Id
	*/
		@ApiModelProperty(value = "社区Id")
		@TableField("communityId")
	private String communityId;
	/**
	* 巡更人姓名
	*/
		@ApiModelProperty(value = "巡更人姓名")
		private String name;
	/**
	* 联系方式
	*/
		@ApiModelProperty(value = "联系方式")
		private String phone;
	/**
	* 身份证号
	*/
		@ApiModelProperty(value = "身份证号")
		@TableField("idCard")
	private String idCard;
	/**
	* 设备编号
	*/
		@ApiModelProperty(value = "设备编号")
		@TableField("deviceNo")
	private String deviceNo;


}
