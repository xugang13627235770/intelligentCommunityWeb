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
import cn.citms.icw.dto.PersoncheckinDTO;
import cn.citms.icw.entity.Personcheckin;
import cn.citms.icw.entity.StatisticTypeTotal;
import cn.citms.icw.vo.PersoncheckinParamVO;
import cn.citms.icw.vo.SelectCntVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 住户信息 Mapper 接口
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface PersoncheckinMapper extends BaseMapper<Personcheckin> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param personcheckin
	 * @return
	 */
	List<PersoncheckinDTO> selectPersoncheckinPage(IPage page, @Param("vo") PersoncheckinParamVO personcheckin);

    PersoncheckinDTO getPersonCheckInDetail(@Param("id") String id);

	List<PersoncheckinDTO> getAllPersonCheckin();

	/**
	 * 获取指定门户信息
	 * @param id
	 * @return
	 */
	List<PersoncheckinDTO> manageFindById(String id);

	/**
	 * 查询住户信息数量
	 * @param id
	 * @return
	 */
	Integer selectCountBandingPersoncheckinById(String id);

    double countPerson(@Param("communityId") String communityId);

	/**
	 * 员类型-数量统计
	 * @return
	 */
	List<SelectCntVO> getCheckInRylxCnt(@Param("communityIds") List<String> communityIds);

	/**
	 * 用户标识-数量统计
	 * @return
	 */
	List<SelectCntVO> getCheckInYhbsCnt(@Param("communityIds") List<String> communityIds);

	/**
	 * 重点人员判断标识-数量统计
	 * @return
	 */
	List<SelectCntVO> getCheckInZdryCnt(@Param("communityIds") List<String> communityIds);

	/**
	 * group by houseId
	 * @return
	 */
	List<SelectCntVO> findGroupByHouseId();
	List<SelectCntVO> findGroupByHouseId2();

	/**
	 * 查询数量
	 * @return
	 */
	Integer getCheckInCnt(@Param("communityIds") List<String> communityIds);

	/**
	 * 查询最近一年的数据
	 * @param communityIds
	 * @return
	 */
	List<StatisticTypeTotal> getPersoncheckinByTime(@Param("communityIds") List<String> communityIds);

	List<CommonCountDTO> countGroupbyPersonType(@Param("ids") List<String> communityIds);

	List<Personcheckin> selectPersonByCommunityIds(@Param("ids") List<String> communityIds);

	void batchInsert(@Param("list") List<Personcheckin> list);
}
