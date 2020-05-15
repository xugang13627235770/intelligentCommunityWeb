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

import cn.citms.icw.entity.Unit;
import cn.citms.icw.vo.UnitVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 单元信息 Mapper 接口
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface UnitMapper extends BaseMapper<Unit> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param unit
	 * @return
	 */
	List<UnitVO> selectUnitPage(@Param("page")IPage page, @Param("vo")UnitVO unit);

	/**
	 * 查询所有单元信息接口
	 * @return
	 */
	List<UnitVO> findUnit(@Param("id")String id, @Param("community_Id")String community_Id);

	/**
	 * 查询单元信息  in
	 * @param ids
	 * @return
	 */
	List<Unit> selectUnitByIdIn(@Param("ids") Set<String> ids);

	/**
	 * 查询社区是否绑定了楼栋信息
	 * @param id
	 * @return
	 */
	Integer selectCountBandingUnitId(String id);

	List<Unit> selectByCommunityIds(@Param("ids") List<String> communityIds);

	void batchInsert(@Param("list") List<Unit> list);
}
