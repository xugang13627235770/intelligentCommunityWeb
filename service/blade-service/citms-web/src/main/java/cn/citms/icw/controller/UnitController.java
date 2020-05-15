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

import cn.citms.icw.entity.Unit;
import cn.citms.icw.service.IUnitService;
import cn.citms.icw.vo.UnitVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.common.utils.CommonException;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 单元信息 控制器
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("Unit")
@Api(value = "单元信息", tags = "基础数据-单元管理接口")
public class UnitController extends BladeController {

	private IUnitService unitService;

	/**
	 * 获取指定单元信息
	 */
	@GetMapping("/FindById")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取指定单元信息", notes = "传入unit")
	public R<UnitVO> findById(@ApiParam(value = "主键集合", required = true) @RequestParam String id) {
		try {
			UnitVO unitVO = unitService.findById(id);
			return R.data(unitVO);
		} catch (CommonException e) {
			return R.fail(e.getErrormsg());
		}
	}

	/**
	 * 查询所有单元信息接口
	 */
	@GetMapping("/FindAll")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询所有单元信息接口", notes = "")
	public R<List<UnitVO>> findAll() {
		try {
			List<UnitVO> unitVOList = unitService.findAll();
			return R.data(unitVOList);
		} catch (CommonException e) {
			return R.fail(e.getErrormsg());
		}
	}

	/**
	 * 查询社区下的单元信息
	 */
	@GetMapping("/QueryUnitByCommunityId")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "查询社区下的单元信息", notes = "")
	public R<List<UnitVO>> queryUnitByCommunityId(@ApiParam(value = "社区ID", required = true) @RequestParam String communityId) {
		try {
			List<UnitVO> unitVOList = unitService.queryUnitByCommunityId(communityId);
			return R.data(unitVOList);
		} catch (CommonException e) {
			return R.fail(e.getErrormsg());
		}
	}

	/**
	 * 查询接口
	 */
	@PostMapping("/Query")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "查询接口", notes = "传入unit")
	public R<IPage<UnitVO>> query(@RequestBody UnitVO unit) {
		IPage<UnitVO> pages = unitService.selectUnitPage(unit);
		return R.data(pages);
	}

	/**
	 * 新增或修改 单元信息
	 */
	@PostMapping("/SaveUnit")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "新增或修改", notes = "data=true:新增或修改成功，data=false:新增或修改失败")
	public R submit(@Valid @RequestBody UnitVO unit) {
		try {
			boolean flag = unitService.saveOrUpdateUnit(unit);
			return R.status(flag);
		} catch (CommonException e) {
			return R.fail(e.getErrormsg());
		}
	}

	
	/**
	 * 删除 单元信息
	 */
	@DeleteMapping("/DeleteById")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "删除单元信息", notes = "data=true:删除成功，data=false:删除失败")
	public R deleteById(@ApiParam(value = "主键", required = true) @RequestParam String id) {
		try {
			boolean flag = unitService.deleteById(id);
			return R.status(flag);
		}catch (CommonException e){
			return R.fail(e.getErrormsg());
		}
	}

	/**
	 * 批量删除车辆信息
	 */
	@DeleteMapping("/DeleteByArrayIds")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "批量删除单元信息", notes = "data=true:删除成功，data=false:删除失败")
	public R deleteByArrayIds(@ApiParam(value = "主键集合", required = true) @RequestBody  List<String> ids) {
		try {
			return unitService.deleteByArrayIds(ids);
		}catch (Exception e){
			return R.fail("删除失败");
		}
	}

	/**
	 * 查询单元信息是否已经绑定了数据
	 */
	@GetMapping("/BandingUnitId")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "查询单元信息是否已经绑定了数据", notes = "data=true:单元信息绑定了数据，data=false:单元信息没有绑定数据")
	public R bandingUnitId(@ApiParam(value = "社区ID", required = true) @RequestParam String id) {
		try {
			Unit unitCon = new Unit();
			unitCon.setCommunity_Id(id);
			int count = unitService.count(Condition.getQueryWrapper(unitCon));
			return R.data(count>0);
		} catch (Exception e) {
			return R.fail("查询失败");
		}
	}


}
