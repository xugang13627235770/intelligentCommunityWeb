package cn.citms.icw.dto;

import cn.citms.icw.entity.EsVehicleTrafficflow;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *      车辆通行数据DTO
 * </pre>
 * @author liuyuyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EsVehicleTrafficflowDTO extends EsVehicleTrafficflow {
    private static final long serialVersionUID = 1L;
}
