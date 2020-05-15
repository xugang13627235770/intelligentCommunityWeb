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

import cn.citms.icw.entity.House;
import cn.citms.icw.utils.IntergerSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 门户信息视图实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HouseVO对象", description = "门户信息")
public class HouseVO extends House {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "社区名称")
	private String sqmc;

	@ApiModelProperty(value = "楼栋名称")
	private String ldmc;

	@ApiModelProperty(value = "单元名称")
	private String dymc;

	@ApiModelProperty(value = "房屋状态")
	private String dic_Fwzt;

	@ApiModelProperty(value = "房屋类型")
	private String dic_Fwlx;

	@ApiModelProperty(value = "房屋所有权类型")
	private String dic_Fwsyq;

	@ApiModelProperty(value = "证件类型")
	private String dic_Zjlx;

	@ApiModelProperty(value = "住户数")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Integer personCount;

	@ApiModelProperty(value = "车辆数")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Integer vehicleCount;

	@ApiModelProperty(value = "户主姓名")
	private String hzName;

	@ApiModelProperty(value = "户主证件号码")
	private String hzZjhm;

	@ApiModelProperty(value = "社区地址")
	private String sqdz;

	@ApiModelProperty(value = "车辆信息")
	private List<VehicleVO> vehicleList;

	@ApiModelProperty(value = "人员信息")
	private List<PersoncheckinVO> personList;

	@ApiModelProperty(value = "附件信息")
	private List<AttachmentVO> files;

	@ApiModelProperty(value = "所在楼层号")
	private String szLch;

	@ApiModelProperty(value = "门户编号FW_BH")
	private String house_Id;

	@ApiModelProperty(hidden = true)
	private List<String> communityIds;

	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Integer pageSize;
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Integer pageIndex;

}
