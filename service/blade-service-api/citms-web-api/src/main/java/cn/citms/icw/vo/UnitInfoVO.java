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
 * 单元信息
 * @author cyh
 */
@ApiModel(value = "UnitInfoVO", description = "单元信息")
public class UnitInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "单元id")
	private String unitId;

	@ApiModelProperty(value = "单元名称")
	private String unitName;

	@ApiModelProperty(value = "房屋总数")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Integer houseCount;

	@ApiModelProperty(value = "住户总数")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Integer personCount;

	@ApiModelProperty(value = "房间详情")
	private List<HouseInfoVO> houseInfo;

	public UnitInfoVO(){
		this.houseInfo = new ArrayList<>(10);
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getHouseCount() {
		return this.houseInfo.size();
	}

	public void setHouseCount(Integer houseCount) {
		this.houseCount = houseCount;
	}

	public Integer getPersonCount() {
		int _personCount = 0;
		for (HouseInfoVO vo : this.houseInfo) {
			_personCount += vo.getPersonCount();
		}
		return _personCount;
	}

	public void setPersonCount(Integer personCount) {
		this.personCount = personCount;
	}

	public List<HouseInfoVO> getHouseInfo() {
		return houseInfo;
	}

	public void setHouseInfo(List<HouseInfoVO> houseInfo) {
		this.houseInfo = houseInfo;
	}
}
