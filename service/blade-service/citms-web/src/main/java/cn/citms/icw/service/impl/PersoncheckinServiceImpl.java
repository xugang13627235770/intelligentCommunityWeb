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
import cn.citms.icw.dto.PersoncheckinDTO;
import cn.citms.icw.entity.Personcheckin;
import cn.citms.icw.entity.StatisticTypeTotal;
import cn.citms.icw.mapper.PersoncheckinMapper;
import cn.citms.icw.service.ICommunityService;
import cn.citms.icw.service.IPersoncheckinService;
import cn.citms.icw.vo.PersoncheckinParamVO;
import cn.citms.icw.vo.SelectCntVO;
import cn.citms.mbd.basicdatacache.service.IDictionaryCacheService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoleilu.hutool.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 住户信息 服务实现类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Service
public class PersoncheckinServiceImpl extends ServiceImpl<PersoncheckinMapper, Personcheckin> implements IPersoncheckinService {

	@Value("${dgaRpc.getDictByKindUrl}")
	private String getDictByKindUrl;

	@Value("${dgaRpc.getAttachmentBySourceIdUrl}")
	private String getAttachmentBySourceIdUrl;

	@Value("${dgaRpc.getChildDepartmentIds}")
	private String getChildDepartmentIdsUrl;

	@Resource
	private ICommunityService communityService;

	@Resource
	private BaseService baseService;

	@Autowired
	private IDictionaryCacheService dictionaryCacheService;

	@Override
	public IPage<PersoncheckinDTO> selectPersoncheckinPage(IPage<PersoncheckinDTO> page, PersoncheckinParamVO vo) {
		if(vo==null || StringUtils.isBlank(vo.getCommunity_Id())){
            List<String> departmentIds = baseService.getPermissionDepartment();
            if(!CollectionUtils.isEmpty(departmentIds)){
                List<String> communityIds = communityService.selectCommunityIdByDeptIds(departmentIds);
                vo.setCommunityIds(communityIds);
            }
        }
		List<PersoncheckinDTO> dtoList = baseMapper.selectPersoncheckinPage(page, vo);
        Map<Integer, Map<String, String>> dictMap = baseService.findDict(new ArrayList<Integer>(Arrays.asList(IntelligentCommunityConstant.MZ,
                IntelligentCommunityConstant.RYLX, IntelligentCommunityConstant.XB, IntelligentCommunityConstant.YHBS,
                IntelligentCommunityConstant.ZJLX, IntelligentCommunityConstant.WHCD, IntelligentCommunityConstant.HYZK,
                IntelligentCommunityConstant.JZSY,
                IntelligentCommunityConstant.ZYLB, IntelligentCommunityConstant.ZZCS)));
        Map<String, String> mzMap = dictMap.get(IntelligentCommunityConstant.MZ);
        Map<String, String> rylxMap = dictMap.get(IntelligentCommunityConstant.RYLX);
        Map<String, String> xbMap = dictMap.get(IntelligentCommunityConstant.XB);
        Map<String, String> yhbsMap = dictMap.get(IntelligentCommunityConstant.YHBS);
        Map<String, String> zjlxMap = dictMap.get(IntelligentCommunityConstant.ZJLX);
        Map<String, String> whcdMap = dictMap.get(IntelligentCommunityConstant.WHCD);
        Map<String, String> hyzkMap = dictMap.get(IntelligentCommunityConstant.HYZK);
        Map<String, String> jzsyMap = dictMap.get(IntelligentCommunityConstant.JZSY);
        Map<String, String> zylbMap = dictMap.get(IntelligentCommunityConstant.ZYLB);
        Map<String, String> zzcsMap = dictMap.get(IntelligentCommunityConstant.ZZCS);
		if(!CollectionUtils.isEmpty(dtoList)){
			for (PersoncheckinDTO dto : dtoList) {
                dto.setDic_Mz(mzMap.get(dto.getMzdm()));
                dto.setDic_Rylx(rylxMap.get(dto.getRylx()));
                dto.setDic_Xb(xbMap.get(dto.getXbdm()));
                dto.setDic_Yhbs(yhbsMap.get(dto.getYhbs()));
                dto.setDic_Zjlx(zjlxMap.get(dto.getCyzjlxdm()));
                dto.setDic_Whcd(whcdMap.get(dto.getWhcddm()));
                dto.setDic_Hyzk(hyzkMap.get(dto.getHyzkdm()));
                dto.setDic_Jzsy(jzsyMap.get(dto.getJzsy()));
                dto.setDic_Zylb(zylbMap.get(dto.getZylbdm()));
                dto.setDic_Zzcs(zzcsMap.get(dto.getZzcs()));
                dto.setDic_Csrq(dto.getCsrq());
			}
		}
		return page.setRecords(dtoList);
	}

