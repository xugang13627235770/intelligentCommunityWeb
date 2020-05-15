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

import cn.citms.icw.entity.House;
import cn.citms.icw.vo.HouseVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.core.tool.api.R;

import java.util.List;
import java.util.Map;

/**
 * 门户信息 服务类
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface IHouseService extends IService<House> {

	/**
	 * 管理员查询接口
	 */
	IPage<HouseVO> selectManageQuery(HouseVO houseVO);

	/**
	 * 获取指定门户信息
	 * @param id
	 * @return
	 */
	HouseVO manageFindById(String id);

	/**
	 * 查询接口
	 */
	IPage<HouseVO> selectHousePage(HouseVO houseVO);

	/**
	 * 查询所有门户信息接口
	 */
	List<HouseVO> selectAll();

	/**
	 * 获取指定门户信息
	 */
	HouseVO selectById(String id);

	/**
	 * 保存门户信息
	 */
	R<String> saveHouse(HouseVO houseVO);

	/**
	 * 批量删除门户信息
	 */
	String deleteByBuildingIds(List<String> ids);

	/**
	 * 查询门户信息是否已经绑定了住户数据
	 */
	Boolean bandingHouseId(String id);

    double countHouse(String communityId);

	/**
	 * 房屋状态-数量统计
	 * @return
	 */
	Map<String, Integer> getHouseZtCnt(List<String> communityIds);

	/**
	 * 查询数量
	 * @param communityIds
	 * @return
	 */
	Integer getHouseCnt(List<String> communityIds);
}
