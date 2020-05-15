package cn.citms.icw.dao;

import cn.citms.icw.dto.EsPersonTrafficflowDTO;
import cn.citms.icw.entity.ESPatrollog;
import cn.citms.icw.entity.StatisticTypeCount;
import com.xiaoleilu.hutool.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.MapRestResponse;
import org.springblade.common.constant.CitmsAppConstant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("personTrafficFlowDao")
public class PersonTrafficFlowDao<main> {

    /**
     * 根据时间区间分组统计人流的数量
     *
     * @return EsPersonTrafficflowDTO  返回值
     */
    public List<Map<String, Object>> getTimeAreaPartollogCount(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_PERSON_TRAFFICFLOW_PATH);
        MapRestResponse restResponse = clientUtil.search(CitmsAppConstant.PERSON_TRAFFICFLOW_CLUSTER_ES_INDEX_SEARCH,
                "getPersonFlowCountGroupByTimeArea",
                params);
        List<Map<String, Object>> prsonFlowStatics = (List<Map<String, Object>>) restResponse.getAggBuckets("groupDate");
        return prsonFlowStatics;
    }

    public Map<String, Map<String, Object>> queryPersonTrafficflowStatisticsByCount(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_PERSON_TRAFFICFLOW_PATH);
        MapRestResponse restResponse = clientUtil.search(CitmsAppConstant.PERSON_TRAFFICFLOW_CLUSTER_ES_INDEX_SEARCH,
                "queryPersonTrafficflowStatisticsByCount",
                params);
        return restResponse.getAggregations();
    }


}
