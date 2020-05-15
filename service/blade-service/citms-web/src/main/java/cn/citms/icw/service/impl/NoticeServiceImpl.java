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
import cn.citms.icw.entity.Community;
import cn.citms.icw.entity.Notice;
import cn.citms.icw.mapper.CommunityMapper;
import cn.citms.icw.mapper.NoticeMapper;
import cn.citms.icw.service.INoticeService;
import cn.citms.icw.vo.NoticeVO;
import cn.citms.mbd.basicdatacache.service.IDictionaryCacheService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 通知公告表 服务实现类
 *
 * @author Blade
 * @since 2020-04-22
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

	@Autowired
	private IDictionaryCacheService dictionaryCacheService;
	@Autowired
	private BaseService baseService;

	@Resource
	private CommunityMapper communityMapper;

	@Override
	public IPage<NoticeVO> selectNoticePage(NoticeVO vo) {
		int pageIndex = vo.getPageIndex() == null ? 1 : vo.getPageIndex();
		int pageSize = (vo.getPageSize() == null || vo.getPageSize() == 0) ? 20 : vo.getPageSize();
		IPage<NoticeVO> page = new Page<>(pageIndex, pageSize);
		if(StrUtil.isBlank(vo.getSortField())) {
			vo.setSortField("pubTime");
			vo.setSortOrder("desc");
		}
		//获取当前部门能够查看的社区数据
		if(StrUtil.isBlank(vo.getCommunityId())) {
			List<String> departmentIds = baseService.getPermissionDepartment();
			if(CollUtil.isNotEmpty(departmentIds)) {
				List<String> communityIds = communityMapper.selectCommunityIdByDeptIds(departmentIds);
				vo.setCommunityIds(communityIds);
			}
		}
		IPage<Notice> pages = baseMapper.selectNoticePage(page, vo);
		if(pages != null && CollUtil.isNotEmpty(pages.getRecords())) {
			List<NoticeVO> result = new ArrayList<>(pages.getRecords().size());
			List<String> ids = new ArrayList<>(pages.getRecords().size());
			for (Notice record : pages.getRecords()) {
				result.add(convert2VO(record));
				ids.add(record.getCommunityId());
			}
			Map<String, String> map = getCommunityNames(ids);
			result.forEach(v -> v.setSqmc(map.get(v.getCommunityId())));
			page.setRecords(result);
		}
		return page;
	}

	@Override
	public List<NoticeVO> selectAll() {
		List<Notice> list = baseMapper.selectAll();
		if(CollUtil.isNotEmpty(list)) {
			List<NoticeVO> result = new ArrayList<>(list.size());
			List<String> ids = new ArrayList<>(list.size());
			for (Notice record : list) {
				result.add(convert2VO(record));
				ids.add(record.getCommunityId());
			}
			Map<String, String> map = getCommunityNames(ids);
			result.forEach(v -> v.setSqmc(map.get(v.getCommunityId())));
			return result;
		}
		return new ArrayList<>(0);
	}

	@Override
	public R<NoticeVO> selectById(String id) {
		Notice notice = baseMapper.selectById(id);
		if(notice == null) {
			return R.fail("不存在id为【"+id+"】的公告信息");
		}
		NoticeVO vo = convert2VO(notice);
		// 社区名称
		Community community = communityMapper.selectCommunityNameById(vo.getCommunityId());
		if(community != null) {
			vo.setSqmc(community.getXqmc());
		}
		return R.data(vo);
	}

	@Override
	public R<NoticeVO> findByIdOrder(String id, String type) {
		List<String> departmentIds = baseService.getPermissionDepartment();
		List<String> communityIds = communityMapper.selectCommunityIdByDeptIds(departmentIds);
		Notice notice = baseMapper.selectById(id);
		if(notice == null) {
			return R.fail("不存在id为【"+id+"】的公告信息");
		}
		List<Notice> list = baseMapper.selectAll();
		Notice notice1;
		if("1".equals(type)) {
			notice1 = list.stream().filter(e -> communityIds.contains(e.getCommunityId()) && e.getPubTime().compareTo(notice.getPubTime()) > 0)
					.min(Comparator.comparing(Notice::getPubTime)).orElse(null);
			if(notice1 == null) {
				notice1 = list.stream().filter(e -> communityIds.contains(e.getCommunityId()) && e.getPubTime().compareTo(notice.getPubTime()) < 0)
						.min(Comparator.comparing(Notice::getPubTime)).orElse(null);
			}
		} else {
			notice1 = list.stream().filter(e -> communityIds.contains(e.getCommunityId()) && e.getPubTime().compareTo(notice.getPubTime()) < 0)
					.max(Comparator.comparing(Notice::getPubTime)).orElse(null);
			if(notice1 == null) {
				notice1 = list.stream().filter(e -> communityIds.contains(e.getCommunityId()) && e.getPubTime().compareTo(notice.getPubTime()) > 0)
						.max(Comparator.comparing(Notice::getPubTime)).orElse(null);
			}
		}
		NoticeVO vo = convert2VO(notice1);
		if(vo != null) {
			Community community = communityMapper.selectCommunityNameById(vo.getCommunityId());
			if(community != null) {
				vo.setSqmc(community.getXqmc());
			}
		}
		return R.data(vo);
	}

	@Override
	public List<NoticeVO> findByUserCode(String userCode) {
		List<Notice> list = baseMapper.findByUserCode(userCode);
		if(CollUtil.isNotEmpty(list)) {
			List<NoticeVO> result = new ArrayList<>(list.size());
			List<String> ids = new ArrayList<>(list.size());
			for (Notice notice : list) {
				ids.add(notice.getCommunityId());
				result.add(convert2VO(notice));
			}
			Map<String, String> map = getCommunityNames(ids);
			result.forEach(v -> v.setSqmc(map.get(v.getCommunityId())));
			return result;
		}
		return new ArrayList<>(0);
	}

	@Override
	public R<String> saveNotice(NoticeVO vo) {
		String noticeId;
		int result;
		if(StrUtil.isBlank(vo.getNoticeId())) {
			Notice notice = BeanUtil.copy(vo, Notice.class);
			noticeId = CommonUtils.createUUID();
			notice.setNoticeId(noticeId);
			notice.setPubTime(new Date());
			result = baseMapper.insert(notice);
		} else {
			noticeId = vo.getNoticeId();
			Notice notice = baseMapper.selectById(noticeId);
			if(notice == null) {
				return R.fail("不存在id为【"+noticeId+"】的公告数据");
			}
			notice.setTitle(vo.getTitle());
			notice.setContent(vo.getContent());
			notice.setUserCode(vo.getUserCode());
			notice.setNoticeType(vo.getNoticeType());
			notice.setDueTime(vo.getDueTime());
			notice.setCommunityId(vo.getCommunityId());
			notice.setDqjd(vo.getDqjd());
			notice.setDqwd(vo.getDqwd());
			result = baseMapper.updateById(notice);
		}
		if(result == 0) {
			return R.fail("保存失败");
		}
		return R.data(noticeId);
	}

	@Override
	public R<String> removeById(String id) {
		Notice notice = baseMapper.selectById(id);
		if(notice == null) {
			return R.fail("不存在id【"+id+"】的公告数据");
		}
		int result = baseMapper.deleteById(id);
		return R.status(result > 0);
	}

	@Override
	public String deleteByBuildingIds(List<String> ids) {
		int delTotal = 0;
		for (String id : ids) {
			Notice notice = baseMapper.selectById(id);
			if(notice != null) {
				int result = baseMapper.deleteById(id);
				if(result > 0) {
					delTotal++;
				}
			}
		}
		return "成功删除【" + delTotal + "】条数据";
	}

	private NoticeVO convert2VO(Notice record){
		if(record == null) {
			return null;
		}
		NoticeVO vo = BeanUtil.copy(record, NoticeVO.class);
		try {
			vo.setDic_NoticeType(dictionaryCacheService.getDictionaryNameByKindNo(String.valueOf(IntelligentCommunityConstant.GGLX), vo.getNoticeType()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	/**
	 * 查询社区的名称
	 * @param ids
	 * @return
	 */
	private Map<String, String> getCommunityNames(List<String> ids){
		List<Community> communityList = communityMapper.selectCommunityByIdIn(ids);
		Map<String, String> map = new HashMap<>(16);
		if(CollUtil.isNotEmpty(communityList)) {
			for (Community community : communityList) {
				map.put(community.getId(), community.getXqmc());
			}
		}
		return map;
	}

}
