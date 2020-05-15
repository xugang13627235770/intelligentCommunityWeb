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
import cn.citms.icw.dto.AttachmentsDto;
import cn.citms.icw.dto.CommunityDTO;
import cn.citms.icw.entity.Community;
import cn.citms.icw.vo.CommunityVO;
import cn.citms.icw.mapper.CommunityMapper;
import cn.citms.icw.service.ICommunityService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.beans.BladeBeanCopier;
import org.springblade.core.tool.beans.CopyProperty;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 社区信息 服务实现类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements ICommunityService {

    @Value("${dgaRpc.getDictByKindUrl}")
    private String getDictByKindUrl;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public IPage<CommunityDTO> selectCommunityPage(CommunityVO vo) {
        IPage page = new Page();
        if(vo.getPageIndex() !=null && vo.getPageIndex() >0 && vo.getPageSize() !=null &&  vo.getPageSize() >0){
            page = new Page(vo.getPageIndex().longValue(),vo.getPageSize().longValue());
        }else{
            page = new Page(1L,20L);
        }
        List<Community> communityList = baseMapper.selectCommunityPage(page, vo);
        List<CommunityDTO> dtoList = setRelationProperty(communityList);
        return page.setRecords(dtoList);
    }

    @Override
	public List<CommunityDTO> findAll() {
		return setRelationProperty(baseMapper.findAll());
	}

    @Override
    public List<Community> findAllWithoutFile() {
        return baseMapper.findAll();
    }

    @Override
    public List<String> selectCommunityIdByDeptIds(List<String> departmentIds) {
        return baseMapper.selectCommunityIdByDeptIds(departmentIds);
    }

    @Override
    public List<Community> selectCommunityInfoByDeptIds(List<String> departmentIds) {
        return baseMapper.selectCommunityInfoByDeptIds(departmentIds);
    }

    @Override
    public List<String> selectCommunityNoByDeptIds(List<String> departmentIds) {
        return baseMapper.selectCommunityNoByDeptIds(departmentIds);
    }


    // 设置关联属性
	private List<CommunityDTO> setRelationProperty(List<Community> all) {
        List<CommunityDTO> list = new ArrayList<>();
        for (Community community : all) {
            CommunityDTO dto = BeanUtil.copyProperties(community, CommunityDTO.class);
            dto.setDic_Sqlx(CommonUtils.getDictByKindAndDictNo(getDictByKindUrl, IntelligentCommunityConstant.SQLX, community.getSqlx()));
            dto.setDic_Cjsj(community.getCjsj());
            dto.setFiles(new ArrayList<AttachmentsDto>());
            list.add(dto);
        }
        return list;
	}

}
