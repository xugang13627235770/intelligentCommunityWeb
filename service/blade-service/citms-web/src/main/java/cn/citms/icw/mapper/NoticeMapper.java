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

import cn.citms.icw.entity.Notice;
import cn.citms.icw.vo.NoticeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知公告表 Mapper 接口
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface NoticeMapper extends BaseMapper<Notice> {

	/**
	 * 查询列表
	 * @param vo
	 * @return
	 */
	IPage<Notice> selectNoticePage(IPage<NoticeVO> page, @Param("vo") NoticeVO vo);

	/**
	 *查询所有公告信息接口
	 * @return
	 */
	List<Notice> selectAll();

	/**
	 * 根据用户代码获取当前用户发布的公告
	 * @param userCode
	 * @return
	 */
	List<Notice> findByUserCode(@Param("userCode") String userCode);
}
