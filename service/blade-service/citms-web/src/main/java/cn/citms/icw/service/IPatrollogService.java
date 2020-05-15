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

import cn.citms.icw.vo.EsPatrollogSearchVO;
import cn.citms.icw.vo.EsPatrollogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 *  巡更记录
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface IPatrollogService{

	/**
	 * 查询巡更记录-分页
	 * @param vo
	 * @return
	 */
	IPage<EsPatrollogVO> queryPatrollog(EsPatrollogSearchVO vo);

	/**
	 * 查询巡更记录详情
	 * @param id
	 * @return
	 */
	EsPatrollogVO findById(String id);

}
