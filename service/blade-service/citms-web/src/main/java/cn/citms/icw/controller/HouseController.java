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

import cn.citms.icw.service.IHouseService;
import cn.citms.icw.vo.HouseVO;
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
 * 门户信息 控制器
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("House")
@Api(value = "门户信息", tags = "门户信息接口")
public class HouseController extends BladeController {

	private IHouseService houseService;

	/**
	 * 管理员查询接口
	 */
	@PostMapping("/ManageQuery")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "管理员查询接口", notes = "传入HouseVO")
	public R<IPage<HouseVO>> list(@RequestBody HouseVO houseVO) {
		IPage<HouseVO> r = houseService.selectManageQuery(houseVO);
		return R.data(r);
	}

	/**
	 * 获取指定门户信息
	 */
	@GetMapping("/ManageFindById")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取指定门户信息", notes = "传入id")
	public R<HouseVO> manageFindById(@RequestParam String id) {
		HouseVO vo = houseService.manageFindById(id);
		return R.data(vo);
	}

	/**
	 * 查询接口
	 */
	@PostMapping("/Query")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "查询接口", notes = "传入house")
	public R<IPage<HouseVO>> page(@RequestBody HouseVO house) {
		IPage<HouseVO> pages = houseService.selectHousePage(house);
		return R.data(pages);
	}

	/**
	 * 查询所有门户信息接口
	 */
	@GetMapping("/FindAll")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "查询所有门户信息接口")
	public R<List<HouseVO>> listAll() {
		List<HouseVO> list = houseService.selectAll();
		return R.data(list);
	}

	/**
	 * 获取指定门户信息
	 */
	@GetMapping("/FindById")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "获取指定门户信息", notes = "传入id")
	public R<HouseVO> detail(@RequestParam String id) {
		HouseVO detail = houseService.selectById(id);
		return R.data(detail);
	}

	/**
	 * 保存门户信息
	 */
	@PostMapping("/SaveHouse")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "保存门户信息")
	public R save(@Valid @RequestBody HouseVO houseVO) {
		return houseService.saveHouse(houseVO);
	}

	/**
	 * 删除门户信息
	 */
	@DeleteMapping("/DeleteById")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "删除门户信息", notes = "传入id")
	public R remove(@RequestParam String id) {
		return R.status(houseService.removeById(id));
	}

	/**
	 * 批量删除门户信息
	 */
	@DeleteMapping("/DeleteByArrayIds")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "批量删除门户信息", notes = "传入ids")
	public R removeByIds(@ApiParam(value = "主键集合", required = true) @RequestBody List<String> ids) {
		String msg = houseService.deleteByBuildingIds(ids);
		return R.success(msg);
	}

	/**
	 * 查询门户信息是否已经绑定了住户数据
	 */
	@GetMapping("/BandingHouseId")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "查询门户信息是否已经绑定了住户数据", notes = "传入building")
	public R bandingHouseId(@RequestParam String id) {
		Boolean flag = houseService.bandingHouseId(id);
		return R.data(flag ? "" : "1");
	}
}
