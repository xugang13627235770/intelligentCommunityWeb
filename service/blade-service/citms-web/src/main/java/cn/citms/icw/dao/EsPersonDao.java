package cn.citms.icw.dao;

import cn.citms.icw.dto.StatisticTypeCountDTO;
import cn.citms.icw.dto.StatisticTypeTotalDTO;
import cn.citms.icw.entity.EsAlarm;
import cn.citms.icw.entity.EsPerson;
import cn.citms.icw.vo.AlarmStatisticVO;
import cn.citms.icw.vo.EsPersonVO;
import cn.citms.mbd.basicdatacache.service.IDictionaryCacheService;
import lombok.extern.slf4j.Slf4j;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.MapRestResponse;
import org.springblade.common.constant.CitmsAppConstant;
import org.springblade.common.enums.DictEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
 * @author liuyuyang
 */
@Slf4j
@Component("esPersonDao")
public class EsPersonDao {

    /**
     * 根据参数查询人员通过数据
     *
     * @param params {@link Map} 参数
     * @return EsAlarm {@link EsAlarm} 返回值
     */
    public ESDatas<EsPerson> queryPerson(Map<String, Object> params) {
        ClientInterface clientUtil = ElasticSearchHelper.getConfigRestClientUtil(CitmsAppConstant.ES_SEARCH_PERSON_PATH);
        return clientUtil.searchList(CitmsAppConstant.PERSON_CLUSTER_ES_INDEX_SEARCH,
                CitmsAppConstant.SEARCH_PERSON,
                params,
                EsPerson.class);
    }



}
