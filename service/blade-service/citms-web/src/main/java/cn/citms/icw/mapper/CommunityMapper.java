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

import cn.citms.icw.dto.CommonCountDTO;
import cn.citms.icw.entity.Community;
import cn.citms.icw.vo.CommunityVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 社区信息 Mapper 接口
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface CommunityMapper extends BaseMapper<Community> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param community
	 * @return
	 */
	List<Community> selectCommunityPage(IPage page, @Param("vo") CommunityVO community);

    List<Community> findAll();

	/**
	 * 查询有权限的社区ids
	 * @param departmentIds
	 * @return
	 */
	List<String> selectCommunityIdByDeptIds(@Param("ids") List<String> departmentIds);

	List<Community> selectCommunityInfoByDeptIds(@Param("ids") List<String> departmentIds);

	List<String> selectCommunityNoByDeptIds(@Param("ids") List<String> departmentIds);

	List<Community> selectCommunityByIdIn(@Param("ids") List<String> ids);

	Community selectCommunityNameById(String id);

    List<CommonCountDTO> countCommunityTypeByCommuityIds(@Param("ids") List<String> communityIds);

	List<CommonCountDTO> countBasicCount(@Param("ids") List<String> communityIds);

	List<String> selectCommunityNameBySqmc(@Param("list") List<String> list);
	List<String> selectCommunityBmBySqmc(@Param("list") List<String> list);

	void batchInsert(@Param("list") List<Community> list);
}
