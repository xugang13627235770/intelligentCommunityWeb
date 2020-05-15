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
package cn.citms.icw.service.impl;

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.dto.AttachmentsDto;
import cn.citms.icw.mapper.BuildingMapper;
import cn.citms.icw.service.IVillageService;
import cn.citms.icw.vo.*;
import cn.citms.mbd.basicdatacache.service.impl.ComponentUtil;
import com.xiaoleilu.hutool.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cyh
 */
@Service
public class VillageServiceImpl implements IVillageService {

	@Resource
	private BaseService baseService;
	@Resource
	private BuildingMapper buildingMapper;

	@Value("${dgaRpc.getAttachmentBySourceIdUrl}")
	private String getAttachmentBySourceIdUrl;

	@Autowired
	private ComponentUtil componentUtil;

	@Override
	public List<VillageVO> getVillageCount() {
		List<String> departmentIds = baseService.getPermissionDepartment();
		if(CollUtil.isEmpty(departmentIds)) {
			return new ArrayList<>(0);
		}
		//测试Arrays.asList("420000101","42000010101","42000010102","42000010203","420000103")
		return buildingMapper.selectVillageInfo(departmentIds);
	}

	@Override
	public CommunityDetailVO selectCommunityDetail(String communityId) {
		CommunityDetailVO communtiy = new CommunityDetailVO();
		List<VillageCommunityDetailVO> list = buildingMapper.selectCommunityDetail(communityId);
		if(CollUtil.isEmpty(list)) {
			return communtiy;
		}
		VillageCommunityDetailVO model = list.get(0);
		if(model == null) {
			return communtiy;
		}
		communtiy.setId(model.getId());
		communtiy.setName(model.getName());
		communtiy.setImgUrl(getZhsqImgUrl(model.getId()));
		list = list.stream().sorted(Comparator.comparing(VillageCommunityDetailVO::getBuildingName)
				.thenComparing(VillageCommunityDetailVO::getUnitName)
				.thenComparingInt(a -> a.getHouseName().length())
				.thenComparing(VillageCommunityDetailVO::getHouseName))
				.collect(Collectors.toList());
		for (VillageCommunityDetailVO vo : list) {
			//楼栋信息
			List<BuildingInfoVO> buildingInfoList = communtiy.getBuildingInfo();
			BuildingInfoVO buildingInfo = null;
			if(buildingInfoList.size() > 0) {
				buildingInfoList = buildingInfoList.stream().filter(p -> p.getBuildingId().equals(vo.getBuildingId()))
						.collect(Collectors.toList());
				if(CollUtil.isNotEmpty(buildingInfoList)) {
					buildingInfo = buildingInfoList.get(0);
				}
			}
			if(buildingInfo == null) {
				buildingInfo = new BuildingInfoVO();
				buildingInfo.setBuildingId(vo.getBuildingId());
				buildingInfo.setBuildingName(vo.getBuildingName());
				communtiy.getBuildingInfo().add(buildingInfo);
			}
			//单元信息
			List<UnitInfoVO> unitInfoList = buildingInfo.getUnitInfo();
			UnitInfoVO unitInfo = null;
			if(unitInfoList.size() > 0) {
				unitInfoList = unitInfoList.stream().filter(p -> p.getUnitId().equals(vo.getUnitId()))
						.collect(Collectors.toList());
				if(CollUtil.isNotEmpty(unitInfoList)) {
					unitInfo = unitInfoList.get(0);
				}
			}
			if(unitInfo == null) {
				unitInfo = new UnitInfoVO();
				unitInfo.setUnitId(vo.getUnitId());
				unitInfo.setUnitName(vo.getUnitName());
				buildingInfo.getUnitInfo().add(unitInfo);
			}
			//房屋信息
			List<HouseInfoVO> houseInfoList = unitInfo.getHouseInfo();
			HouseInfoVO houseInfo = null;
			if(houseInfoList.size() > 0) {
				houseInfoList = houseInfoList.stream().filter(p -> p.getHouseId().equals(vo.getHouseId()))
						.collect(Collectors.toList());
				if(CollUtil.isNotEmpty(houseInfoList)) {
					houseInfo = houseInfoList.get(0);
				}
			}
			if(houseInfo == null) {
				houseInfo = new HouseInfoVO();
				houseInfo.setHouseId(vo.getHouseId());
				houseInfo.setHouseName(vo.getHouseName());
				unitInfo.getHouseInfo().add(houseInfo);
			}
			//住户信息
			houseInfo.getPersonIdList().add(vo.getPersonId());
		}
		return communtiy;
	}

	private String getZhsqImgUrl(String communityId){
		List<AttachmentsDto> list = CommonUtils.getAttachmentBySourceId(getAttachmentBySourceIdUrl, communityId);
		String imgUrl = "";
		if(CollUtil.isNotEmpty(list)) {
			imgUrl = componentUtil.getServerUrlAddress() + list.get(0).getUrl();
		}
		return imgUrl;
	}
}
