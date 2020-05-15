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
import cn.citms.icw.entity.Building;
import cn.citms.icw.entity.Community;
import cn.citms.icw.mapper.BuildingMapper;
import cn.citms.icw.mapper.CommunityMapper;
import cn.citms.icw.mapper.HouseMapper;
import cn.citms.icw.mapper.UnitMapper;
import cn.citms.icw.service.IBuildingService;
import cn.citms.icw.vo.AttachmentVO;
import cn.citms.icw.vo.BuildingVO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 楼栋信息 服务实现类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements IBuildingService {

	@Autowired
	private BaseService baseService;

	@Value("${dgaRpc.getAttachmentBySourceIdUrl}")
	private String getAttachmentBySourceIdUrl;

	@Resource
	private CommunityMapper communityMapper;
	@Resource
	private UnitMapper unitMapper;
	@Resource
	private HouseMapper houseMapper;

	@Autowired
	private IDictionaryCacheService dictionaryCacheService;

	@Override
	public IPage<BuildingVO> selectBuildingPage(BuildingVO building) {
		Integer pageIndex = building.getPageIndex() == null ? 1 : building.getPageIndex();
		Integer pageSize = (building.getPageSize() == null || building.getPageSize() == 0) ? 20 : building.getPageSize();
		IPage<BuildingVO> page = new Page<>(pageIndex, pageSize);
		//获取当前部门能够查看的社区数据
		List<String> departmentIds = baseService.getAllIdsByCommunityId(building.getCommunity_Id());
		if(CollUtil.isNotEmpty(departmentIds)) {
			List<String> communityIds = communityMapper.selectCommunityIdByDeptIds(departmentIds);
			building.setCommunityIds(communityIds);
		}
		IPage<Building> list = baseMapper.selectBuildingPage(page, building);
		if(CollUtil.isNotEmpty(list.getRecords())) {
			List<BuildingVO> result = new ArrayList<>(list.getRecords().size());
			SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
			List<String> ids = new ArrayList<>(list.getRecords().size());
			List<String> ids2 = new ArrayList<>(list.getRecords().size());
			for (Building dto : list.getRecords()) {
				ids.add(dto.getCommunity_Id());
				ids2.add(dto.getId());
				BuildingVO vo = convert2VO(dto);
				if(vo.getCjsj() != null) {
					vo.setDic_Cjsj(dtf.format(vo.getCjsj()));
				}
				result.add(vo);
			}
			//社区名称 附件
			Map<String, List<AttachmentVO>> attachMap = baseService.getAttachmentBySourceIds(ids2);
			Map<String, String> map = getCommunityNames(ids);
			result.forEach(vo -> {
				vo.setFiles(attachMap.get(vo.getId()));
				vo.setSqmc(map.get(vo.getCommunity_Id()));
			});
			page.setRecords(result);
		}
		return page;
	}

	@Override
	public BuildingVO selectById(String id) {
		Building dto = baseMapper.selectBuildingById(id);
		if(dto == null) {
			return null;
		}
		BuildingVO vo = convert2VO(dto);
		// 社区名称
		Community community = communityMapper.selectCommunityNameById(vo.getCommunity_Id());
		if(community != null) {
			vo.setSqmc(community.getXqmc());
		}
		//附件
		List<AttachmentsDto> list = CommonUtils.getAttachmentBySourceId(getAttachmentBySourceIdUrl, vo.getId());
		if(CollUtil.isNotEmpty(list)) {
			List<AttachmentVO> voList = new ArrayList<>(list.size());
			for (AttachmentsDto att : list) {
				voList.add(BeanUtil.copy(att, AttachmentVO.class));
			}
			vo.setFiles(voList);
		}
		return vo;
	}

	@Override
	public List<BuildingVO> selectAll() {
		List<Building> dtoList = baseMapper.selectAll();
		if(CollUtil.isNotEmpty(dtoList)) {
			List<BuildingVO> voList = new ArrayList<>(dtoList.size());
			List<String> ids = new ArrayList<>(dtoList.size());
			for (Building dto : dtoList) {
				voList.add(convert2VO(dto));
				ids.add(dto.getCommunity_Id());
			}
			//社区
			Map<String, String> map = getCommunityNames(ids);
			voList.forEach(vo -> vo.setSqmc(map.get(vo.getCommunity_Id())));
			return voList;
		}
		return new ArrayList<>(0);
	}

	@Override
	public R<String> saveBuilding(BuildingVO buildingVO) {
		Building building = BeanUtil.copy(buildingVO, Building.class);
		int result = 0;
		if(StrUtil.isNotBlank(building.getId())){
			Building buildingExist = baseMapper.selectById(buildingVO.getId());
			if(buildingExist != null) {
				BeanUtil.copyProperties(building, buildingExist);
				result = baseMapper.updateById(buildingExist);
			} else {
				return R.fail("不存在id为【"+building.getId()+"】的楼栋数据");
			}
		}
		if(result == 0) {
			building.setId(CommonUtils.createUUID());
			result = baseMapper.insert(building);
		}
		if(result>0) {
			List<AttachmentVO> attachmentVOList = buildingVO.getFiles();
			if(attachmentVOList == null) {
				attachmentVOList = new ArrayList<>(0);
			}
			//UpdateOrRemove
			baseService.attachmentUpdateOrRemove(building.getId(),
					attachmentVOList.stream().map(AttachmentVO::getAttachmentId).collect(Collectors.toList()));
			return R.data(building.getId());
		} else {
			return R.fail("数据操作异常");
		}
	}

	@Override
	public String deleteByBuildingIds(List<String> ids) {
		int count = 0, delTotal = 0;
		for (String id : ids) {
			Integer unitCnt = unitMapper.selectCountBandingUnitId(id);
			Integer houseCnt = houseMapper.selectCountBandingHouseId(id);
			if(unitCnt > 0 || houseCnt > 0) {
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
			msg += ",【" + count + "】条绑定单元或门户信息，无法删除";
		}
		return msg;
	}

	@Override
	public Boolean bandingCommunityId(String id) {
		int cnt = baseMapper.selectCountBandingCommunityId(id);
		return cnt > 0;
	}

	@Override
	public Boolean bandingUnitId(String id) {
		int unitCnt = unitMapper.selectCountBandingUnitId(id);
		Integer houseCnt = houseMapper.selectCountBandingHouseId(id);
		return unitCnt > 0 || houseCnt > 0;
	}

	/**
	 * 查询社区的名称
	 * @param ids
	 * @return
	 */
	private Map<String, String> getCommunityNames(List<String> ids){
		List<Community> communityList = communityMapper.selectCommunityByIdIn(ids);
		Map<String, String> map = new HashMap<>(16);
		if(CollUtil.isNotEmpty(communityList)) {
			for (Community community : communityList) {
				map.put(community.getId(), community.getXqmc());
			}
		}
		return map;
	}

	private BuildingVO convert2VO(Building building) {
		BuildingVO vo = BeanUtil.copy(building, BuildingVO.class);
		//字典转换
		vo.setDic_Jzxz(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.JZXZ), vo.getJz_Xz()));
		return vo;
	}
}
