package cn.citms.icw.dao;

import cn.citms.icw.entity.ESPatrollog;
import lombok.extern.slf4j.Slf4j;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springblade.common.constant.CitmsAppConstant;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("eSPatrollogDao")
public class ESPatrollogDao {

    /**
     * 获取指定时间区间的巡更数量
     *
     * @return ESPatrollog  返回值
     */
    public ESDatas<ESPatrollog> getTimeAreaPartollogCount(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_PATROLLOG_PATH);
        return clientUtil.searchList(CitmsAppConstant.PATROLLOG_CLUSTER_ES_INDEX_SEARCH,
               "getTimeAreaPartollogCount",
                params,
                ESPatrollog.class);
    }

    /**
     * 查询巡更记录-分页
     * @param params
     * @return
     */
    public ESDatas<ESPatrollog> getPatrollogInfos(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_PATROLLOG_PATH);
        return clientUtil.searchList(CitmsAppConstant.PATROLLOG_CLUSTER_ES_INDEX_SEARCH,
                CitmsAppConstant.SEARCH_PATROLLOG_INFO,
                params,
                ESPatrollog.class);
    }

    /**
     * 查询巡更记录-详情
     * @param params
     * @return
     */
    public ESDatas<ESPatrollog> getPatrollogById(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_PATROLLOG_PATH);
        return clientUtil.searchList(CitmsAppConstant.PATROLLOG_CLUSTER_ES_INDEX_SEARCH,
                CitmsAppConstant.SEARCH_PATROLLOG_BYID,
                params,
                ESPatrollog.class);
    }

}
