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

import cn.citms.icw.dto.PatroltaskDTO;
import cn.citms.icw.service.IPatroltaskService;
import cn.citms.icw.vo.PatroltaskVO;
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
 *  控制器
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("PatrolTask")
@Api(value = "巡更管理-人员分配", tags = "巡更管理-人员分配接口")
public class PatroltaskController extends BladeController {

	private IPatroltaskService patroltaskService;

	/**
	 * 详情
	 */
	@GetMapping("/FindPatrolTaskById")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取指定巡更任务信息", notes = "传入patroltask")
	public R<PatroltaskVO> detail(@ApiParam(value = "主键", required = true) @RequestParam String id) {
		return R.data(patroltaskService.detail(id));
	}

	/**
	 * 自定义分页 
	 */
	@PostMapping("/QueryPatrolTask")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询巡更任务信息", notes = "传入patroltask")
	public R<IPage<PatroltaskVO>> page(@RequestBody PatroltaskVO patroltask) {
		IPage<PatroltaskVO> pages = patroltaskService.selectPatroltaskPage(patroltask);
		return R.data(pages);
	}

	/**
	 * 新增或修改 
	 */
	@PostMapping("/SavePatrolTask")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "保存巡更任务", notes = "传入patroltask")
	public R submit(@Valid @RequestBody PatroltaskDTO patroltaskDTO) {
		try {
			boolean flag = patroltaskService.saveOrUpdatePatroltask(patroltaskDTO);
			return R.status(flag);
		} catch (CommonException e) {
			return R.fail(e.getErrormsg());
		}
	}

	
	/**
	 * 删除 
	 */
	@DeleteMapping("/DeletePatrolTaskById")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "删除巡更任务", notes = "传入id")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String id) {
		try {
			boolean flag = patroltaskService.removeById(id);
			return R.status(flag);
		}catch (Exception e){
			return R.fail("删除失败");
		}
	}

}
