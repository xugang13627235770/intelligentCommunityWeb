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

import cn.citms.icw.Utils.CommonUtils;
import cn.citms.icw.constant.IntelligentCommunityConstant;
import cn.citms.icw.dto.CommunityDTO;
import cn.citms.icw.entity.Building;
import cn.citms.icw.entity.CommunityDevice;
import cn.citms.icw.mapper.BuildingMapper;
import cn.citms.icw.service.ICommunityDeviceService;
import cn.citms.icw.service.impl.BaseService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.annotation.Resource;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.utils.UserContextHolder;
import org.springblade.common.vo.UserRightViewModel;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.citms.icw.entity.Community;
import cn.citms.icw.vo.CommunityVO;
import cn.citms.icw.service.ICommunityService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 社区信息 控制器
 *
 * @author Blade
 * @since 2020-04-22
 */
@Slf4j
@RestController
@RequestMapping("/Community")
@Api(value = "社区信息", tags = "社区信息接口")
public class CommunityController  {

    @Value("${dgaRpc.getDictByKindUrl}")
    private String getDictByKindUrl;

    @Value("${dgaRpc.getAttachmentBySourceIdUrl}")
    private String getAttachmentBySourceIdUrl;

    @Value("${dgaRpc.getChildDepartmentIds}")
    private String getChildDepartmentIdsUrl;

    @Value("${dgaRpc.UpdateDeviceDepartmentUrl}")
    private String UpdateDeviceDepartmentUrl;

    @Resource
    private ICommunityService communityService;

    @Resource
    private BuildingMapper buildingMapper;

    @Resource
    private ICommunityDeviceService communityDeviceService;

    @Resource
    private BaseService baseService;

    /**
     * 详情
     */
    @GetMapping("/FindById")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入community")
    public R<CommunityDTO> detail(String id) {
        if(StringUtils.isBlank(id)){
            return R.fail("参数id空异常");
        }
        Community community = communityService.getById(id);
        if(community == null){
            return R.fail("不存在id为" + id + "的社区信息");
        }
        CommunityDTO detail = BeanUtil.copyProperties(community, CommunityDTO.class);
        // 字典转换
        detail.setDic_Sqlx(CommonUtils.getDictByKindAndDictNo(getDictByKindUrl, IntelligentCommunityConstant.SQLX, detail.getSqlx()));
        detail.setDic_Cjsj(detail.getCjsj());
        detail.setDic_Ssxq(detail.getXqmc());
        // 获取附件信息
        detail.setFiles(CommonUtils.getAttachmentBySourceId(getAttachmentBySourceIdUrl,detail.getId()));
        return R.data(detail);
    }

    /**
     * 分页 社区信息
     */
    @PostMapping("/Query")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入community")
    public R<IPage<CommunityDTO>> list(@RequestBody CommunityVO vo) {
        UserRightViewModel userInfo = UserContextHolder.getUserInfo();
        if(userInfo != null && StringUtils.isNotBlank(userInfo.getDepartmentId())){
            List<String> departmentIds = CommonUtils.getChildDepartmentIds(getChildDepartmentIdsUrl, userInfo.getDepartmentId());
            if(!CollectionUtils.isEmpty(departmentIds)){
                List<String> communityIds = communityService.selectCommunityIdByDeptIds(departmentIds);
                if(!CollectionUtils.isEmpty(communityIds)){
                    vo.setCommunityIds(communityIds);
                }
            }
        }
        IPage<CommunityDTO> pages = communityService.selectCommunityPage(vo);
        return R.data(pages);
    }


