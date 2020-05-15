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

import cn.citms.icw.entity.Building;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.CommunityDevice;
import cn.citms.icw.entity.Unit;
import cn.citms.icw.mapper.BuildingMapper;
import cn.citms.icw.mapper.CommunityDeviceMapper;
import cn.citms.icw.mapper.CommunityMapper;
import cn.citms.icw.mapper.UnitMapper;
import cn.citms.icw.service.ICommunityDeviceService;
import cn.citms.icw.vo.CommunityDeviceVO;
import cn.citms.icw.vo.DeviceRequestVO;
import cn.citms.icw.vo.DeviceVO;
import cn.citms.mbd.basicdatacache.service.IDeviceCacheService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 社区设备关联表 服务实现类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Service
public class CommunityDeviceServiceImpl extends ServiceImpl<CommunityDeviceMapper, CommunityDevice> implements ICommunityDeviceService {

	@Autowired
	private BaseService baseService;

	@Resource
	private BuildingMapper buildingMapper;
	@Resource
	private UnitMapper unitMapper;
	@Resource
	private CommunityMapper communityMapper;

	@Autowired
	private IDeviceCacheService deviceCacheService;

	@Override
	public IPage<CommunityDeviceVO> selectDevicePage(DeviceRequestVO vo) {
		int pageIndex = vo.getPageIndex() == null ? 1 : vo.getPageIndex();
		int pageSize = (vo.getPageSize() == null || vo.getPageSize() == 0) ? 20 : vo.getPageSize();
		IPage<CommunityDeviceVO> page = new Page<>(pageIndex, pageSize);
		//获取当前部门能够查看的社区数据
		List<String> departmentIds = baseService.getAllIdsByCommunityId(vo.getCommunityId());
		if(CollUtil.isNotEmpty(departmentIds)) {
			List<String> communityIds = communityMapper.selectCommunityIdByDeptIds(departmentIds);
			vo.setCommunityIds(communityIds);
		}
		List<CommunityDevice> devList = baseMapper.selectCommunityDevicePage(vo);
		if(CollUtil.isEmpty(devList)) {
			return page;
		}
		//去掉空字符串
		List<String> functionTypes = vo.getFunctionTypes();
		if(CollUtil.isNotEmpty(functionTypes)) {
			List<String> funcs = functionTypes.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
			vo.setFunctionTypes(funcs);
		}
		//查询设备信息
		List<CommunityDeviceVO> result = baseService.getDevice(vo);
		if(CollUtil.isNotEmpty(result)) {
			Map<String, CommunityDevice> map = CollUtil.newHashMap();
			for (CommunityDevice record : devList) {
				map.put(record.getDeviceId(), record);
			}
			List<CommunityDeviceVO> list = result.stream().filter(e -> map.keySet().contains(e.getDeviceNo())).collect(Collectors.toList());
			Set<String> arrId = new HashSet<>(list.size());
			Set<String> arrBuildId = new HashSet<>(list.size());
			Set<String> arrUnitId = new HashSet<>(list.size());
			for (CommunityDeviceVO deviceVO : list) {
				CommunityDevice device = map.get(deviceVO.getDeviceNo());
				deviceVO.setCommunityId(device.getCommunityId());
				deviceVO.setBuildId(device.getBuildId());
				deviceVO.setUnitId(device.getUnitId());
				deviceVO.setCommunityShow(device.getCommunityShow());

				if(StrUtil.isNotBlank(deviceVO.getCommunityId())) {
					arrId.add(deviceVO.getCommunityId());
				}
				if(StrUtil.isNotBlank(deviceVO.getBuildId())) {
					arrBuildId.add(deviceVO.getBuildId());
				}
				if(StrUtil.isNotBlank(deviceVO.getUnitId())) {
					arrUnitId.add(deviceVO.getUnitId());
				}
			}
			//社区名称/编码
			Map<String, String> dictCommunityMc = new HashMap<>(16);
			Map<String, String> dictCommunityBh = new HashMap<>(16);
			if(arrId.size() > 0) {
				List<Community> communityInfo = communityMapper.selectCommunityByIdIn(new ArrayList<>(arrId));
				if(CollUtil.isNotEmpty(communityInfo)) {
					for (Community community : communityInfo) {
						dictCommunityMc.put(community.getId(), community.getXqmc());
						dictCommunityBh.put(community.getId(), community.getXqbm());
					}
				}
			}
			//楼栋号名称
			Map<String, String> dictBuild = new HashMap<>(16);
			if(arrBuildId.size() > 0) {
				List<Building> buildingList = buildingMapper.selectBuildingByIdIn(arrBuildId);
				if(CollUtil.isNotEmpty(buildingList)) {
					dictBuild = buildingList.stream().collect(Collectors.toMap(Building::getId, Building::getLdh_Mc));
				}
			}
			//单元名称
			Map<String, String> dictUnit = new HashMap<>(16);
			if(arrUnitId.size() > 0) {
				List<Unit> unitList = unitMapper.selectUnitByIdIn(arrUnitId);
				if(CollUtil.isNotEmpty(unitList)) {
					dictUnit = unitList.stream().collect(Collectors.toMap(Unit::getId, Unit::getDy_Mc));
				}
			}

			for (CommunityDeviceVO deviceVO : list) {
				deviceVO.setSqmc(dictCommunityMc.get(deviceVO.getCommunityId()));
				deviceVO.setSqbm(dictCommunityBh.get(deviceVO.getCommunityId()));
				String buildName = dictBuild.get(deviceVO.getBuildId());
				String unitName = dictUnit.get(deviceVO.getUnitId());
				if(StrUtil.isNotBlank(buildName) && StrUtil.isNotBlank(unitName)) {
					deviceVO.setBuildUnitName(buildName + "-" + unitName);
				}
			}
			page.setRecords(list);
			page.setTotal(list.size());
		}
		return page;
	}

