package cn.citms.icw.dto;

import cn.citms.icw.entity.EsAlarm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *      检测预警数据DTO
 * </pre>
 * @author liuyuyang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EsAlarmDTO extends EsAlarm {
    private static final long serialVersionUID = 1L;
}
