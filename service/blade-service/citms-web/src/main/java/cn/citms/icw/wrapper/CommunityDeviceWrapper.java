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
package cn.citms.icw.wrapper;

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import cn.citms.icw.entity.CommunityDevice;
import cn.citms.icw.vo.CommunityDeviceVO;

/**
 * 社区设备关联表包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-04-22
 */
public class CommunityDeviceWrapper extends BaseEntityWrapper<CommunityDevice, CommunityDeviceVO>  {

	public static CommunityDeviceWrapper build() {
		return new CommunityDeviceWrapper();
 	}

	@Override
	public CommunityDeviceVO entityVO(CommunityDevice communityDevice) {
		CommunityDeviceVO communityDeviceVO = BeanUtil.copy(communityDevice, CommunityDeviceVO.class);

		return communityDeviceVO;
	}

}
