package cn.citms.icw.service;

import cn.citms.icw.vo.EsPersonVO;
import cn.citms.icw.vo.EsVehicleVO;
import cn.citms.icw.vo.PersonSearchVO;
import cn.citms.icw.vo.VehicleSearchVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
/**
 * <pre>
 *     车辆记录 服务类
 * </pre>
 * @author liuyuyang
 */
public interface IVehiclePassService {

    /**
     * 查询车辆
     * @param vo {@link VehicleSearchVO}  参数
     * @return IPage {@link EsVehicleVO} 返回值
     */
    IPage<EsVehicleVO> queryVehicle(VehicleSearchVO vo);
}
