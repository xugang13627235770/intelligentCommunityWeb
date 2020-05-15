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
package cn.citms.icw.service;

import cn.citms.icw.entity.CommunityDevice;
import cn.citms.icw.vo.CommunityDeviceVO;
import cn.citms.icw.vo.DeviceRequestVO;
import cn.citms.icw.vo.DeviceVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.core.tool.api.R;

import java.util.List;

/**
 * 社区设备关联表 服务类
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface ICommunityDeviceService extends IService<CommunityDevice> {

	/**
	 * 自定义分页
	 * @param vo
	 * @return
	 */
	IPage<CommunityDeviceVO> selectDevicePage(DeviceRequestVO vo);

	/**
	 * 获取指定设备信息
	 * @param id 设备id
	 */
	R<CommunityDeviceVO> selectDeviceById(String id);

	List<CommunityDevice> getInfoByCommunityId(String id);
	/**
	 * 获取相关id设备信息
	 * @param ids
	 * @return
	 */
	List<CommunityDeviceVO> selectSomeDevice(List<String> ids);

	/**
	 * 获取所有设备信息
	 * @return
	 */
	List<CommunityDeviceVO> selectAll();

	/**
	 * 保存设备信息
	 * @param device
	 * @return
	 */
	R<String> saveDevice(DeviceVO device);

	/**
	 * 删除设备信息
	 * @param deviceId
	 * @return
	 */
	R<String> deleteById(String deviceId);

	List<CommunityDevice> getByCommunityIdList(List<String> communityIds);
}
