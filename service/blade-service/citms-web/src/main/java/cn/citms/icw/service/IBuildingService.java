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

import cn.citms.icw.entity.Building;
import cn.citms.icw.vo.BuildingVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.core.tool.api.R;

import java.util.List;

/**
 * 楼栋信息 服务类
 *
 * @author Blade
 * @since 2020-04-22
 */
public interface IBuildingService extends IService<Building> {

	/**
	 * 自定义分页
	 *
	 * @param building
	 * @return
	 */
	IPage<BuildingVO> selectBuildingPage(BuildingVO building);

	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	BuildingVO selectById(String id);

	/**
	 * 保存楼栋信息
	 * @param buildingVO
	 * @return
	 */
	R<String> saveBuilding(BuildingVO buildingVO);

	/**
	 * 删除楼栋信息
	 * @param ids
	 * @return
	 */
	String deleteByBuildingIds(List<String> ids);

	/**
	 * 查询所有
	 * @return
	 */
	List<BuildingVO> selectAll();

	/**
	 * 查询社区是否绑定了楼栋信息
	 * @param id
	 * @return
	 */
	Boolean bandingCommunityId(String id);

	/**
	 * 查询楼栋信息是否已经绑定了数据
	 * @param id
	 * @return
	 */
	Boolean bandingUnitId(String id);
}
