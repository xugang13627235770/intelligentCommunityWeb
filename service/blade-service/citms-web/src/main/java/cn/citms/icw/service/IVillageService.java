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

import cn.citms.icw.vo.CommunityDetailVO;
import cn.citms.icw.vo.VillageVO;

import java.util.List;

/**
 * @author cyh
 */
public interface IVillageService {

	/**
	 * 获取社区基础数据
	 * @return
	 */
	List<VillageVO> getVillageCount();
	/**
	 * 根据小区id获取小区详情
	 * @return
	 */
	CommunityDetailVO selectCommunityDetail(String communityId);
}
