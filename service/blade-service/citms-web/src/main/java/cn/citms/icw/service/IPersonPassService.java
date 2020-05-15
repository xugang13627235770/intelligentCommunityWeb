package cn.citms.icw.service;

import cn.citms.icw.vo.EsPersonVO;
import cn.citms.icw.vo.PersonSearchVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * <pre>
 *     行人通行记录 服务类
 * </pre>
 * @author liuyuyang
 */
public interface IPersonPassService {

    /**
     * 查询通行人
     * @param vo {@link PersonSearchVO}  参数
     * @return IPage {@link EsPersonVO} 返回值
     */
    IPage<EsPersonVO> queryPerson(PersonSearchVO vo);
}
