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
import cn.citms.icw.entity.House;
import cn.citms.icw.vo.HouseVO;
import cn.citms.icw.vo.SelectCntVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门户信息 Mapper 接口
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface HouseMapper extends BaseMapper<House> {

	/**
	 * 管理员查询接口
	 */
	IPage<House> selectManageQuery(IPage page, @Param("vo") HouseVO vo);

	/**
	 * 查询所有门户信息接口
	 */
	List<House> selectAll();

	House selectHouseById(String id);

	Integer selectCountBandingHouseId(String id);

    double countHouse(@Param("communityId") String communityId);

	/**
	 * 房屋状态-数量统计
	 * @return
	 */
	List<SelectCntVO> getHouseZtCnt(@Param("communityIds") List<String> communityIds);

	/**
	 * 查询数量
	 * @param communityIds
	 * @return
	 */
	Integer getHouseCnt(@Param("communityIds") List<String> communityIds);

	List<CommonCountDTO> countGroupByHouseFunction(@Param("ids") List<String> communityIds);

	List<House> selectByCommunityIds(@Param("ids") List<String> communityIds);

	void batchInsert(@Param("list") List<House> list);
}
