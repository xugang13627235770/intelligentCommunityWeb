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

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.constant.IntelligentCommunityConstant;
import cn.citms.icw.entity.Vehicle;
import cn.citms.icw.mapper.VehicleMapper;
import cn.citms.icw.service.IVehicleService;
import cn.citms.icw.vo.AttachmentVO;
import cn.citms.icw.vo.VehicleVO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.utils.CommonException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 社区车辆信息 服务实现类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements IVehicleService {

	@Value("${dgaRpc.getAttachmentBySourceIdUrl}")
	private String getAttachmentBySourceIdUrl;
	@Value("${dgaRpc.getDictByKindUrl}")
	private String getDictByKindUrl;
	@Resource
	private BaseService baseService;

	@Override
	public IPage<VehicleVO> selectVehiclePage(VehicleVO vehicle) {
		Integer pageIndex = vehicle.getPageindex() == null ? 1 : vehicle.getPageindex();
		Integer pageSize = (vehicle.getPagesize() == null || vehicle.getPagesize() == 0) ? 20 : vehicle.getPagesize();
		IPage<VehicleVO> page = new Page<>(pageIndex, pageSize);

		List<VehicleVO> vehicleList = baseMapper.selectVehiclePage(page, vehicle);
		convertVehicleAttachment(vehicleList);
		return page.setRecords(vehicleList);
	}

	/**
	 * 获取车辆附件信息
	 */
	private void convertVehicleAttachment(List<VehicleVO> vehicleList){
		if(CollectionUtils.isEmpty(vehicleList)){
			return;
		}

		//附件信息
		List<String> vehicleIdIds = vehicleList.stream().map(VehicleVO::getVehicleId).collect(toList());
		Map<String, List<AttachmentVO>> filesMap = baseService.getAttachmentBySourceIds(vehicleIdIds);

		//车辆数据字典，号牌种类，号牌颜色，车身颜色
		Map<Integer, Map<String, String>> allDict = baseService.findDict(Arrays.asList(
				IntelligentCommunityConstant.HPZL,IntelligentCommunityConstant.HPYS,IntelligentCommunityConstant.CSYS));

		for(VehicleVO vehicleVO : vehicleList){
			// 获取附件信息
			if(filesMap != null){
				vehicleVO.setFiles(filesMap.get(vehicleVO.getVehicleId()));
			}
			//号牌种类
			if(allDict != null && allDict.get(IntelligentCommunityConstant.HPZL) != null){
				vehicleVO.setPlateTypeCn(allDict.get(IntelligentCommunityConstant.HPZL).get(vehicleVO.getPlateType()));
			}
			//号牌颜色
			if(allDict != null && allDict.get(IntelligentCommunityConstant.HPYS) != null){
				vehicleVO.setPlateColorCn(allDict.get(IntelligentCommunityConstant.HPYS).get(vehicleVO.getPlateColor()));
			}
			//车身颜色
			if(allDict != null && allDict.get(IntelligentCommunityConstant.CSYS) != null){
				vehicleVO.setVehicleBodyColorCn(allDict.get(IntelligentCommunityConstant.CSYS).get(vehicleVO.getVehicleBodyColor()));
			}
		}

	}

	/**
	 * 查询指定的车辆信息
	 * @param id
	 * @return
	 */
	@Override
	public VehicleVO findById(String id) throws CommonException {
		if(StringUtils.isEmpty(id)){
			throw new CommonException(null, "参数异常");
		}

		List<VehicleVO> vehicleVOList = baseMapper.findVehicle(id);
		if(CollectionUtils.isEmpty(vehicleVOList)){
			return null;
		}

		convertVehicleAttachment(vehicleVOList);
		return vehicleVOList.get(0);
	}

	@Override
	public List<VehicleVO> findAll() {
		List<VehicleVO> vehicleVOList = baseMapper.findVehicle(null);
//		composeVehicleInfo(vehicleVOList);
		return vehicleVOList;
	}

	/**
	 * 新增或者保存车辆信息
	 * @param vehicleVO
	 * @return
	 */
	@Override
	public boolean saveOrUpdateVehicle(VehicleVO vehicleVO) throws CommonException {
		if(StringUtils.isEmpty(vehicleVO.getCommunityId())){
			throw new CommonException(null, "社区不能为空");
		}
		if(StringUtils.isEmpty(vehicleVO.getPlateNo())){
			throw new CommonException(null, "车牌号码不能为空");
		}

		if(StringUtils.isEmpty(vehicleVO.getVehicleId())){
			return saveVehicle(vehicleVO);
		}else{
			return updateVehicle(vehicleVO);
		}
	}

	/**
	 * 新增车辆信息
	 * @param vehicleVO
	 * @return
	 * @throws CommonException
	 */
	private boolean saveVehicle(VehicleVO vehicleVO) throws CommonException {
		Vehicle vehicleCon = new Vehicle();
		vehicleCon.setCommunityId(vehicleVO.getCommunityId());
		vehicleCon.setPlateNo(vehicleVO.getPlateNo());
		int count = baseMapper.selectCount(Condition.getQueryWrapper(vehicleCon));
		if(count > 0){
			throw new CommonException(null, "社区下已经存在该车的登记信息，无法再登记相同车辆 ");
		}

		Vehicle vehicle = BeanUtil.copy(vehicleVO, VehicleVO.class);
		vehicle.setVehicleId(CommonUtils.createUUID());
		vehicle.setCreatedTime(new Date());
		vehicle.setModifiedTime(vehicle.getCreatedTime());
		int num = baseMapper.insert(vehicle);

		//维护附件关联关系的逻辑
		composeAttachment(vehicleVO, vehicle.getVehicleId());
		return num > 0;
	}

	/**
	 * 修改车辆信息
	 * @param vehicleVO
	 * @return
	 * @throws CommonException
	 */
	private boolean updateVehicle(VehicleVO vehicleVO) throws CommonException {
		Vehicle vehicleCon = new Vehicle();
		vehicleCon.setCommunityId(vehicleVO.getCommunityId());
		vehicleCon.setPlateNo(vehicleVO.getPlateNo());
		Vehicle vehicleResult = baseMapper.selectOne(Condition.getQueryWrapper(vehicleCon));
		if(vehicleResult != null && !vehicleVO.getVehicleId().equals(vehicleResult.getVehicleId())){
			throw new CommonException(null, "社区下已经存在该车的登记信息，无法再登记相同车辆");
		}

		Vehicle vehicle = BeanUtil.copy(vehicleVO, VehicleVO.class);
		int num = baseMapper.updateById(vehicle);

        //维护附件关联关系的逻辑
        composeAttachment(vehicleVO, vehicle.getVehicleId());
		return num>0 ;
	}

	//维护附件关联关系的逻辑
	private void composeAttachment(VehicleVO vehicleVO, String id){
		if(CollectionUtils.isNotEmpty(vehicleVO.getFiles())){
			List<String> attachmentIds = vehicleVO.getFiles().stream().map(AttachmentVO::getAttachmentId).collect(Collectors.toList());
			boolean attachmentUpdateOrRemoveFlag = baseService.attachmentUpdateOrRemove(id, attachmentIds);
			if(!attachmentUpdateOrRemoveFlag){
				log.error("维护附件信息失败:" + JSON.toJSONString(vehicleVO));
			}
		}
	}

	/**
	 * 删除车辆信息
	 * @param id
	 * @return
	 * @throws CommonException
	 */
	@Override
	public boolean deleteById(String id) throws CommonException {
		return baseMapper.deleteById(id)>0;
	}

	/**
	 *批量删除车辆信息
	 * @param ids
	 * @return
	 * @throws CommonException
	 */
	@Override
	public R deleteByArrayIds(List<String> ids) throws CommonException {
		int num = baseMapper.deleteBatchIds(ids);
		return R.status(num>0);
	}

	/**
	 * 本市车数量统计
	 * @param localPlateMark
	 * @return
	 */
    @Override
    public double getLocalVehicle(String localPlateMark, String communityId) {
		return baseMapper.getLocalVehicle(localPlateMark,communityId);
    }

	/**
	 * 本省外地车数量统计
	 * @param localPlateMark
	 * @param provincePlateMark
	 * @return
	 */
	@Override
	public double getLocalProvinceOtherCityVehicle(String localPlateMark, String provincePlateMark, String communityId) {
		return baseMapper.getLocalProvinceOtherCityVehicle(localPlateMark,provincePlateMark,communityId);
	}

	/**
	 * 外省车数量统计
	 * @param provincePlateMark
	 * @return
	 */
	@Override
	public double getOtherProvinceVehicle(String provincePlateMark, String communityId) {
		return baseMapper.getOtherProvinceVehicle(provincePlateMark,communityId);
	}

	/**
	 * 根据开始时间和结束时间统计车辆数量
	 * @param monthStart
	 * @param monthEnd
	 * @return
	 */
    @Override
    public double getVehicleByStartMonthAndEndMonth(Date monthStart, Date monthEnd,String communityId) {
    	return baseMapper.getVehicleByStartMonthAndEndMonth(monthStart, monthEnd, communityId);
    }

	/**
	 * 根据车牌类型统计车辆数量
	 * @param plateType
	 * @return
	 */
	@Override
	public double getVehicleCountByPlateType(String plateType,String communityId) {
		return baseMapper.getVehicleCountByPlateType(plateType,communityId);
	}

    @Override
    public double countVehicle(String communityId) {
		return baseMapper.countVehicle(communityId);
    }
}
