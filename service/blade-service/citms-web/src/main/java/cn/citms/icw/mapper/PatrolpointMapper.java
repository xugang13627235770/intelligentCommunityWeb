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

import cn.citms.icw.entity.Patrolpoint;
import cn.citms.icw.vo.PatrolpointVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface PatrolpointMapper extends BaseMapper<Patrolpoint> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param patrolpoint
	 * @return
	 */
	List<PatrolpointVO> selectPatrolpointPage(@Param("page")IPage page, @Param("vo")PatrolpointVO patrolpoint);

	Integer getCountByCommunityIds(@Param("ids") List<String> communityIds);

	List<Patrolpoint> selectByCommunityIds(@Param("ids") List<String> communityIds);

	void batchInsert(@Param("list") List<Patrolpoint> list);
}
