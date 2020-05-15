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
package cn.citms.icw.service;

import cn.citms.icw.entity.Unit;
import cn.citms.icw.vo.UnitVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.common.utils.CommonException;
import org.springblade.core.tool.api.R;

import java.util.List;

/**
 * 单元信息 服务类
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface IUnitService extends IService<Unit> {

	/**
	 * 自定义分页
	 *
	 * @param unit
	 * @return
	 */
	IPage<UnitVO> selectUnitPage(UnitVO unit);

	/**
	 * 详情
	 * @param id
	 * @return
	 * @throws CommonException
	 */
	UnitVO findById(String id) throws CommonException;

	/**
	 * 查询所有单元信息接口
	 * @return
	 * @throws CommonException
	 */
	List<UnitVO> findAll() throws CommonException;

	/**
	 * 查询社区下的单元信息
	 * @param communityId
	 * @return
	 * @throws CommonException
	 */
	List<UnitVO> queryUnitByCommunityId(String communityId) throws CommonException;

	/**
	 * 新增或者修改单元信息
	 * @param unitVO
	 * @return
	 */
	boolean saveOrUpdateUnit(UnitVO unitVO) throws CommonException;

	/**
	 * 删除单元信息
	 * @param id
	 * @return
	 * @throws CommonException
	 */
	boolean deleteById(String id) throws CommonException;

	/**
	 *批量删除单元信息
	 * @param ids
	 * @return
	 * @throws CommonException
	 */
	R deleteByArrayIds(List<String> ids) throws CommonException;
}
