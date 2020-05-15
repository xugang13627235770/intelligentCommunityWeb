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

import cn.citms.icw.entity.Vehicle;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 社区车辆信息视图实体类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "VehicleVO对象", description = "社区车辆信息")
public class VehicleVO extends Vehicle {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "社区名称")
	private String               sqmc;
	@ApiModelProperty(value = "社区编码")
	private String               sqbm;
	@ApiModelProperty(value = "号牌种类")
	private String               plateTypeCn;
	@ApiModelProperty(value = "号牌颜色")
	private String               plateColorCn;
	@ApiModelProperty(value = "车身颜色")
	private String               vehicleBodyColorCn;
	@ApiModelProperty(value = "关联门户")
	private List<HouseVO>               relateHouseList;
	@ApiModelProperty(value = "附件集合")
	private List<AttachmentVO> files;

	@ApiModelProperty(value = "登记开始时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date registeBeginTime;
	@ApiModelProperty(value = "登记结束时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date registeEndTime;

	@ApiModelProperty(value = "页码数")
	private Integer pagesize;
	@ApiModelProperty(value = "第几页")
	private Integer pageindex;

}
