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

import cn.citms.icw.utils.IntZeroSerializer;
import cn.citms.icw.utils.IntergerSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cyh
 */
@Data
@ApiModel(value = "VillageVO", description = "社区基础数据VO")
public class VillageVO {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "社区id")
	private String id;

	@ApiModelProperty(value = "社区名称")
	private String name;

	@ApiModelProperty(value = "经度")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Double longitude;

	@ApiModelProperty(value = "纬度")
	@JsonSerialize(nullsUsing = IntergerSerializer.class)
	private Double latitude;

	@ApiModelProperty(value = "楼栋数量")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Long buildingCount;

	@ApiModelProperty(value = "单元数量")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Long unitCount;

	@ApiModelProperty(value = "门户数量")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Long houseCount;

	@ApiModelProperty(value = "住户数量")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Long personCount;

	@ApiModelProperty(value = "登记车辆")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Long vehicleCount;

}
