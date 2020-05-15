package cn.citms.icw.dao;

import cn.citms.icw.dto.EsPersonTrafficflowDTO;
import cn.citms.icw.dto.EsVehicleTrafficflowDTO;
import lombok.extern.slf4j.Slf4j;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.MapRestResponse;
import org.springblade.common.constant.CitmsAppConstant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component("vehicleTrafficFlowDao")
public class VehicleTrafficFlowDao {

    /**
     * 根据时间区间分组统计车流的数量
     *
     * @return EsVehicleTrafficflowDTO  返回值
     */
    public List<Map<String,Object>> getTimeAreaPartollogCount(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_VEHICLE_TRAFFICFLOW_PATH);
        MapRestResponse restResponse = clientUtil.search(CitmsAppConstant.VEHICLE_TRAFFICFLOW_CLUSTER_ES_INDEX_SEARCH,
                "getVehicleFlowCountGroupByTimeArea",
                params);
        List<Map<String,Object>> vehicleFlowStatics = (List<Map<String,Object>>)restResponse.getAggBuckets("groupDate");
        return vehicleFlowStatics;
    }

    public Map<String,Map<String,Object>>  queryVehicleTrafficflowStatisticsByCount(Map<String, Object> params){
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_VEHICLE_TRAFFICFLOW_PATH);
        MapRestResponse restResponse = clientUtil.search(CitmsAppConstant.VEHICLE_TRAFFICFLOW_CLUSTER_ES_INDEX_SEARCH,
                "queryVehicleTrafficflowStatisticsByCount",
                params);
        return restResponse.getAggregations();
    }

}
