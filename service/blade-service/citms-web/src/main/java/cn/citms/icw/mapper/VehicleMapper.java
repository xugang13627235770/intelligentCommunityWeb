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
package cn.citms.icw.mapper;

import cn.citms.icw.dto.VehicleDTO;
import cn.citms.icw.entity.Vehicle;
import cn.citms.icw.vo.VehicleVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 社区车辆信息 Mapper 接口
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface VehicleMapper extends BaseMapper<Vehicle> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param vehicle
	 * @return
	 */
	List<VehicleVO> selectVehiclePage(@Param("page") IPage page, @Param("vo")VehicleVO vehicle);

	/**
	 * 根据id查找车辆信息
	 * @param id
	 * @return
	 */
	List<VehicleVO> findVehicle(@Param("id")String id);

	/**
	 * 证件查询社区车辆信息
	 * @param cardNos
	 * @return
	 */
	List<VehicleDTO> selectVehicleInCardNos(@Param("cardNos") List<String> cardNos);

	/**
	 * 本市车数量统计
	 * @param localPlateMark
	 * @return
	 */
    double getLocalVehicle(@Param("localPlateMark")String localPlateMark,@Param("communityId")String communityId);

	/**
	 * 本省外地车数量统计
	 * @param localPlateMark
	 * @param provincePlateMark
	 * @return
	 */
	double getLocalProvinceOtherCityVehicle(@Param("localPlateMark")String localPlateMark,
											@Param("provincePlateMark")String provincePlateMark,
											@Param("communityId")String communityId);

	/**
	 * 外省车数量统计
	 * @param provincePlateMark
	 * @return
	 */
	double getOtherProvinceVehicle(@Param("provincePlateMark")String provincePlateMark,@Param("communityId")String communityId);

	/**
	 * 根据开始时间和结束时间统计车辆数量
	 * @param monthStart
	 * @param monthEnd
	 * @return
	 */
	double getVehicleByStartMonthAndEndMonth(@Param("monthStart")Date monthStart,
											 @Param("monthEnd")Date monthEnd,
											 @Param("communityId")String communityId);

	/**
	 * 根据车牌类型统计车辆数量
	 * @param plateType
	 * @return
	 */
	double getVehicleCountByPlateType(@Param("plateType")String plateType,@Param("communityId")String communityId);

	double countVehicle(@Param("communityId")String communityId);

	List<Vehicle> selectByCommunityIds(@Param("ids") List<String> communityIds);

	void batchInsert(@Param("list") List<Vehicle> list);

}
