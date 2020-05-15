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
 * 楼栋信息
 * @author cyh
 */
@ApiModel(value = "BuildingInfoVO", description = "楼栋信息")
public class BuildingInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "楼栋id")
	private String buildingId;

	@ApiModelProperty(value = "楼栋名称")
	private String buildingName;

	@ApiModelProperty(value = "单元总数")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Integer unitCount;

	@ApiModelProperty(value = "房间数量")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Integer houseCount;

	@ApiModelProperty(value = "住户总数")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Integer personCount;

	@ApiModelProperty(value = "单元信息集合")
	private List<UnitInfoVO> unitInfo;

	public BuildingInfoVO(){
		this.unitInfo = new ArrayList<>(10);
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public Integer getUnitCount() {
		return this.unitInfo.size();
	}

	public void setUnitCount(Integer unitCount) {
		this.unitCount = unitCount;
	}

	public Integer getHouseCount() {
		int _houseCount = 0;
		for (UnitInfoVO vo : this.unitInfo) {
			_houseCount += vo.getHouseCount();
		}
		return _houseCount;
	}

	public void setHouseCount(Integer houseCount) {
		this.houseCount = houseCount;
	}

	public Integer getPersonCount() {
		int _personCount = 0;
		for (UnitInfoVO vo : this.unitInfo) {
			_personCount += vo.getPersonCount();
		}
		return _personCount;
	}

	public void setPersonCount(Integer personCount) {
		this.personCount = personCount;
	}

	public List<UnitInfoVO> getUnitInfo() {
		return unitInfo;
	}

	public void setUnitInfo(List<UnitInfoVO> unitInfo) {
		this.unitInfo = unitInfo;
	}
}
