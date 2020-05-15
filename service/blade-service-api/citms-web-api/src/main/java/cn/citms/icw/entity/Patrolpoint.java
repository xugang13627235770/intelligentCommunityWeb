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
@TableName("sq_patrolpoint")
@ApiModel(value = "Patrolpoint对象", description = "巡逻点对象")
public class Patrolpoint{

	private static final long serialVersionUID = 1L;

	/**
	* id
	*/
		@ApiModelProperty(value = "id")
		private String id;
	/**
	* 社区ID
	*/
		@ApiModelProperty(value = "社区ID")
		@TableField("communityId")
	private String communityId;
	/**
	* 设备编号
	*/
		@ApiModelProperty(value = "设备编号")
		@TableField("deviceNo")
	private String deviceNo;
	/**
	* 巡更点编号
	*/
		@ApiModelProperty(value = "巡更点编号")
		@TableField("pointNumber")
	private String pointNumber;
	/**
	* 巡更点名称
	*/
		@ApiModelProperty(value = "巡更点名称")
		@TableField("pointName")
	private String pointName;
	/**
	* 经度
	*/
		@ApiModelProperty(value = "经度")
		private Double dqjd;
	/**
	* 纬度
	*/
		@ApiModelProperty(value = "纬度")
		private Double dqwd;


}
