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

import cn.citms.icw.entity.Vehicle;
import cn.citms.icw.vo.VehicleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.common.utils.CommonException;
import org.springblade.core.tool.api.R;

import java.util.Date;
import java.util.List;

/**
 * 社区车辆信息 服务类
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface IVehicleService extends IService<Vehicle> {

	/**
	 * 自定义分页
	 *
	 * @param vehicle
	 * @return
	 */
	IPage<VehicleVO> selectVehiclePage(VehicleVO vehicle);

	/**
	 * 查询指定的车辆信息
	 * @param id
	 * @return
	 */
	VehicleVO findById(String id) throws CommonException;

	/**
	 * 查询所有车辆信息
	 * @return
	 */
	List<VehicleVO>  findAll();

	/**
	 * 新增或者保存车辆信息
	 * @param vehicleVO
	 * @return
	 */
	boolean saveOrUpdateVehicle(VehicleVO vehicleVO) throws CommonException;

	/**
	 * 删除车辆信息
	 * @param id
	 * @return
	 * @throws CommonException
	 */
	boolean deleteById(String id) throws CommonException;

	/**
	 *批量删除车辆信息
	 * @param ids
	 * @return
	 * @throws CommonException
	 */
	R deleteByArrayIds(List<String> ids) throws CommonException;

	/**
	 * 本市车数量统计
	 * @param localPlateMark
	 * @return
	 */
	double getLocalVehicle(String localPlateMark, String communityId);

	/**
	 * 本省外地车数量统计
	 * @param localPlateMark
	 * @param provincePlateMark
	 * @return
	 */
	double getLocalProvinceOtherCityVehicle(String localPlateMark, String provincePlateMark, String communityId);

	/**
	 * 外省车数量统计
	 * @param provincePlateMark
	 * @return
	 */
	double getOtherProvinceVehicle(String provincePlateMark, String communityId);

	/**
	 * 根据开始时间和结束时间统计车辆数量
	 * @param monthStart
	 * @param monthEnd
	 * @return
	 */
    double getVehicleByStartMonthAndEndMonth(Date monthStart, Date monthEnd, String communityId);

    /**
     * 根据车牌类型统计车辆数量
     * @param plateType
     * @return
     */
    double getVehicleCountByPlateType(String plateType,String communityId);

    double countVehicle(String communityId);

}
