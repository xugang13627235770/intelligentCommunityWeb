package cn.citms.icw.dto;

import cn.citms.icw.entity.EsPerson;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liuyuyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EsPersonDTO extends EsPerson {
    private static final long serialVersionUID = -2885873051651473461L;
}
