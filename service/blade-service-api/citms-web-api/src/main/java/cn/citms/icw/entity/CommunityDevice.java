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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 社区设备关联表实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@TableName("sq_community_device")
@ApiModel(value = "CommunityDevice对象", description = "社区设备关联表")
public class CommunityDevice  {

	private static final long serialVersionUID = 1L;

	/**
	* 设备Id
	*/
	@ApiModelProperty(value = "设备Id")
	@TableId("deviceId")
	private String deviceId;
	/**
	* 社区Id
	*/
	@ApiModelProperty(value = "社区Id")
	@TableField("communityId")
	private String communityId;
	/**
	* 楼栋Id
	*/
	@ApiModelProperty(value = "楼栋Id")
	@TableField("buildId")
	private String buildId;
	/**
	* 单元Id
	*/
	@ApiModelProperty(value = "单元Id")
	@TableField("unitId")
	private String unitId;
	/**
	* 是否在社区首页显示该设备 1:是 0:否
	*/
	@ApiModelProperty(value = "是否在社区首页显示该设备 1:是 0:否")
	@TableField("communityShow")
	private Integer communityShow;

}