	@Override
	public PersoncheckinDTO getPersonCheckInDetail(String id) {
		PersoncheckinDTO dto = baseMapper.getPersonCheckInDetail(id);
        Map<Integer, Map<String, String>> dictMap = baseService.findDict(new ArrayList<Integer>(Arrays.asList(IntelligentCommunityConstant.MZ,
                IntelligentCommunityConstant.RYLX, IntelligentCommunityConstant.XB, IntelligentCommunityConstant.YHBS,
                IntelligentCommunityConstant.ZJLX, IntelligentCommunityConstant.WHCD, IntelligentCommunityConstant.HYZK,
                IntelligentCommunityConstant.JZSY,
                IntelligentCommunityConstant.ZYLB, IntelligentCommunityConstant.ZZCS)));
        Map<String, String> mzMap = dictMap.get(IntelligentCommunityConstant.MZ);
        Map<String, String> rylxMap = dictMap.get(IntelligentCommunityConstant.RYLX);
        Map<String, String> xbMap = dictMap.get(IntelligentCommunityConstant.XB);
        Map<String, String> yhbsMap = dictMap.get(IntelligentCommunityConstant.YHBS);
        Map<String, String> zjlxMap = dictMap.get(IntelligentCommunityConstant.ZJLX);
        Map<String, String> whcdMap = dictMap.get(IntelligentCommunityConstant.WHCD);
        Map<String, String> hyzkMap = dictMap.get(IntelligentCommunityConstant.HYZK);
        Map<String, String> jzsyMap = dictMap.get(IntelligentCommunityConstant.JZSY);
        Map<String, String> zylbMap = dictMap.get(IntelligentCommunityConstant.ZYLB);
        Map<String, String> zzcsMap = dictMap.get(IntelligentCommunityConstant.ZZCS);
        dto.setDic_Mz(mzMap.get(dto.getMzdm()));
        dto.setDic_Rylx(rylxMap.get(dto.getRylx()));
        dto.setDic_Xb(xbMap.get(dto.getXbdm()));
        dto.setDic_Yhbs(yhbsMap.get(dto.getYhbs()));
        dto.setDic_Zjlx(zjlxMap.get(dto.getCyzjlxdm()));
        dto.setDic_Whcd(whcdMap.get(dto.getWhcddm()));
        dto.setDic_Hyzk(hyzkMap.get(dto.getHyzkdm()));
        dto.setDic_Jzsy(jzsyMap.get(dto.getJzsy()));
        dto.setDic_Zylb(zylbMap.get(dto.getZylbdm()));
        dto.setDic_Zzcs(zzcsMap.get(dto.getZzcs()));
        dto.setDic_Csrq(dto.getCsrq());
		return dto;
	}

