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

/**
 * @author cyh
 */
@Data
@ApiModel(value = "VillageCommunityDetailVO", description = "社区基础数据VO")
public class VillageCommunityDetailVO {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "社区id")
	private String id;

	@ApiModelProperty(value = "社区名称")
	private String name;

	@ApiModelProperty(value = "楼栋id")
	private String buildingId;

	@ApiModelProperty(value = "楼栋名称")
	private String buildingName;

	@ApiModelProperty(value = "单元id")
	private String unitId;

	@ApiModelProperty(value = "单元名称")
	private String unitName;

	@ApiModelProperty(value = "房间id")
	private String houseId;

	@ApiModelProperty(value = "房间名称")
	private String houseName;

	@ApiModelProperty(value = "住户id")
	private String personId;

}
