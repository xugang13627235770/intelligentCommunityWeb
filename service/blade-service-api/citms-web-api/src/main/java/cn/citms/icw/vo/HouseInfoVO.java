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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 房屋信息
 * @author cyh
 */
@ApiModel(value = "HouseInfoVO", description = "房屋信息")
public class HouseInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "房间id")
	private String houseId;

	@ApiModelProperty(value = "房间名称")
	private String houseName;

	@ApiModelProperty(value = "住户总数")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Integer personCount;

	@ApiModelProperty(value = "住户id集合")
	private List<String> personIdList;

	public HouseInfoVO(){
		this.personIdList = new ArrayList<>(10);
	}

	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public Integer getPersonCount() {
		return this.personIdList.size();
	}

	public void setPersonCount(Integer personCount) {
		this.personCount = personCount;
	}

	public List<String> getPersonIdList() {
		return personIdList;
	}

	public void setPersonIdList(List<String> personIdList) {
		this.personIdList = personIdList;
	}
}
