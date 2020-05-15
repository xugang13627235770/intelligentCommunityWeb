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

import cn.citms.icw.entity.Notice;
import cn.citms.icw.vo.NoticeVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.core.tool.api.R;

import java.util.List;

/**
 * 通知公告表 服务类
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface INoticeService extends IService<Notice> {

	/**
	 * 自定义分页
	 * @param notice
	 * @return
	 */
	IPage<NoticeVO> selectNoticePage(NoticeVO notice);

	/**
	 *查询所有公告信息接口
	 * @return
	 */
	List<NoticeVO> selectAll();

	/**
	 * 获取指定公告信息
	 * @param id
	 * @return
	 */
	R<NoticeVO> selectById(String id);

	/**
	 * 根据当前ID获取上一条公告或下一条公告
	 * @param id
	 * @param type
	 * @return
	 */
	R<NoticeVO> findByIdOrder(String id, String type);

	/**
	 * 根据用户代码获取当前用户发布的公告
	 * @param userCode
	 * @return
	 */
	List<NoticeVO> findByUserCode(String userCode);

	/**
	 * 保存
	 * @param vo
	 * @return
	 */
	R<String> saveNotice(NoticeVO vo);

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	R<String> removeById(String id);

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	String deleteByBuildingIds(List<String> ids);
}
