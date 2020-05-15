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

import cn.citms.icw.dto.PersoncheckinDTO;
import cn.citms.icw.vo.PersoncheckinParamVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.citms.icw.entity.Personcheckin;
import cn.citms.icw.vo.PersoncheckinVO;
import cn.citms.icw.wrapper.PersoncheckinWrapper;
import cn.citms.icw.service.IPersoncheckinService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 住户信息 控制器
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/PersonCheckIn")
@Api(value = "住户信息", tags = "住户信息接口")
public class PersoncheckinController extends BladeController {

	@Resource
	private IPersoncheckinService personcheckinService;

	/**
	 * 详情
	 */
	@GetMapping("/FindById")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入personcheckin")
	public R<PersoncheckinDTO> detail(String id) {
		if(StringUtils.isBlank(id)){
			R.fail("入参id为空");
		}
		PersoncheckinDTO dto = personcheckinService.getPersonCheckInDetail(id);
		if(dto == null){
			return R.fail("不存在id为【"+ id +"】的用户入住信息");
		}
		return R.data(dto);
	}

	/**
	 * 分页 住户信息
	 */
	@PostMapping("/Query")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页")
	public R<IPage<PersoncheckinDTO>> list(@RequestBody PersoncheckinParamVO vo) {
		Query query = new  Query();
		if(vo!=null && vo.getPageIndex() != null && vo.getPageIndex()>0 && vo.getPageSize() !=null && vo.getPageSize()>0){
			query.setCurrent(vo.getPageIndex());
			query.setSize(vo.getPageSize());
		}
		return R.data(personcheckinService.selectPersoncheckinPage(Condition.getPage(query),vo));
	}


	/**
	 *
	 * 查询所有用户入住信息接口
	 */
	@GetMapping("/FindAll")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "查询所有用户入住信息接口")
	public R<List<PersoncheckinDTO>> findAll() {
		List<PersoncheckinDTO> list = personcheckinService.getAllPersonCheckin();
		return R.data(list);
	}

	/**
	 * 新增 住户信息
	 */
	@PostMapping("/SavePersonCheckIn")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "保存用户入住信息", notes = "传入personcheckin")
	public R savePersonCheckIn(@Valid @RequestBody Personcheckin personcheckin) {
		if(personcheckin == null){
			return R.fail("入参传入personcheckin为空");
		}
		boolean flag = true;
		if(StringUtils.isNotBlank(personcheckin.getId())){
			Personcheckin personcheckinModel = personcheckinService.getById(personcheckin.getId());
			if(personcheckinModel == null){
				return R.fail("不存在id为【"+ personcheckin.getId() +"】的用户入住数据");
			}
			flag = personcheckinService.updateById(personcheckin);
		}else{
			personcheckin.setId(UUID.randomUUID().toString().replaceAll("-",""));
			personcheckin.setBlsj(new Date());
			flag = personcheckinService.save(personcheckin);
		}
		return R.status(flag);
	}

	/**
	 * 删除用户入住信息
	 */
	@DeleteMapping("/DeleteById")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "删除用户入住信息")
	public R deleteById(@RequestParam String id) {
		if(StringUtils.isBlank(id)){
			return R.fail("参数id为空");
		}
		Personcheckin personcheckin = personcheckinService.getById(id);
		if(personcheckin == null){
			return R.fail("不存在id【"+ id +"】的用户入住数据");
		}
		return R.status(personcheckinService.removeById(id));
	}

	/**
	 * 批量删除住户信息
	 */
	@DeleteMapping("/DeleteByArrayIds")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "批量删除住户信息", notes = "传入ids")
	public R deleteByArrayIds(@ApiParam(value = "主键集合", required = true) @RequestBody List<String> ids) {
		if(CollectionUtils.isEmpty(ids)){
			return R.fail("参数ids为空");
		}
		try {
			for (String id : ids) {
				personcheckinService.removeById(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.status(false);
		}
		return R.status(true);
	}

	/**
	 * 批量上传图片
	 */
	@PostMapping("/UploadFiles")
	@ApiOperationSupport(order =7)
	@ApiOperation(value = "批量上传图片")
	public R uploadFiles(List<MultipartFile> files) {
		return R.status(true);
	}

	/**
	 * 住户信息上传对接
	 */
	@PostMapping("/PersonInfoUpload")
	@ApiOperationSupport(order =8)
	@ApiOperation(value = "住户信息上传对接")
	public R personInfoUpload(List<MultipartFile> files) {
		return R.status(true);
	}
	
}
