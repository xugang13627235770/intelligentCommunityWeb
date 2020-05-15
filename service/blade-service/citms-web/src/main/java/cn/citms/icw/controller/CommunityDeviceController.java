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

import cn.citms.icw.service.ICommunityDeviceService;
import cn.citms.icw.vo.CommunityDeviceVO;
import cn.citms.icw.vo.DeviceRequestVO;
import cn.citms.icw.vo.DeviceVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 社区设备关联表 控制器
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("Device")
@Api(value = "社区设备关联表", tags = "社区设备关联表接口")
public class CommunityDeviceController extends BladeController {

	private ICommunityDeviceService deviceService;

	/**
	 * 查询接口
	 */
	@PostMapping("/Query")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "查询接口", notes = "传入BuildingVO")
	public R<IPage<CommunityDeviceVO>> list(@RequestBody DeviceRequestVO vo) {
		IPage<CommunityDeviceVO> pages = deviceService.selectDevicePage(vo);
		return R.data(pages);
	}

	/**
	 * 获取指定设备信息
	 */
	@GetMapping("/FindById")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取指定设备信息", notes = "传入设备id")
	public R<CommunityDeviceVO> detail(@RequestParam String id) {
		return deviceService.selectDeviceById(id);
	}

	/**
	 * 获取相关id设备信息
	 */
	@PostMapping("/GetSomeDevice")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "获取相关id设备信息", notes = "传入设备ids")
	public R<List<CommunityDeviceVO>> getSomeDevice(@RequestBody List<String> ids) {
		List<CommunityDeviceVO> list = deviceService.selectSomeDevice(ids);
		return R.data(list);
	}

	/**
	 * 查询所有设备
	 */
	@GetMapping("/FindAll")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "查询所有设备", notes = "查询所有设备")
	public R<List<CommunityDeviceVO>> findAll() {
		List<CommunityDeviceVO> list = deviceService.selectAll();
		return R.data(list);
	}

	/**
	 * 保存设备信息
	 */
	@PostMapping("/SaveDevice")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "保存设备信息", notes = "传入设备对象device")
	public R SaveDevice(@Valid @RequestBody DeviceVO device) {
		return deviceService.saveDevice(device);
	}

	/**
	 * 删除设备信息
	 */
	@DeleteMapping("/DeleteById")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "删除设备信息", notes = "传入设备对象device")
	public R deleteById(@RequestParam String id) {
		return deviceService.deleteById(id);
	}
}
