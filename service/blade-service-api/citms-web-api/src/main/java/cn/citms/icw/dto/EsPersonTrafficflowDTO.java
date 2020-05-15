package cn.citms.icw.dto;

import cn.citms.icw.entity.EsPersonTrafficflow;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *      行人通行数据DTO
 * </pre>
 * @author liuyuyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EsPersonTrafficflowDTO extends EsPersonTrafficflow {
    private static final long serialVersionUID = 1L;
}
