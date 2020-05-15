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

import cn.citms.icw.dto.PersoncheckinDTO;
import cn.citms.icw.entity.Personcheckin;
import cn.citms.icw.entity.StatisticTypeTotal;
import cn.citms.icw.vo.PersoncheckinParamVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 住户信息 服务类
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface IPersoncheckinService extends IService<Personcheckin> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param vo
	 * @return
	 */
	IPage<PersoncheckinDTO> selectPersoncheckinPage(IPage<PersoncheckinDTO> page, PersoncheckinParamVO vo);

    PersoncheckinDTO getPersonCheckInDetail(String id);

	List<PersoncheckinDTO> getAllPersonCheckin();

    double countPerson(String communityId);

	/**
	 * 人员类型-数量统计
	 * @return
	 */
	Map<String, Integer> getCheckInRylxCnt(List<String> communityIds);

	/**
	 * 用户标识-数量统计
	 * @return
	 */
	Map<String, Integer> getCheckInYhbsCnt(List<String> communityIds);

	/**
	 * 重点人员判断标识-数量统计
	 * @return
	 */
	Map<String, Integer> getCheckInZdryCnt(List<String> communityIds);
	/**
	 * 查询数量
	 * @return
	 */
	Integer getCheckInCnt(List<String> communityIds);

	/**
	 * 通过houseId查询
	 * @return
	 */
	Map<String, Integer> findGroupByHouseId();
	/**
	 * 查询最近一年的数据
	 * @param communityIds
	 * @return
	 */
	List<StatisticTypeTotal> getPersoncheckinByTime(List<String> communityIds);
}
