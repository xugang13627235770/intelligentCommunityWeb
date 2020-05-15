package cn.citms.icw.dao;

import cn.citms.icw.dto.StatisticTypeCountDTO;
import cn.citms.icw.dto.StatisticTypeTotalDTO;
import cn.citms.icw.entity.EsAlarm;
import cn.citms.icw.vo.AlarmStatisticVO;
import cn.citms.mbd.basicdatacache.service.IDictionaryCacheService;
import lombok.extern.slf4j.Slf4j;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.MapRestResponse;
import org.frameworkset.elasticsearch.entity.MapSearchHits;
import org.springblade.common.constant.CitmsAppConstant;
import org.springblade.common.enums.DictEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static cn.citms.icw.Utils.CommonUtils.VBD_POOL;

/**
 * <pre>
 *     es 报警dao
 * </pre>
 *
 * @author liuyuyang
 */
@Slf4j
@Component("esAlarmDao")
public class EsAlarmDao {

    private final IDictionaryCacheService dictionaryCacheService;
    private final static long OUT_TIME = 5000L;

    @Autowired
    public EsAlarmDao(IDictionaryCacheService dictionaryCacheService) {
        this.dictionaryCacheService = dictionaryCacheService;
    }

    /**
     * 根据参数查询报警数据
     *
     * @param params {@link Map} 参数
     * @return EsAlarm {@link EsAlarm} 返回值
     */
    public ESDatas<EsAlarm> getAlarmInfos(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_ALARM_PATH);
        return clientUtil.searchList(CitmsAppConstant.ALARM_CLUSTER_ES_INDEX_SEARCH,
                CitmsAppConstant.SEARCH_ALARM,
                params,
                EsAlarm.class);
    }

    /**
     * 根据参数查询报警数据
     *
     * @param params {@link Map} 参数
     * @return EsAlarm {@link EsAlarm} 返回值
     */
    public ESDatas<EsAlarm> findById(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_ALARM_PATH);
        return clientUtil.searchList(CitmsAppConstant.ALARM_CLUSTER_ES_INDEX_SEARCH,
                CitmsAppConstant.SEARCH_ALARM_BY_ID,
                params,
                EsAlarm.class);
    }

    /**
     * 统计预警数据
     *
     * @param params {@link Map} 参数
     * @return AlarmStatisticVO {@link AlarmStatisticVO} 返回值
     */
    public List<AlarmStatisticVO> queryAlarmStatistic(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_ALARM_PATH);
        List<AlarmStatisticVO> avs = new CopyOnWriteArrayList<>();
        AtomicReference<String> total = new AtomicReference<>();
        CompletableFuture<List<StatisticTypeCountDTO>> f = CompletableFuture.supplyAsync(() -> {
            MapRestResponse restResponseByAlarmType = clientUtil.search(CitmsAppConstant.ALARM_CLUSTER_ES_INDEX_SEARCH,
                    "queryAlarmStatisticByAlarmType", params);
            MapSearchHits searchHit = restResponseByAlarmType.getSearchHits();
            total.set(searchHit.getTotal().toString());
            List<Map<String, Object>> alarmTypeStatics = (List<Map<String, Object>>) restResponseByAlarmType.getAggBuckets("terms_alarmType");
            return alarmTypeStatics.parallelStream().map(c -> {
                        StatisticTypeCountDTO stDto = new StatisticTypeCountDTO();
                        stDto.setCount((int) c.get("doc_count"));
                        stDto.setType(dictionaryCacheService.getDictionaryNameByKindNo(
                                DictEnum.YJLX.getIndex(), (String) c.get("key")));
                        return stDto;
                    }
            ).collect(Collectors.toList());
        }, VBD_POOL);

        CompletableFuture<List<StatisticTypeTotalDTO>> f1 = CompletableFuture.supplyAsync(() -> {
            MapRestResponse restResponse = clientUtil.search(CitmsAppConstant.ALARM_CLUSTER_ES_INDEX_SEARCH,
                    "queryAlarmStatistic", params);
            List<Map<String, Object>> dataGroup = (List<Map<String, Object>>) restResponse.getAggBuckets("groupDate");
            return dataGroup.parallelStream().map(c ->
                    StatisticTypeTotalDTO.builder().count((int) c.get("doc_count")).total((int) c.get("doc_count")).type((String) c.get("key_as_string")).build()
            ).collect(Collectors.toList());
        }, VBD_POOL);
        try {
            avs.add(AlarmStatisticVO.builder().alarmTypeTj(f.get()).data(f1.get()).sqmc("全局").alarmTotal(Double.parseDouble(total.get())).build());
        } catch (InterruptedException | ExecutionException e) {
            log.error("执行线程错误：{}",e.getMessage());
        }
        return avs;
    }


}
