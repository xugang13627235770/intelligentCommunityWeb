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

import cn.citms.icw.service.IVehicleService;
import cn.citms.icw.vo.VehicleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.common.utils.CommonException;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 社区车辆信息 控制器
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("Vehicle")
@Api(value = "社区车辆信息", tags = "基础数据-车辆管理接口")
public class VehicleController extends BladeController {

	private IVehicleService vehicleService;

	/**
	 * 详情
	 */
	@GetMapping("/FindById")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "查询指定的车辆信息", notes = "传入Id")
	public R<VehicleVO> findById(@ApiParam(value = "主键", required = true) @RequestParam String id)
			throws CommonException {
		VehicleVO detail = vehicleService.findById(id);
		return R.data(detail);
	}

	/**
	 * 管理员查询指定的车辆信息
	 */
	@GetMapping("/ManageFindById")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "管理员查询指定的车辆信息", notes = "传入Id")
	public R<VehicleVO> manageFindById(@ApiParam(value = "主键", required = true) @RequestParam String id)
			throws CommonException {
		VehicleVO detail = vehicleService.findById(id);
		return R.data(detail);
	}

	/**
	 * 查询所有车辆信息
	 */
	@GetMapping("/FindAll")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "查询所有车辆信息", notes = "查询所有车辆信息")
	public R<List<VehicleVO>> findAll() {
		return R.data(vehicleService.findAll());
	}

	/**
	 * 自定义分页 社区车辆信息
	 */
	@PostMapping("/Query")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "查询车辆信息", notes = "传入vehicle")
	public R<IPage<VehicleVO>> page(@RequestBody VehicleVO vehicle) {
		IPage<VehicleVO> pages = vehicleService.selectVehiclePage(vehicle);
		return R.data(pages);
	}

	/**
	 * 新增或修改 社区车辆信息
	 */
	@PostMapping("/SaveVehicle")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "保存车辆信息", notes = "传入vehicle")
	public R submit(@Valid @RequestBody VehicleVO vehicleVO) {
		try {
			boolean flag = vehicleService.saveOrUpdateVehicle(vehicleVO);
			return R.status(flag);
		} catch (CommonException e) {
			return R.fail(e.getErrormsg());
		}
	}

	/**
	 * 删除 车辆信息
	 */
	@DeleteMapping("/DeleteById")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "删除车辆信息", notes = "data=true:删除成功，data=false:删除失败")
	public R deleteById(@ApiParam(value = "主键", required = true) @RequestParam String id) {
		try {
			boolean flag = vehicleService.deleteById(id);
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
	@ApiOperation(value = "批量删除车辆信息", notes = "data=true:删除成功，data=false:删除失败")
	public R deleteByArrayIds(@ApiParam(value = "主键集合", required = true) @RequestBody  List<String> ids) {
		try {
			return vehicleService.deleteByArrayIds(ids);
		}catch (Exception e){
			return R.fail("删除失败");
		}
	}

	
}
