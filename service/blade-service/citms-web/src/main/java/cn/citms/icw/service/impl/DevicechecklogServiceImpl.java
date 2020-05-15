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
package cn.citms.icw.service.impl;

import cn.citms.icw.entity.Devicechecklog;
import cn.citms.icw.vo.DevicechecklogVO;
import cn.citms.icw.mapper.DevicechecklogMapper;
import cn.citms.icw.service.IDevicechecklogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 *  服务实现类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Service
public class DevicechecklogServiceImpl extends ServiceImpl<DevicechecklogMapper, Devicechecklog> implements IDevicechecklogService {

	@Override
	public IPage<DevicechecklogVO> selectDevicechecklogPage(IPage<DevicechecklogVO> page, DevicechecklogVO devicechecklog) {
		return page.setRecords(baseMapper.selectDevicechecklogPage(page, devicechecklog));
	}

}
