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
import cn.citms.icw.constant.IntelligentCommunityConstant;
import cn.citms.icw.dto.AttachmentsDto;
import cn.citms.icw.dto.PersoncheckinDTO;
import cn.citms.icw.dto.VehicleDTO;
import cn.citms.icw.entity.Building;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.House;
import cn.citms.icw.entity.Unit;
import cn.citms.icw.mapper.*;
import cn.citms.icw.service.IHouseService;
import cn.citms.icw.vo.*;
import cn.citms.mbd.basicdatacache.service.IDictionaryCacheService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 门户信息 服务实现类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements IHouseService {

	@Resource
	private CommunityMapper communityMapper;
	@Resource
	private BuildingMapper buildingMapper;
	@Resource
	private UnitMapper unitMapper;
	@Resource
	private PersoncheckinMapper personcheckinMapper;
	@Resource
	private VehicleMapper vehicleMapper;
	@Autowired
	private BaseService baseService;

	@Value("${dgaRpc.getAttachmentBySourceIdUrl}")
	private String getAttachmentBySourceIdUrl;

	@Autowired
	private IDictionaryCacheService dictionaryCacheService;

	@Override
	public IPage<HouseVO> selectManageQuery(HouseVO houseVO) {
		IPage<HouseVO> page = selectHousePage(houseVO);
		//查询住户数量
		List<SelectCntVO> list = personcheckinMapper.findGroupByHouseId2();
		if(CollUtil.isNotEmpty(list)) {
			int size = (int) (list.size() / 0.75 + 1);
			Map<String, Integer> map = new HashMap<>(size);
			Map<String, String> map2 = new HashMap<>(size);
			List<String> cardNos = new ArrayList<>(10);
			for (SelectCntVO vo : list) {
				map.put(vo.getName(), vo.getValue());
				map2.put(vo.getName(), vo.getZjhm());
				String zjhm = vo.getZjhm();
				if(StrUtil.isNotBlank(zjhm)) {
					String[] arr = zjhm.split(",");
					for (String s : arr) {
						map2.put(s, vo.getName());
						cardNos.add(s);
					}
				}
			}
			//关联车辆
			Map<String, Integer> vehicleMap = new HashMap<>(16);
			List<VehicleDTO> vehicleList = vehicleMapper.selectVehicleInCardNos(cardNos);
			if(CollUtil.isNotEmpty(vehicleList)) {
				for (VehicleDTO dto : vehicleList) {
					String pId = map2.get(dto.getIdCardNo());
					Integer cnt = vehicleMap.get(pId);
					cnt = cnt==null ? 1 : (cnt+1);
					vehicleMap.put(pId, cnt);
				}
			}

			for (HouseVO vo : page.getRecords()) {
				vo.setPersonCount(map.get(vo.getId())==null ? 0 : map.get(vo.getId()));
				vo.setVehicleCount(vehicleMap.get(vo.getId())==null ? 0 : vehicleMap.get(vo.getId()));
			}
		}
		return page;
	}

	private HouseVO findById(String id){
		House house = baseMapper.selectHouseById(id);
		if(house != null) {
			HouseVO vo =convert2Vm(house);
			getCommunityName(vo);
			getAttach(vo);
			return vo;
		}
		return null;
	}

	@Override
	public HouseVO manageFindById(String id) {
		HouseVO vo = findById(id);
		if(vo == null) {
			return null;
		}
		getCommunityName(vo);
		List<PersoncheckinDTO> perosonList = personcheckinMapper.manageFindById(id);
		if(CollUtil.isNotEmpty(perosonList)) {
			List<PersoncheckinVO> personcheckinVOList = new ArrayList<>(perosonList.size());
			for (PersoncheckinDTO dto : perosonList) {
				PersoncheckinVO checkVO = BeanUtil.copy(dto, PersoncheckinVO.class);
				checkVO.setDic_Mz(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.MZ), checkVO.getMzdm()));
				checkVO.setDic_Rylx(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.RYLX), checkVO.getRylx()));
				checkVO.setDic_Xb(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.XB), checkVO.getXbdm()));
				checkVO.setDic_Yhbs(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.YHBS), checkVO.getYhbs()));
				checkVO.setDic_Zjlx(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.ZJLX), checkVO.getCyzjlxdm()));
				personcheckinVOList.add(checkVO);
			}
			vo.setPersonList(personcheckinVOList);
			vo.setPersonCount(personcheckinVOList.size());

			List<String> cardNos = perosonList.stream().filter(e -> StrUtil.isNotBlank(e.getZjhm()))
					.map(PersoncheckinDTO::getZjhm).collect(Collectors.toList());
			if(CollUtil.isNotEmpty(cardNos)) {
				List<VehicleDTO> vehicleList = vehicleMapper.selectVehicleInCardNos(cardNos);
				if(CollUtil.isNotEmpty(vehicleList)) {
					List<VehicleVO> vehicleVOList = new ArrayList<>(vehicleList.size());
					for (VehicleDTO dto : vehicleList) {
						VehicleVO vehicleVO = BeanUtil.copy(dto, VehicleVO.class);
						vehicleVO.setPlateColorCn(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.HPYS), vehicleVO.getPlateColor()));
						vehicleVO.setPlateTypeCn(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.HPZL), vehicleVO.getPlateType()));
						vehicleVO.setVehicleBodyColorCn(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.CSYS), vehicleVO.getVehicleBodyColor()));
						vehicleVOList.add(vehicleVO);
					}
					vo.setVehicleList(vehicleVOList);
					vo.setVehicleCount(vehicleVOList.size());
				}
			}
		}
		return vo;
	}

	@Override
	public IPage<HouseVO> selectHousePage(HouseVO houseVO) {
		Integer pageIndex = houseVO.getPageIndex() == null ? 1 : houseVO.getPageIndex();
		Integer pageSize = (houseVO.getPageSize() == null || houseVO.getPageSize() == 0) ? 20 : houseVO.getPageSize();
		IPage<HouseVO> page = new Page<>(pageIndex, pageSize);
		//获取当前部门能够查看的社区数据
		if(StrUtil.isBlank(houseVO.getCommunity_Id())) {
			List<String> departmentIds = baseService.getPermissionDepartment();
			if(CollUtil.isNotEmpty(departmentIds)) {
				List<String> communityIds = communityMapper.selectCommunityIdByDeptIds(departmentIds);
				houseVO.setCommunityIds(communityIds);
			}
		}
		IPage<House> pages = baseMapper.selectManageQuery(page, houseVO);
		if(CollUtil.isNotEmpty(pages.getRecords())) {
			List<HouseVO> result = new ArrayList<>(pages.getRecords().size());
			List<String> ids = new ArrayList<>(pages.getRecords().size());
			List<String> ids2 = new ArrayList<>(pages.getRecords().size());
			Set<String> ids3 = new HashSet<>(pages.getRecords().size());
			Set<String> ids4 = new HashSet<>(pages.getRecords().size());
			for (House house : pages.getRecords()) {
				result.add(convert2Vm(house));
				ids.add(house.getCommunity_Id());
				ids2.add(house.getId());
				ids3.add(house.getBuilding_Id());
				ids4.add(house.getUnit_Id());
			}
			//社区名称 附件
			Map<String, List<AttachmentVO>> attachMap = baseService.getAttachmentBySourceIds(ids2);
			List<Community> communityList = communityMapper.selectCommunityByIdIn(ids);
			Map<String, String> map = new HashMap<>(16);
			Map<String, String> map2 = new HashMap<>(16);
			if(CollUtil.isNotEmpty(communityList)) {
				for (Community community : communityList) {
					map.put(community.getId(), community.getXqmc());
					map2.put(community.getId(), community.getXqdz());
				}
			}
			List<Building> buildingList = buildingMapper.selectBuildingByIdIn(ids3);
			List<Unit> unitList = unitMapper.selectUnitByIdIn(ids4);
			Map<String, String> map3 = buildingList.stream().collect(Collectors.toMap(Building::getId, Building::getLdh_Mc));
			Map<String, String> map4 = unitList.stream().collect(Collectors.toMap(Unit::getId, Unit::getDy_Mc));
			result.forEach(vo -> {
				vo.setFiles(attachMap.get(vo.getId()));
				vo.setSqmc(map.get(vo.getCommunity_Id()));
				vo.setSqdz(map2.get(vo.getCommunity_Id()));
				vo.setLdmc(map3.get(vo.getBuilding_Id()));
				vo.setDymc(map4.get(vo.getUnit_Id()));
			});
			page.setRecords(result);
		}
		return page;
	}

	@Override
	public List<HouseVO> selectAll() {
		List<House> list = baseMapper.selectAll();
		if(CollUtil.isNotEmpty(list)) {
			List<HouseVO> result = new ArrayList<>(list.size());
			List<String> ids = new ArrayList<>(list.size());
			 //单单从dictionaryCacheService的缓存中取数据，太慢。每次4ms，取1000次，也就4000ms，响应太慢
			 //所以这里全部一次拿出来，再进行取值会更快，通常总共只有100ms
			Map<Integer, Map<String, String>> findDict = baseService.findDict(Arrays.asList(IntelligentCommunityConstant.FWLX,IntelligentCommunityConstant.FWSYQLX,IntelligentCommunityConstant.FWZT,IntelligentCommunityConstant.ZJLX));
			list.parallelStream().forEach(house -> {
				result.add(convert2Vm(house, findDict));
				ids.add(house.getCommunity_Id());
			});
			//社区名称
			List<Community> communityList = communityMapper.selectCommunityByIdIn(ids);
			if(CollUtil.isNotEmpty(communityList)) {
				Map<String, String> map = new HashMap<>(16);
				Map<String, String> map2 = new HashMap<>(16);
				for (Community community : communityList) {
					map.put(community.getId(), community.getXqmc());
					map2.put(community.getId(), community.getXqdz());
				}
				result.forEach(vo -> {
					vo.setSqmc(map.get(vo.getCommunity_Id()));
					vo.setSqdz(map2.get(vo.getCommunity_Id()));
				});
			}
			return result;
		}
		return null;
	}

	@Override
	public HouseVO selectById(String id) {
		House house = baseMapper.selectHouseById(id);
		HouseVO vo = convert2Vm(house);
		getCommunityName(vo);
		getAttach(vo);
		return vo;
	}

	private void getCommunityName(HouseVO vo){
		//社区名称
		Community community = communityMapper.selectCommunityNameById(vo.getCommunity_Id());
		if(community != null) {
			vo.setSqmc(community.getXqmc());
			vo.setSqdz(community.getXqdz());
		}
	}

	private void getAttach(HouseVO vo){
		//附件
		List<AttachmentsDto> list = CommonUtils.getAttachmentBySourceId(getAttachmentBySourceIdUrl, vo.getId());
		if(CollUtil.isNotEmpty(list)) {
			List<AttachmentVO> voList = new ArrayList<>(list.size());
			for (AttachmentsDto att : list) {
				voList.add(BeanUtil.copy(att, AttachmentVO.class));
			}
			vo.setFiles(voList);
		}
	}

	@Override
	public R<String> saveHouse(HouseVO houseVO) {
		House house = BeanUtil.copy(houseVO, House.class);
		int result = 0;
		if(StrUtil.isNotBlank(houseVO.getId())){
			House buildingExist = baseMapper.selectById(houseVO.getId());
			if(buildingExist != null) {
				BeanUtil.copyProperties(house, buildingExist);
				result = baseMapper.updateById(buildingExist);
			} else {
				return R.fail("不存在id为【"+houseVO.getId()+"】的门户数据");
			}
		}
		if(result == 0) {
			house.setId(CommonUtils.createUUID());
			result = baseMapper.insert(house);
		}
		if(result>0) {
			List<AttachmentVO> attachmentVOList = houseVO.getFiles();
			if(attachmentVOList == null) {
				attachmentVOList = new ArrayList<>(0);
			}
			//UpdateOrRemove
			baseService.attachmentUpdateOrRemove(house.getId(),
					attachmentVOList.stream().map(AttachmentVO::getAttachmentId).collect(Collectors.toList()));
			return R.data(house.getId());
		} else {
			return R.fail("数据操作异常");
		}
	}

	@Override
	public String deleteByBuildingIds(List<String> ids) {
		int count = 0, delTotal = 0;
		for (String id : ids) {
			Integer checkIn = personcheckinMapper.selectCountBandingPersoncheckinById(id);
			if(checkIn > 0) {
				count++;
			} else {
				int del = baseMapper.deleteById(id);
				if(del > 0) {
					delTotal++;
				}
			}
		}
		String msg = "共" + ids.size() + "条数据，成功删除【" + delTotal + "】条";
		if (count > 0) {
			msg += ",【" + count + "】条绑定住户信息，无法删除";
		}
		return msg;
	}

	@Override
	public Boolean bandingHouseId(String id) {
		int cnt = personcheckinMapper.selectCountBandingPersoncheckinById(id);
		return cnt > 0;
	}

    @Override
    public double countHouse(String communityId) {
		return baseMapper.countHouse(communityId);
    }

	@Override
	public Map<String, Integer> getHouseZtCnt(List<String> communityIds) {
		if(CollUtil.isEmpty(communityIds)) {
			return new HashMap<>(0);
		}
		List<SelectCntVO> list = baseMapper.getHouseZtCnt(communityIds);
		if(CollUtil.isEmpty(list)) {
			return new HashMap<>(0);
		}
		Map<String, Integer> resMap = new HashMap<>(list.size() * 2);
		for (SelectCntVO vo : list) {
			resMap.put(vo.getName(), vo.getValue());
		}
		return resMap;
	}

	@Override
	public Integer getHouseCnt(List<String> communityIds) {
		if(CollUtil.isEmpty(communityIds)) {
			return 0;
		}
		return baseMapper.getHouseCnt(communityIds);
	}

	private HouseVO convert2Vm(House house){
		if(house == null) {
			return null;
		}
		HouseVO vo = BeanUtil.copy(house, HouseVO.class);
		vo.setDic_Fwlx(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.FWLX), vo.getFw_Lx()));
		vo.setDic_Fwsyq(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.FWSYQLX), vo.getSyq_Lx()));
		vo.setDic_Fwzt(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.FWZT), vo.getFw_Zt()));
		vo.setDic_Zjlx(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.ZJLX), vo.getWtdlr_Zjlxmc()));
		return vo;
	}

	private HouseVO convert2Vm(House house, Map<Integer, Map<String, String>> findDict){
		if(house == null) {
			return null;
		}
		HouseVO vo = BeanUtil.copy(house, HouseVO.class);
		Map<String, String> map1 = findDict.get(IntelligentCommunityConstant.FWLX);
		if(map1 != null && StrUtil.isNotBlank(vo.getFw_Lx())) {
			vo.setDic_Fwlx(map1.get(vo.getFw_Lx()));
		}
		Map<String, String> map2 = findDict.get(IntelligentCommunityConstant.FWSYQLX);
		if(map2 != null && StrUtil.isNotBlank(vo.getSyq_Lx())) {
			vo.setDic_Fwsyq(map2.get(vo.getSyq_Lx()));
		}
		Map<String, String> map3 = findDict.get(IntelligentCommunityConstant.FWZT);
		if(map3 != null && StrUtil.isNotBlank(vo.getFw_Zt())) {
			vo.setDic_Fwzt(map3.get(vo.getFw_Zt()));
		}
		Map<String, String> map4 = findDict.get(IntelligentCommunityConstant.ZJLX);
		if(map4 != null && StrUtil.isNotBlank(vo.getWtdlr_Zjlxmc())) {
			vo.setDic_Zjlx(map4.get(vo.getWtdlr_Zjlxmc()));
		}
		return vo;
	}

}
