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

import cn.citms.icw.service.IPatrollogService;
import cn.citms.icw.vo.EsPatrollogSearchVO;
import cn.citms.icw.vo.EsPatrollogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

/**
 *  巡更记录
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("Patrollog")
@Api(value = "巡更管理-巡更记录", tags = "巡更管理-巡更记录接口")
public class PatrollogController extends BladeController {

	private IPatrollogService patrollogService;

	/**
	 * 详情
	 */
	@GetMapping("/FindById")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取指定巡更记录信息", notes = "传入id")
	public R<EsPatrollogVO> detail(@ApiParam(value = "主键", required = true) @RequestParam String id) {
		return R.data(patrollogService.findById(id));
	}

	/**
	 * 自定义分页 
	 */
	@PostMapping("/Query")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询巡更记录信息", notes = "传入patroltask")
	public R<IPage<EsPatrollogVO>> page(@RequestBody EsPatrollogSearchVO esPatrollogSearchVO) {
		IPage<EsPatrollogVO> pages = patrollogService.queryPatrollog(esPatrollogSearchVO);
		return R.data(pages);
	}


}
