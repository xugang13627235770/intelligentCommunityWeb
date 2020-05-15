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
package cn.citms.icw.mapper;

import cn.citms.icw.entity.Building;
import cn.citms.icw.vo.BuildingVO;
import cn.citms.icw.vo.VillageCommunityDetailVO;
import cn.citms.icw.vo.VillageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 楼栋信息 Mapper 接口
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface BuildingMapper extends BaseMapper<Building> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param building
	 * @return
	 */
	IPage<Building> selectBuildingPage(IPage page, @Param("building") BuildingVO building);

	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	Building selectBuildingById(String id);

	/**
	 * 查询所有
	 * @return
	 */
	List<Building> selectAll();

	/**
	 * 查询社区是否绑定了楼栋信息
	 * @param id
	 * @return
	 */
	Integer selectCountBandingCommunityId(String id);

    List<Building> getByCommunityId(@Param("id") String id);

	List<Building> selectBuildingByIdIn(@Param("ids") Set<String> ids);

	/**
	 * 查询社区各类基础数据数量
	 * @return
	 */
	List<VillageVO> selectVillageInfo(@Param("ids") List<String> depts);
	/**
	 * 根据小区id获取小区详情
	 * @return
	 */
	List<VillageCommunityDetailVO> selectCommunityDetail(String communityId);

	List<Building> selectByCommunityIds(@Param("ids") List<String> list);

	void batchInsert(@Param("list") List<Building> list);
}
