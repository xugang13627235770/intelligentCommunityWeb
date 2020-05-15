package cn.citms.icw.service.impl;

import cn.citms.icw.constant.IntelligentCommunityConstant;
import cn.citms.icw.entity.Community;
import cn.citms.icw.service.IBaseService;
import cn.citms.icw.service.ICommunityService;
import com.alibaba.fastjson.JSON;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 *      基础服务 实现类
 * </pre>
 *
 * @author liuyuyang
 */
@Service
public class BaseServiceImpl implements IBaseService {
    private final StringRedisTemplate stringRedisTemplate;
    private final ICommunityService communityService;

    @Autowired
    public BaseServiceImpl(StringRedisTemplate stringRedisTemplate, ICommunityService communityService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.communityService = communityService;

    }

    @Override
    public Community getCommunityInfo(String code) {
        String codeKey = StrUtil.format("{}{}", IntelligentCommunityConstant.REDIS_COMMUNITY_PREFIX, code);
        String s = stringRedisTemplate.opsForValue().get(codeKey);
        if(StrUtil.isNotEmpty(s)){
            return JSON.parseObject(s,Community.class);
        }
        return  new Community();
    }

    /**
     * 定时刷新缓存
     */
    @Scheduled(initialDelay = 5000,fixedRate = 1200000)
    public void syncCommunity(){
        System.out.println(new Date());
            List<Community> lcs = communityService.list();
            lcs.parallelStream().forEach(c -> {
                String key = StrUtil.format("{}{}", IntelligentCommunityConstant.REDIS_COMMUNITY_PREFIX, c.getXqbm());
                stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(c), Duration.ofHours(1));
            });

    }

}
