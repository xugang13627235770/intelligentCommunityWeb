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

import cn.citms.icw.service.IBuildingService;
import cn.citms.icw.vo.BuildingVO;
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
 * 楼栋信息 控制器
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("Building")
@Api(value = "楼栋信息", tags = "楼栋信息接口")
public class BuildingController extends BladeController {

	private IBuildingService buildingService;

	/**
	 * 分页 楼栋信息
	 */
	@PostMapping("/Query")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "查询接口", notes = "传入BuildingVO")
	public R<IPage<BuildingVO>> list(@RequestBody BuildingVO buildingVO) {
		IPage<BuildingVO> pages = buildingService.selectBuildingPage(buildingVO);
		return R.data(pages);
	}

	/**
	 * 获取指定楼栋信息
	 */
	@GetMapping("/FindById")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取指定楼栋信息", notes = "传入building id")
	public R<BuildingVO> detail(@RequestParam String id) {
		BuildingVO detail = buildingService.selectById(id);
		return R.data(detail);
	}

	/**
	 * 查询所有 楼栋信息
	 */
	@GetMapping("/FindAll")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "查询所有楼栋信息接口")
	public R<List<BuildingVO>> listAll() {
		List<BuildingVO> list = buildingService.selectAll();
		return R.data(list);
	}

	/**
	 * 新增 楼栋信息
	 */
	@PostMapping("/SaveBuilding")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "保存楼栋信息")
	public R save(@Valid @RequestBody BuildingVO buildingVO) {
		return buildingService.saveBuilding(buildingVO);
	}

	/**
	 * 删除 楼栋信息
	 */
	@DeleteMapping("/DeleteById")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "删除楼栋信息", notes = "传入id")
	public R remove(@RequestParam String id) {
		return R.status(buildingService.removeById(id));
	}

	/**
	 * 删除 楼栋信息
	 */
	@DeleteMapping("/DeleteByArrayIds")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "批量删除楼栋信息", notes = "传入ids")
	public R removeByIds(@ApiParam(value = "主键集合", required = true) @RequestBody List<String> ids) {
		String msg = buildingService.deleteByBuildingIds(ids);
		return R.success(msg);
	}

	/**
	 * 查询社区是否绑定了楼栋信息
	 */
	@GetMapping("/BandingCommunityId")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "查询社区是否绑定了楼栋信息", notes = "传入社区id")
	public R bandingCommunityId(@RequestParam String id) {
		Boolean flag = buildingService.bandingCommunityId(id);
		return R.data(flag ? "" : "1");
	}

	/**
	 * 查询楼栋信息是否已经绑定了数据
	 */
	@GetMapping("/BandingBuildingId")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "查询楼栋信息是否已经绑定了数据", notes = "传入building")
	public R bandingUnitId(@RequestParam String id) {
		Boolean flag = buildingService.bandingUnitId(id);
		return R.data(flag ? "" : "1");
	}
	
}
