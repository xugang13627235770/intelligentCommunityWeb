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
import cn.citms.icw.dto.PatroltaskDTO;
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.CommunityDevice;
import cn.citms.icw.entity.Patroltask;
import cn.citms.icw.mapper.PatroltaskMapper;
import cn.citms.icw.service.ICommunityDeviceService;
import cn.citms.icw.service.ICommunityService;
import cn.citms.icw.service.IPatroltaskService;
import cn.citms.icw.vo.PatroltaskVO;
import cn.citms.icw.wrapper.PatroltaskWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.utils.CommonException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Service
public class PatroltaskServiceImpl extends ServiceImpl<PatroltaskMapper, Patroltask> implements IPatroltaskService {

	@Autowired
	private ICommunityService communityService;
	@Autowired
	private ICommunityDeviceService communityDeviceService;

	@Override
	public IPage<PatroltaskVO> selectPatroltaskPage(PatroltaskVO patroltask) {
		Integer pageIndex = patroltask.getPageindex() == null ? 1 : patroltask.getPageindex();
		Integer pageSize = (patroltask.getPagesize() == null || patroltask.getPagesize() == 0) ? 20 : patroltask.getPagesize();
		IPage<PatroltaskVO> page = new Page<>(pageIndex, pageSize);

		return page.setRecords(baseMapper.selectPatroltaskPage(page, patroltask));
	}

	/**
	 * 查询详情
	 * @return
	 */
	@Override
	public PatroltaskVO detail(String id) {
		Patroltask patroltask = baseMapper.selectById(id);
		if(patroltask == null){
			return null;
		}

		PatroltaskVO patroltaskVO  = PatroltaskWrapper.build().entityVO(patroltask);
		Community community = communityService.getById(patroltask.getCommunityId());
		if(community == null){
			return null;
		}
		patroltaskVO.setSqmc(community.getXqmc());
		return patroltaskVO;
	}

	/**
	 * 新增或者修改人员分配
	 * @param patroltaskDTO
	 * @return
	 */
	@Override
	public boolean saveOrUpdatePatroltask(PatroltaskDTO patroltaskDTO) throws CommonException {
		if(StringUtils.isEmpty(patroltaskDTO.getDeviceNo())){
			throw new CommonException(null, "设备编号不能为空");
		}
		if(StringUtils.isEmpty(patroltaskDTO.getCommunityId())){
			throw new CommonException(null, "社区ID不能为空");
		}

		if(StringUtils.isEmpty(patroltaskDTO.getId())){
			return savePatrolTask(patroltaskDTO);
		}else{
			return updatePatrolTask(patroltaskDTO);
		}
	}

	/**
	 * 新增人员分配
	 * @param patroltaskDTO
	 * @return
	 * @throws CommonException
	 */
	private boolean savePatrolTask(PatroltaskDTO patroltaskDTO) throws CommonException {
//		if(!isExistsDevice(patroltaskDTO.getDeviceNo(), patroltaskDTO.getCommunityId())){
//			throw new CommonException(null, "该设备编号【"+patroltaskDTO.getDeviceNo()+"】不存在社区设备中");
//		}

		Patroltask patrolTaskCon = new Patroltask();
		patrolTaskCon.setDeviceNo(patroltaskDTO.getDeviceNo());
		patrolTaskCon.setCommunityId(patroltaskDTO.getCommunityId());
		Patroltask patrolTaskResult = baseMapper.selectOne(Condition.getQueryWrapper(patrolTaskCon));
		if(patrolTaskResult != null){
			throw new CommonException(null, "当前小区已存在设备编号为【"+patroltaskDTO.getDeviceNo()+"】的巡更任务");
		}

		Patroltask patroltask = BeanUtil.copy(patroltaskDTO, Patroltask.class);
		patroltask.setId(CommonUtils.createUUID());
		int num = baseMapper.insert(patroltask);
		return num > 0;
	}

	private boolean updatePatrolTask(PatroltaskDTO patroltaskDTO) throws CommonException {
//		if(!isExistsDevice(patroltaskDTO.getDeviceNo(), patroltaskDTO.getCommunityId())){
//			throw new CommonException(null, "该设备编号【"+patroltaskDTO.getDeviceNo()+"】不存在社区设备中");
//		}

		Patroltask patrolTaskCon = new Patroltask();
		patrolTaskCon.setDeviceNo(patroltaskDTO.getDeviceNo());
		patrolTaskCon.setCommunityId(patroltaskDTO.getCommunityId());
		Patroltask patrolTaskResult = baseMapper.selectOne(Condition.getQueryWrapper(patrolTaskCon));
		if(patrolTaskResult != null && !patroltaskDTO.getId().equals(patrolTaskResult.getId())){
			throw new CommonException(null, "当前小区已存在设备编号为【"+patroltaskDTO.getDeviceNo()+"】的巡更任务");
		}

		Patroltask patroltask = BeanUtil.copy(patroltaskDTO, Patroltask.class);
		int num = baseMapper.updateById(patroltask);
		return num>0 ;
	}

	private boolean isExistsDevice(String deviceNo, String communityId){
		if(StringUtils.isEmpty(deviceNo)){
			return false;
		}
		CommunityDevice communityDeviceCon = new CommunityDevice();
		communityDeviceCon.setDeviceId(deviceNo);
		communityDeviceCon.setCommunityId(communityId);
		int count = communityDeviceService.count(Condition.getQueryWrapper(communityDeviceCon));
		return count>0;
	}

}
