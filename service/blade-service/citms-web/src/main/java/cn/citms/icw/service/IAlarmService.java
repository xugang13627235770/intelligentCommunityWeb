package cn.citms.icw.service;

import cn.citms.icw.vo.EsAlarmSearchVO;
import cn.citms.icw.vo.EsAlarmVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.tool.api.R;


/**
 * <pre>
 *      报警数据 服务类
 * </pre>
 *
 * @author liuyuyang
 */
public interface IAlarmService {

    /**
     * 查询说有报警数据
     *
     * @param vo {@link EsAlarmSearchVO} 查询参数
     * @return IPage {@link EsAlarmVO} 返回参数
     */
    IPage<EsAlarmVO> queryAlarm(EsAlarmSearchVO vo);

    /**
     * 根据id查询报警数据
     *
     * @param id {@link String} 查询id
     * @return R {@link EsAlarmVO } 返回参数
     */
    R<EsAlarmVO> findById(String id);



}
