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
package cn.citms.icw.vo;

import cn.citms.icw.entity.Notice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 通知公告表视图实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "NoticeVO对象", description = "通知公告表")
public class NoticeVO extends Notice {
	private static final long serialVersionUID = 1L;

	/**
	 * 公告ID =NOTICEID
	 */
	@ApiModelProperty(value = "公告ID")
	public String id;
	/**
	 * 公告信息 Like, FieldName = "TITLE,CONTENT"
	 */
	@ApiModelProperty(value = "公告信息")
	public String keyWord;
	/**
	 * 发布开始时间 GE, FieldName = "PUBTIME"
	 */
	@ApiModelProperty(value = "发布开始时间")
	public Date pubStartTime;
	/**
	 * 发布截止时间 LE, FieldName = "PUBTIME"
	 */
	@ApiModelProperty(value = "发布开始时间")
	public Date pubEndTime;
	/**
	 * 到期开始时间 GE, FieldName = "DUETIME"
	 */
	@ApiModelProperty(value = "到期开始时间")
	public Date dueStartTime;
	/**
	 * 到期截止时间 LE, FieldName = "DUETIME"
	 */
	@ApiModelProperty(value = "到期截止时间")
	public Date dueEndTime;
	/**
	 * 社区ID
	 */
	@ApiModelProperty(value = "社区ID")
	public List<String> communityIds;


	private Integer pageIndex;
	private Integer pageSize;
	private String sortField;
	private String sortOrder;

	/**
	 * 社区名称
	 */
	public String sqmc;

	/**
	 * 公告类型
	 */
	public String dic_NoticeType;
}
