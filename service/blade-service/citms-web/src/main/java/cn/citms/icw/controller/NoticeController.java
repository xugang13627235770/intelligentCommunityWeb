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
package cn.citms.icw.controller;

import cn.citms.icw.service.INoticeService;
import cn.citms.icw.vo.NoticeVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 通知公告表 控制器
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("Notice")
@Api(value = "公告发布", tags = "公告发布接口")
public class NoticeController extends BladeController {

	private INoticeService noticeService;

	/**
	 * 查询接口
	 */
	@PostMapping("/Query")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "查询接口", notes = "传入BuildingVO")
	public R list(@RequestBody NoticeVO vo) {
		IPage<NoticeVO> pages = noticeService.selectNoticePage(vo);
		return R.data(pages);
	}
	/**
	 * 查询所有公告信息接口
	 */
	@GetMapping("/FindAll")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询所有公告信息接口")
	public R listAll() {
		List<NoticeVO> list = noticeService.selectAll();
		return R.data(list);
	}

	/**
	 * 获取指定公告信息
	 */
	@GetMapping("/FindById")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "获取指定公告信息", notes = "传入 id")
	public R detail(@RequestParam String id) {
		R<NoticeVO> detail = noticeService.selectById(id);
		return detail;
	}

	/**
	 * 根据当前ID获取上一条公告或下一条公告
	 */
	@GetMapping("/FindByIdOrder")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "根据当前ID获取上一条公告或下一条公告")
	public R findByIdOrder(@RequestParam String id, String type) {
		R<NoticeVO> detail = noticeService.findByIdOrder(id, type);
		return detail;
	}
	/**
	 * 查询接口
	 */
	@PostMapping("/FindByUserCode")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "查询接口", notes = "传入userCode")
	public R findByUserCode(@RequestParam String userCode) {
		List<NoticeVO> list = noticeService.findByUserCode(userCode);
		return R.data(list);
	}
	/**
	 * 保存公告信息
	 */
	@PostMapping("/SaveNotice")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "保存公告信息")
	public R save(@Valid @RequestBody NoticeVO vo) {
		return noticeService.saveNotice(vo);
	}

	/**
	 * 删除公告信息
	 */
	@DeleteMapping("/DeleteById")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "删除公告信息", notes = "传入id")
	public R remove(@RequestParam String id) {
		return noticeService.removeById(id);
	}

	/**
	 * 删除公告信息
	 */
	@DeleteMapping("/DeleteByArrayIds")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "批量删除公告信息", notes = "传入ids")
	public R removeByIds(@ApiParam(value = "主键集合", required = true) @RequestParam List<String> ids) {
		String msg = noticeService.deleteByBuildingIds(ids);
		return R.success(msg);
	}
}
