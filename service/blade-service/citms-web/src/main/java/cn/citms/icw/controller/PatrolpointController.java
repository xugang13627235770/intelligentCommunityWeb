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

import cn.citms.icw.service.IPatrolpointService;
import cn.citms.icw.vo.PatrolpointVO;
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

/**
 *  巡更点
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("PatrolPoint")
@Api(value = "巡更管理-巡更点管理", tags = "巡更管理-巡更点管理接口")
public class PatrolpointController extends BladeController {

	private IPatrolpointService patrolpointService;

	/**
	 * 详情
	 */
	@GetMapping("/FindPatrolPointById")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取指定巡更点信息", notes = "传入patrolpoint")
	public R<PatrolpointVO> detail(@ApiParam(value = "主键", required = true) @RequestParam String id) {
		try {
			PatrolpointVO patrolpointVO = patrolpointService.detail(id);
			return R.data(patrolpointVO);
		} catch (CommonException e) {
			return R.fail(e.getErrormsg());
		}
	}

	/**
	 * 自定义分页 
	 */
	@PostMapping("/QueryPatrolPoint")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询巡更点信息", notes = "传入patrolpoint")
	public R<IPage<PatrolpointVO>> page(@RequestBody PatrolpointVO patrolpoint) {
		IPage<PatrolpointVO> pages = patrolpointService.selectPatrolpointPage(patrolpoint);
		return R.data(pages);
	}

	/**
	 * 新增或修改 
	 */
	@PostMapping("/SavePatrolPoint")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "保存巡更点", notes = "传入patrolpoint")
	public R submit(@Valid @RequestBody PatrolpointVO patrolpoint) {
		try {
			boolean flag = patrolpointService.saveOrUpdatePatrolpoint(patrolpoint);
			return R.status(flag);
		} catch (CommonException e) {
			return R.fail(e.getErrormsg());
		}
	}

	
	/**
	 * 删除 
	 */
	@DeleteMapping("/DeletePatrolPointById")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "删除巡更点", notes = "传入id")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String id) {
		try {
			boolean flag = patrolpointService.removeById(id);
			return R.status(flag);
		}catch (Exception e){
			return R.fail("删除失败");
		}
	}

	
}