	@Override
	public R<CommunityDeviceVO> selectDeviceById(String id) {
		CommunityDevice communityDevice = baseMapper.selectByDeviceId(id);
		if(communityDevice == null) {
			return R.fail(200, "不存在id为【"+id+"】的设备信息");
		}
		JSONObject jsonObject = deviceCacheService.getDeviceJsonByNo(id);
		if(jsonObject == null) {
			return R.fail("查询失败");
		}
		CommunityDeviceVO vo = jsonObject.toJavaObject(CommunityDeviceVO.class);
		vo.setCommunityShow(communityDevice.getCommunityShow());
		vo.setBuildId(communityDevice.getBuildId());
		vo.setUnitId(communityDevice.getUnitId());
		vo.setCommunityId(communityDevice.getCommunityId());

		Community community = communityMapper.selectCommunityNameById(vo.getCommunityId());
		if(community != null) {
			vo.setSqmc(community.getXqmc());
			vo.setSqbm(community.getXqbm());
		}
		return R.data(vo);
	}

	@Override
	public List<CommunityDeviceVO> selectSomeDevice(List<String> ids) {
		ids = ids.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
		List<CommunityDevice> communityDeviceList = baseMapper.selectCommunityByIdIn(ids);
		if(CollUtil.isEmpty(communityDeviceList)) {
			return new ArrayList<>(0);
		}
		Map<String, CommunityDevice> dicDevice = new HashMap<>(communityDeviceList.size());
		List<String> communityIds = new ArrayList<>(communityDeviceList.size());
		for (CommunityDevice device : communityDeviceList) {
			dicDevice.put(device.getDeviceId(), device);
			communityIds.add(device.getCommunityId());
		}
		return deviceInfoSet(communityIds, ids, dicDevice);
	}

	private List<CommunityDeviceVO> deviceInfoSet(List<String> communityIds, List<String> ids,
												  Map<String, CommunityDevice> dicDevice){
		//社区
		Map<String, Community> dictCommunity = new HashMap<>(16);
		if(communityIds.size() > 0) {
			List<Community> communityInfo = communityMapper.selectCommunityByIdIn(communityIds);
			if(CollUtil.isNotEmpty(communityInfo)) {
				for (Community community : communityInfo) {
					dictCommunity.put(community.getId(), community);
				}
			}
		}
		//查询设备
		Map<String, JSONObject> map = deviceCacheService.getDeviceJsonMapByNoList(ids);
		Collection<JSONObject> jsonArr = map.values();
		List<CommunityDeviceVO> list = new ArrayList<>(jsonArr.size());
		for (JSONObject object : jsonArr) {
			CommunityDeviceVO vo = object.toJavaObject(CommunityDeviceVO.class);
			CommunityDevice dev = dicDevice.get(vo.getDeviceId());
			if(dev != null) {
				vo.setCommunityId(dev.getCommunityId());
				vo.setBuildId(dev.getBuildId());
				vo.setUnitId(dev.getUnitId());
				vo.setCommunityShow(dev.getCommunityShow());

				Community community = dictCommunity.get(dev.getCommunityId());
				if(community != null) {
					vo.setSqmc(community.getXqmc());
					vo.setSqbm(community.getXqbm());
				}
			}
			list.add(vo);
		}
		return list;
	}

	@Override
	public List<CommunityDeviceVO> selectAll() {
		List<CommunityDevice> list = baseMapper.selectAll();
		if(CollUtil.isEmpty(list)) {
			return new ArrayList<>(0);
		}
		List<String> ids = new ArrayList<>(list.size());
		Set<String> communityIds = new HashSet<>(list.size());
		Map<String, CommunityDevice> dicDevice = new HashMap<>(list.size());
		for (CommunityDevice device : list) {
			dicDevice.put(device.getDeviceId(), device);
			ids.add(device.getDeviceId());
			communityIds.add(device.getCommunityId());
		}
		return deviceInfoSet(new ArrayList<>(communityIds), ids, dicDevice);
	}

	@Override
	public List<CommunityDevice> getInfoByCommunityId(String id) {
		return baseMapper.getInfoByCommunityId(id);
	}

	@Override
	public R<String> saveDevice(DeviceVO device) {
		CommunityDevice communityDevice = baseMapper.selectByDeviceId(device.getDeviceId());
		int result;
		if(communityDevice == null) {
			CommunityDevice device1 = new CommunityDevice();
			device1.setDeviceId(device.getDeviceId());
			device1.setCommunityId(device.getCommunityId());
			device1.setBuildId(device.getBuildId());
			device1.setUnitId(device.getUnitId());
			device1.setCommunityShow(device.getCommunityShow());
			result = baseMapper.insert(device1);
		} else {
			communityDevice.setCommunityId(device.getCommunityId());
			communityDevice.setBuildId(device.getBuildId());
			communityDevice.setUnitId(device.getUnitId());
			communityDevice.setCommunityShow(device.getCommunityShow());
			result = baseMapper.updateById(communityDevice);
		}
		if(result > 0) {
			return R.data(device.getDeviceId());
		}
		return R.fail("保存失败");
	}

	@Override
	public R<String> deleteById(String deviceId) {
		CommunityDevice communityDevice = baseMapper.selectByDeviceId(deviceId);
		if(communityDevice == null) {
			return R.fail("不存在id【"+deviceId+"】的设备数据");
		}
		int result = baseMapper.deleteById(deviceId);
		return R.status(result > 0);
	}

	@Override
	public List<CommunityDevice> getByCommunityIdList(List<String> communityIds) {
		return baseMapper.getByCommunityIdList(communityIds);
	}
}
