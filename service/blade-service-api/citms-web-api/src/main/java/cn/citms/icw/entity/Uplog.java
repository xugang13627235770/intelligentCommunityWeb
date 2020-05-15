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
import java.time.LocalDateTime;
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
@TableName("sq_uplog")
@ApiModel(value = "Uplog对象", description = "Uplog对象")
public class Uplog {

	private static final long serialVersionUID = 1L;

	/**
	* Id
	*/
		@ApiModelProperty(value = "Id")
		private String id;
	/**
	* 上传ID
	*/
		@ApiModelProperty(value = "上传ID")
		@TableField("upId")
	private String upId;
	/**
	* 上传时间
	*/
		@ApiModelProperty(value = "上传时间")
		@TableField("upTime")
	private LocalDateTime upTime;
	/**
	* 上传是否成功状态
	*/
		@ApiModelProperty(value = "上传是否成功状态")
		private String source;
	/**
	* 备注
	*/
		@ApiModelProperty(value = "备注")
		private String remark;


}
