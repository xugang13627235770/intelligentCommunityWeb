package cn.citms.icw.vo;

import cn.citms.icw.entity.EsPerson;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <pre>
 *     行人VO
 * </pre>
 * @author liuyuyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class EsPersonVO extends EsPerson {
    private static final long serialVersionUID = -2177062451048340930L;
    /**
    * 社区名称
    */
    public String sqmc ;

    /**
    * 设备名称
    */
    public String deviceName ;

    /**
    * 性别
    */
    public String sex ;

    /**
    * 身份
    */
    public String stationName ;

    /**
    * 设备信息
    */
    public List<DeviceVO> listDevice ;
}
