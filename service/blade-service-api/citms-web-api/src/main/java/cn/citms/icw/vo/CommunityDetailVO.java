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
 * 社区详情显示模型
 * @author cyh
 */
@ApiModel(value = "CommunityDetailVO", description = "社区详情显示模型")
public class CommunityDetailVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "社区id")
	private String id;

	@ApiModelProperty(value = "社区名称")
	private String name;

	@ApiModelProperty(value = "楼栋总数")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Integer buildingCount;

	@ApiModelProperty(value = "单元总数")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Integer unitCount;

	@ApiModelProperty(value = "房间总数")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Integer houseCount;

	@ApiModelProperty(value = "住户总数")
	@JsonSerialize(nullsUsing = IntZeroSerializer.class)
	private Integer personCount;

	@ApiModelProperty(value = "社区图片信息")
	private String imgUrl;

	@ApiModelProperty(value = "楼栋信息集合")
	private List<BuildingInfoVO> buildingInfo;

	public CommunityDetailVO(){
		this.buildingInfo = new ArrayList<>(10);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBuildingCount() {
		return this.buildingInfo.size();
	}

	public void setBuildingCount(Integer buildingCount) {
		this.buildingCount = buildingCount;
	}

	public Integer getUnitCount() {
		int _unitCount = 0;
		for (BuildingInfoVO vo : this.buildingInfo) {
			_unitCount += vo.getUnitCount();
		}
		return _unitCount;
	}

	public void setUnitCount(Integer unitCount) {
		this.unitCount = unitCount;
	}

	public Integer getHouseCount() {
		int _houseCount = 0;
		for (BuildingInfoVO vo : this.buildingInfo) {
			_houseCount += vo.getHouseCount();
		}
		return _houseCount;
	}

	public void setHouseCount(Integer houseCount) {
		this.houseCount = houseCount;
	}

	public Integer getPersonCount() {
		int _personCount = 0;
		for (BuildingInfoVO vo : this.buildingInfo) {
			_personCount += vo.getPersonCount();
		}
		return _personCount;
	}

	public void setPersonCount(Integer personCount) {
		this.personCount = personCount;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<BuildingInfoVO> getBuildingInfo() {
		return buildingInfo;
	}

	public void setBuildingInfo(List<BuildingInfoVO> buildingInfo) {
		this.buildingInfo = buildingInfo;
	}
}