	@Override
	public List<PersoncheckinDTO> getAllPersonCheckin() {
		List<PersoncheckinDTO> dtoList = baseMapper.getAllPersonCheckin();
		if(!CollectionUtils.isEmpty(dtoList)){
			Map<Integer, Map<String, String>> dictMap = baseService.findDict(new ArrayList<Integer>(Arrays.asList(IntelligentCommunityConstant.MZ,
					IntelligentCommunityConstant.RYLX, IntelligentCommunityConstant.XB, IntelligentCommunityConstant.YHBS,
					IntelligentCommunityConstant.ZJLX, IntelligentCommunityConstant.WHCD, IntelligentCommunityConstant.HYZK,
					IntelligentCommunityConstant.JZSY,
					IntelligentCommunityConstant.ZYLB, IntelligentCommunityConstant.ZZCS)));
            Map<String, String> mzMap = dictMap.get(IntelligentCommunityConstant.MZ);
            Map<String, String> rylxMap = dictMap.get(IntelligentCommunityConstant.RYLX);
            Map<String, String> xbMap = dictMap.get(IntelligentCommunityConstant.XB);
            Map<String, String> yhbsMap = dictMap.get(IntelligentCommunityConstant.YHBS);
            Map<String, String> zjlxMap = dictMap.get(IntelligentCommunityConstant.ZJLX);
            Map<String, String> whcdMap = dictMap.get(IntelligentCommunityConstant.WHCD);
            Map<String, String> hyzkMap = dictMap.get(IntelligentCommunityConstant.HYZK);
            Map<String, String> jzsyMap = dictMap.get(IntelligentCommunityConstant.JZSY);
            Map<String, String> zylbMap = dictMap.get(IntelligentCommunityConstant.ZYLB);
            Map<String, String> zzcsMap = dictMap.get(IntelligentCommunityConstant.ZZCS);
            for (PersoncheckinDTO dto : dtoList) {
                dto.setDic_Mz(mzMap.get(dto.getMzdm()));
                dto.setDic_Rylx(rylxMap.get(dto.getRylx()));
                dto.setDic_Xb(xbMap.get(dto.getXbdm()));
                dto.setDic_Yhbs(yhbsMap.get(dto.getYhbs()));
                dto.setDic_Zjlx(zjlxMap.get(dto.getCyzjlxdm()));
                dto.setDic_Whcd(whcdMap.get(dto.getWhcddm()));
                dto.setDic_Hyzk(hyzkMap.get(dto.getHyzkdm()));
                dto.setDic_Jzsy(jzsyMap.get(dto.getJzsy()));
                dto.setDic_Zylb(zylbMap.get(dto.getZylbdm()));
                dto.setDic_Zzcs(zzcsMap.get(dto.getZzcs()));
                dto.setDic_Csrq(dto.getCsrq());
			}
		}
		return dtoList;
	}

    @Override
    public double countPerson(String communityId) {
		return baseMapper.countPerson(communityId);
    }

	@Override
	public Map<String, Integer> getCheckInRylxCnt(List<String> communityIds) {
		return getCheckInCnt(communityIds, "rylx");
	}

	@Override
	public Map<String, Integer> getCheckInYhbsCnt(List<String> communityIds) {
		return getCheckInCnt(communityIds, "yhbs");
	}

	@Override
	public Map<String, Integer> getCheckInZdryCnt(List<String> communityIds) {
		return getCheckInCnt(communityIds, "zdry");
	}

	@Override
	public Integer getCheckInCnt(List<String> communityIds) {
		if(CollUtil.isEmpty(communityIds)) {
			return 0;
		}
		return baseMapper.getCheckInCnt(communityIds);
	}

	@Override
	public Map<String, Integer> findGroupByHouseId() {
		return getCheckInCnt(new ArrayList<>(0), "houseId");
	}

	@Override
	public List<StatisticTypeTotal> getPersoncheckinByTime(List<String> communityIds) {
		if(CollUtil.isEmpty(communityIds)) {
			return new ArrayList<>(0);
		}
		List<StatisticTypeTotal> list = baseMapper.getPersoncheckinByTime(communityIds);
		if(CollUtil.isEmpty(list)) {
			return new ArrayList<>(0);
		}
		int total = 0;
		for (StatisticTypeTotal typeTotal : list) {
			total += typeTotal.getCount();
			typeTotal.setTotal(total);
		}
		return list;
	}

	private Map<String, Integer> getCheckInCnt(List<String> communityIds, final String type){
		if(CollUtil.isEmpty(communityIds) && !"houseId".equals(type)) {
			return new HashMap<>(0);
		}
		List<SelectCntVO> list = new ArrayList<>(10);
		if("rylx".equals(type)) {
			list = baseMapper.getCheckInRylxCnt(communityIds);
		} else if("yhbs".equals(type)) {
			list = baseMapper.getCheckInYhbsCnt(communityIds);
		} else if("zdry".equals(type)) {
			list = baseMapper.getCheckInYhbsCnt(communityIds);
		} else if("houseId".equals(type)) {
			list = baseMapper.findGroupByHouseId();
		}
		if(CollUtil.isEmpty(list)) {
			return new HashMap<>(0);
		}
		Map<String, Integer> resMap = new HashMap<>(list.size() * 2);
		for (SelectCntVO vo : list) {
			resMap.put(vo.getName(), vo.getValue());
		}
		return resMap;
	}

}
