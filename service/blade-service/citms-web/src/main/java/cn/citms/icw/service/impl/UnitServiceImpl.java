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
import cn.citms.icw.entity.House;
import cn.citms.icw.entity.Unit;
import cn.citms.icw.mapper.UnitMapper;
import cn.citms.icw.service.IHouseService;
import cn.citms.icw.service.IUnitService;
import cn.citms.icw.vo.AttachmentVO;
import cn.citms.icw.vo.UnitVO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.utils.CommonException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 单元信息 服务实现类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements IUnitService {

	@Autowired
	private IHouseService houseService;
	@Resource
	private BaseService baseService;

	@Value("${dgaRpc.getAttachmentBySourceIdUrl}")
	private String getAttachmentBySourceIdUrl;

	@Override
	public IPage<UnitVO> selectUnitPage(UnitVO unit) {
		Integer pageIndex = unit.getPageindex() == null ? 1 : unit.getPageindex();
		Integer pageSize = (unit.getPagesize() == null || unit.getPagesize() == 0) ? 20 : unit.getPagesize();
		IPage<UnitVO> page = new Page<>(pageIndex, pageSize);

		List<UnitVO> unitList = baseMapper.selectUnitPage(page, unit);
		convertAttachment(unitList);
		return page.setRecords(unitList);
	}

	/**
	 * 获取指定单元信息
	 * @param id
	 * @return
	 * @throws CommonException
	 */
	@Override
	public UnitVO findById(String id) throws CommonException {
		if(StringUtils.isEmpty(id)){
			throw new CommonException(null, "参数异常");
		}

		List<UnitVO> unitList = findUnit(id, null);
		if(CollectionUtils.isEmpty(unitList)){
			return null;
		}
		convertAttachment(unitList);
		return unitList.get(0);
	}

	/**
	 * 查询所有单元信息接口
	 * @return
	 * @throws CommonException
	 */
	@Override
	public List<UnitVO> findAll() throws CommonException {
		return findUnit(null, null);
	}

	/**
	 * 查询社区下的单元信息
	 * @param communityId
	 * @return
	 * @throws CommonException
	 */
	@Override
	public List<UnitVO> queryUnitByCommunityId(String communityId) throws CommonException {
		if(StringUtils.isEmpty(communityId)){
			throw new CommonException(null, "参数异常");
		}
		return findUnit(null, communityId);
	}

	private List<UnitVO> findUnit(String id, String communityId){
		List<UnitVO> unitList = baseMapper.findUnit(id, communityId);
		composeBuildUnitName(unitList);
		return unitList;
	}

	private void convertAttachment(List<UnitVO> unitList){
		if(CollectionUtils.isEmpty(unitList)){
			return;
		}

		//附件信息
		List<String> ids = unitList.stream().map(UnitVO::getId).collect(toList());
		Map<String, List<AttachmentVO>> filesMap = baseService.getAttachmentBySourceIds(ids);

		for(UnitVO unitVO : unitList){
			// 获取附件信息
			if(filesMap != null){
				unitVO.setFiles(filesMap.get(unitVO.getId()));
			}
		}
	}

	/**
	 * 楼栋单元名称
	 * @param unitList
	 */
	private void composeBuildUnitName(List<UnitVO> unitList){
		if(CollectionUtils.isEmpty(unitList)){
			return;
		}

		for(UnitVO unitVO : unitList){
			//楼栋单元名称
			if(StringUtils.isNotEmpty(unitVO.getLdmc()) && StringUtils.isNotEmpty(unitVO.getDy_Mc())){
				unitVO.setBuildUnitName(unitVO.getLdmc()+"-"+unitVO.getDy_Mc());
			}
		}
	}

	/**
	 * 新增或者修改单元信息
	 * @param unitVO
	 * @return
	 */
	@Override
	public boolean saveOrUpdateUnit(UnitVO unitVO) throws CommonException {
		if(StringUtils.isEmpty(unitVO.getCommunity_Id())){
			throw new CommonException(null, "社区不能为空");
		}
		if(StringUtils.isEmpty(unitVO.getBuilding_Id())){
			throw new CommonException(null, "楼栋信息不能为空");
		}
		if(StringUtils.isEmpty(unitVO.getDy_Mc())){
			throw new CommonException(null, "单元名称不能为空");
		}

		if(StringUtils.isEmpty(unitVO.getId())){
			return saveUnit(unitVO);
		}else{
			return updateUnit(unitVO);
		}
	}

	/**
	 * 保存单元信息
	 * @param unitVO
	 */
	private boolean saveUnit(UnitVO unitVO){
		Unit unit = BeanUtil.copy(unitVO, Unit.class);
		unit.setId(CommonUtils.createUUID());
		int num = baseMapper.insert(unit);
		//  维护附件关联关系的逻辑
		composeAttachment(unitVO, unit.getId());
		return num > 0;
	}

	/**
	 * 修改单元信息
	 * @param unitVO
	 * @return
	 */
	private boolean updateUnit(UnitVO unitVO){
		Unit unit = BeanUtil.copy(unitVO, Unit.class);
		int num = baseMapper.updateById(unit);
		//  维护附件关联关系的逻辑
		composeAttachment(unitVO, unit.getId());
		return num>0 ;
	}

	//维护附件关联关系的逻辑
	private void composeAttachment(UnitVO unitVO, String id){
		if(CollectionUtils.isNotEmpty(unitVO.getFiles())){
			List<String> attachmentIds = unitVO.getFiles().stream().map(AttachmentVO::getAttachmentId).collect(Collectors.toList());
			boolean attachmentUpdateOrRemoveFlag = baseService.attachmentUpdateOrRemove(id, attachmentIds);
			if(!attachmentUpdateOrRemoveFlag){
				log.error("维护附件信息失败:" + JSON.toJSONString(unitVO));
			}
		}
	}

	/**
	 * 删除单元信息
	 * @param id
	 * @return
	 * @throws CommonException
	 */
	@Override
	public boolean deleteById(String id) throws CommonException {
		if(checkUnitHouse(id)){
			throw new CommonException(null, " 该单元已绑定门户信息");
		}
		return baseMapper.deleteById(id)>0;
	}

	/**
	 * true:已经绑定了住户信息
	 * @param unitId
	 * @return
	 */
	private boolean checkUnitHouse(String unitId){
		House houseCon = new House();
		houseCon.setUnit_Id(unitId);
		int num = houseService.count(Condition.getQueryWrapper(houseCon));
		return num > 0;
	}

	/**
	 *批量删除单元信息
	 * @param ids
	 * @return
	 * @throws CommonException
	 */
	@Override
	public R deleteByArrayIds(List<String> ids) throws CommonException {
		if(CollectionUtils.isEmpty(ids)){
			throw new CommonException(null, " 参数异常");
		}

		int delTotal = 0;
		int count = 0;
		for (String id : ids) {
			if(StringUtils.isNotBlank(id)){
				if(checkUnitHouse(id)){
					count++;
				}else{
					baseMapper.deleteById(id);
					delTotal ++;
				}
			}
		}
		//;共3条数据，成功删除【0】条,【3】条绑定门户信息，无法删除
		return R.success("共" + ids.size() + "条数据，成功删除【" + delTotal + "】条"+",【" + count + "】条绑定门户信息，无法删除");
	}
}
