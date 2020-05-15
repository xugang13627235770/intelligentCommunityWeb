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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 社区设备关联表视图实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CommunityDeviceVO对象", description = "社区设备关联表")
public class CommunityDeviceVO extends DeviceVO {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "功能类型中文")
	private String functionName;

	@ApiModelProperty(value = "社区名称")
	private String sqmc;

	@ApiModelProperty(value = "社区编号")
	private String sqbm;

	@ApiModelProperty(value = "楼栋单元名称")
	private String buildUnitName;

	@ApiModelProperty(value = "制造商")
	private String manufacturerName;

	@ApiModelProperty(value = "运维状态名称")
	private String ywStatusName;

	@ApiModelProperty(value = "项目名称")
	private String projectName;

	@ApiModelProperty(value = "所属部门名称")
	private String departmentName;

	@ApiModelProperty(value = "所属部门层级code")
	private String departmentHierarchyCode;

	@ApiModelProperty(value = "部门所属行政区域")
	private String areaName;

	@ApiModelProperty(value = "自定义标签")
	private List<String> arrTag;

	@ApiModelProperty(value = "状态连接信息")
	private StatusConnectInfoVO statusConnectInfo;

	@ApiModelProperty(value = "视频连接信息")
	private DeviceVideoInfoVO deviceVideoInfoVO;

	@ApiModelProperty(value = "搜索关键字")
	private String keyWord;
}
