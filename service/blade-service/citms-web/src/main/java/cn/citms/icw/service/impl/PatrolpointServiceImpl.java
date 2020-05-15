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
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.Patrolpoint;
import cn.citms.icw.mapper.PatrolpointMapper;
import cn.citms.icw.service.ICommunityService;
import cn.citms.icw.service.IPatrolpointService;
import cn.citms.icw.vo.PatrolpointVO;
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
public class PatrolpointServiceImpl extends ServiceImpl<PatrolpointMapper, Patrolpoint> implements IPatrolpointService {

	@Autowired
	private ICommunityService communityService;

	@Override
	public IPage<PatrolpointVO> selectPatrolpointPage(PatrolpointVO patrolpoint) {
		Integer pageIndex = patrolpoint.getPageindex() == null ? 1 : patrolpoint.getPageindex();
		Integer pageSize = (patrolpoint.getPagesize() == null || patrolpoint.getPagesize() == 0) ? 20 : patrolpoint.getPagesize();
		IPage<PatrolpointVO> page = new Page<>(pageIndex, pageSize);

		return page.setRecords(baseMapper.selectPatrolpointPage(page, patrolpoint));
	}

	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Override
	public PatrolpointVO detail(String id) throws CommonException {
		if(StringUtils.isEmpty(id)){
			throw new CommonException(null, "参数异常");
		}
		Patrolpoint patrolpoint = baseMapper.selectById(id);
		if(patrolpoint == null){
			return null;
		}

		Community community = communityService.getById(patrolpoint.getCommunityId());
		if(community == null){
			return null;
		}
		PatrolpointVO patrolpointVO  = BeanUtil.copy(patrolpoint, PatrolpointVO.class);
		patrolpointVO.setSqmc(community.getXqmc());
		return patrolpointVO;
	}

	/**
	 * 新增或者修改
	 * @param patrolpointVO
	 * @return
	 */
	@Override
	public boolean saveOrUpdatePatrolpoint(PatrolpointVO patrolpointVO) throws CommonException {
		if(StringUtils.isEmpty(patrolpointVO.getCommunityId())){
			throw new CommonException(null, "社区不能为空");
		}
		if(StringUtils.isEmpty(patrolpointVO.getPointNumber())){
			throw new CommonException(null, "巡更点编号不能为空");
		}

		if(StringUtils.isEmpty(patrolpointVO.getId())){
			return savePatrolpoint(patrolpointVO);
		}else{
			return updatePatrolpoint(patrolpointVO);
		}
	}

	/**
	 * 新增巡更点
	 * @param patrolpointVO
	 * @return
	 * @throws CommonException
	 */
	private boolean savePatrolpoint(PatrolpointVO patrolpointVO) throws CommonException {
		Patrolpoint patrolpointCon = new Patrolpoint();
		patrolpointCon.setCommunityId(patrolpointVO.getCommunityId());
		patrolpointCon.setPointNumber(patrolpointVO.getPointNumber());
		Patrolpoint patrolpointResult = baseMapper.selectOne(Condition.getQueryWrapper(patrolpointCon));
		if(patrolpointResult != null){
			throw new CommonException(null, "该小区存在重复巡更点编号为【"+patrolpointVO.getPointNumber()+"】的巡更点信息");
		}

		Patrolpoint patrolpoint = BeanUtil.copy(patrolpointVO, Patrolpoint.class);
		patrolpoint.setId(CommonUtils.createUUID());
		int num = baseMapper.insert(patrolpoint);
		return num > 0;
	}

	/**
	 * 修改巡更点
	 * @param patrolpointVO
	 * @return
	 * @throws CommonException
	 */
	private boolean updatePatrolpoint(PatrolpointVO patrolpointVO) throws CommonException {
		Patrolpoint patrolpointCon = new Patrolpoint();
		patrolpointCon.setCommunityId(patrolpointVO.getCommunityId());
		patrolpointCon.setPointNumber(patrolpointVO.getPointNumber());
		Patrolpoint patrolpointResult = baseMapper.selectOne(Condition.getQueryWrapper(patrolpointCon));
		if(patrolpointResult != null && !patrolpointVO.getId().equals(patrolpointResult.getId())){
			throw new CommonException(null, "该小区存在重复巡更点编号为【"+patrolpointVO.getPointNumber()+"】的巡更点信息");
		}

		Patrolpoint patrolpoint = BeanUtil.copy(patrolpointVO, Patrolpoint.class);
		int num = baseMapper.updateById(patrolpoint);
		return num>0 ;
	}

}
