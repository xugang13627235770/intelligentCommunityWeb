package cn.citms.icw.dao;

import cn.citms.icw.dto.StatisticTypeCountDTO;
import cn.citms.icw.dto.StatisticTypeTotalDTO;
import cn.citms.icw.dto.VPSYMDDTO;
import cn.citms.icw.entity.EsAlarm;
import cn.citms.icw.entity.EsPerson;
import cn.citms.icw.entity.EsVehicle;
import cn.citms.icw.vo.AlarmStatisticVO;
import cn.citms.icw.vo.VPSYMDVO;
import cn.citms.mbd.basicdatacache.service.IDictionaryCacheService;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.MapRestResponse;
import org.springblade.common.constant.CitmsAppConstant;
import org.springblade.common.enums.DictEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static cn.citms.icw.Utils.CommonUtils.VBD_POOL;

/**
 * <pre>
 *     人员dao
 * </pre>
 *
 * @author liuyuyang
 */
@Slf4j
@Component("esVehicleDao")
public class EsVehicleDao {
    private final IDictionaryCacheService dictionaryCacheService;

    @Autowired
    public EsVehicleDao(IDictionaryCacheService dictionaryCacheService) {
        this.dictionaryCacheService = dictionaryCacheService;
    }

    /**
     * 根据参数查询人员通过数据
     *
     * @param params {@link Map} 参数
     * @return EsAlarm {@link EsAlarm} 返回值
     */
    public ESDatas<EsVehicle> queryVehicle(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_VEHICLE_PATH);
        return clientUtil.searchList(CitmsAppConstant.VEHICLE_CLUSTER_ES_INDEX_SEARCH,
                CitmsAppConstant.SEARCH_VEHICLE,
                params,
                EsVehicle.class);
    }


    /**
     * 统计预警数据
     *
     * @param params {@link Map} 参数
     * @return AlarmStatisticVO {@link AlarmStatisticVO} 返回值
     */
    public List<VPSYMDDTO> queryVehiclePassStatistic(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_VEHICLE_PATH);
        List<VPSYMDDTO> avs = new CopyOnWriteArrayList<>();
        CompletableFuture<List<VPSYMDVO>> f = CompletableFuture.supplyAsync(() -> {
            MapRestResponse restResponseByVehicle = clientUtil.search(CitmsAppConstant.VEHICLE_CLUSTER_ES_INDEX_SEARCH,
                    "queryVehicleStatistic", params);
            List<Map<String, Object>> alarmTypeStatics = (List<Map<String, Object>>) restResponseByVehicle.getAggBuckets("groupDate");
            return alarmTypeStatics.parallelStream().map(c -> {
                        VPSYMDVO stDto = new VPSYMDVO();
                        stDto.setVehilePassTotal((int) c.get("doc_count"));
                        stDto.setTjsj((String) c.get("key"));
                        return stDto;
                    }
            ).collect(Collectors.toList());
        }, VBD_POOL);

        ClientInterface clientUtilPerson = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_PERSON_PATH);
        CompletableFuture<List<VPSYMDVO>> f1 = CompletableFuture.supplyAsync(() -> {
            MapRestResponse restResponseByPerson = clientUtilPerson.search(CitmsAppConstant.PERSON_CLUSTER_ES_INDEX_SEARCH,
                    "queryPersonStatistic", params);
            List<Map<String, Object>> alarmTypeStatics = (List<Map<String, Object>>) restResponseByPerson.getAggBuckets("groupDate");
            return alarmTypeStatics.parallelStream().map(c -> {
                        VPSYMDVO stDto = new VPSYMDVO();
                        stDto.setPersonPassTotal((int) c.get("doc_count"));
                        stDto.setTjsj((String) c.get("key_as_string"));
                        return stDto;
                    }
            ).collect(Collectors.toList());
        }, VBD_POOL);
        try {
            f.get().addAll(f1.get());
            List<VPSYMDVO> lvs = f.get().parallelStream().filter(v -> StrUtil.isNotEmpty(v.getTjsj())).collect(Collectors.toList());
            VPSYMDDTO dto = new VPSYMDDTO();
            dto.setSqmc("全局");
            dto.setData(lvs);
            avs.add(dto);
        } catch (InterruptedException | ExecutionException e) {
            log.error("执行线程错误：{}", e.getMessage());
        }
        return avs;
    }

    /*public static void main(String[] args) {

        VPSYMDVO vo = new VPSYMDVO();
        vo.setTjsj("4-20");
        vo.setVehilePassTotal(50);

        VPSYMDVO v1 = new VPSYMDVO();
        vo.setTjsj("4-20");
        vo.setPersonPassTotal(99);

        List<VPSYMDVO> vs = new ArrayList<>(1);
        vs.add(vo);
        List<VPSYMDVO> vs1 = new ArrayList<>(1);
        vs1.add(v1);

//      List<VPSYMDVO> v2= new ArrayList<>(CollUtil.addAll(vs, vs1));
//        System.out.println(v2);
        System.out.println(vs.addAll(vs1));
        System.out.println(vs =vs.stream().filter(ObjectUtil::isNotNull).collect(Collectors.toList()));
        System.out.println(vs.size());

    }*/
}