    /**
     * 查询所有社区信息接口
     */
    @GetMapping("/FindAll")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "查询所有社区信息接口")
    public R<List<CommunityDTO>> findAll() {
        List<CommunityDTO> list =  communityService.findAll();
        return R.data(list);
    }

    /**
     * 保存社区信息
     */
    @PostMapping("/SaveCommunity")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "保存社区信息", notes = "传入community")
    public R save(@Valid @RequestBody CommunityDTO dto) {
        if(dto == null){
            return R.fail("待保存的社区信息不存在");
        }
        Community community =(Community)dto;
        String keyId = "";
        boolean operationFlag = true;
        if(StringUtils.isBlank(community.getId())){
            keyId = UUID.randomUUID().toString().replaceAll("-","");
            community.setId(keyId);
            operationFlag = communityService.save(community);
        }else{
            keyId = community.getId();
            Community result = communityService.getById(keyId);
            if(result == null){
                return R.fail("不存在id为【"+ community.getId() +"】的社区数据");
            }
            // 所属辖区修改的逻辑
            UpdateDeviceDepartment(dto,  result);
            operationFlag = communityService.updateById(community);
        }
        //  维护附件关联关系的逻辑
        if(!CollectionUtils.isEmpty(dto.getFiles())){
            boolean attachmentUpdateOrRemoveFlag = baseService.attachmentUpdateOrRemove(keyId, dto.getFiles().stream() .filter(el -> StringUtils.isNotBlank(el.getAttachmentId())).map(el -> el.getAttachmentId()).collect(Collectors.toList()));
            if(!attachmentUpdateOrRemoveFlag){
                log.error("社区维护附件信息失败:" + JSON.toJSONString(dto));
            }
        }
        return R.status(operationFlag);
    }

    // 所属辖区修改的逻辑
    private void UpdateDeviceDepartment(CommunityDTO dto, Community result) {
        if(StringUtils.isNotBlank(dto.getSsxq()) && !dto.getSsxq().equals(result.getSsxq())){
            List<CommunityDevice> communityDeviceList = communityDeviceService.getInfoByCommunityId(dto.getId());
            List<String> deviceIdList = new ArrayList<>();
            if(!CollectionUtils.isEmpty(communityDeviceList)){
                deviceIdList = communityDeviceList.stream()
                        .filter(el -> StringUtils.isNotBlank(el.getDeviceId()))
                        .map(el -> el.getDeviceId()).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(deviceIdList)){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("deviceIds", deviceIdList);
                    jsonObject.put("departmentId", dto.getSsxq());
                    try {
                        String postResult = CommonUtils.sendPost(UpdateDeviceDepartmentUrl, jsonObject.toJSONString());
                        log.info("社区进行批量修改部门关联的设备响应结果：" + postResult);
                    } catch (Exception e) {
                        log.error("社区进行批量修改部门关联的设备失败" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 删除社区信息
     */
    @DeleteMapping("/DeleteById")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "删除社区信息", notes = "id")
    public R deleteById(String id) {
        if(StringUtils.isBlank(id)){
            return R.fail("入参id不存在");
        }
        Community community = communityService.getById(id);
        if(community == null){
            R.fail("不存在id"+ id +"的社区数据");
        }
        return R.status(communityService.removeById(id));
    }

    /**
     * 批量删除社区信息
     */
    @DeleteMapping("/DeleteByArrayIds")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "批量删除社区信息", notes = "传入ids")
    public R deleteByArrayIds(@ApiParam(value = "主键集合", required = true) @RequestParam List<String> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return R.fail("入参为空");
        }
        int delTotal = 0;
        int count = 0;
        for (String id : ids) {
            if(StringUtils.isNotBlank(id)){
                List<Building> buildingList = buildingMapper.getByCommunityId(id);
                if(!CollectionUtils.isEmpty(buildingList)){
                    count++;
                }else{
                    communityService.removeById(id);
                    delTotal ++;
                }
            }
        }
        return R.success("共" + ids.size() + "条数据，成功删除【" + delTotal + "】条"+",【" + count + "】条绑定楼栋信息，无法删除");
    }


    /**
     * 查询所有社区信息接口
     */
    @GetMapping("/FindAllWithoutFile")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "查询所有社区信息接口")
    public R<List<Community>> findAllWithoutFile() {
        List<Community> list =  communityService.findAllWithoutFile();
        return R.data(list);
    }


}
