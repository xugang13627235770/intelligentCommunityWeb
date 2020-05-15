package cn.citms.icw.vo;

import cn.citms.icw.entity.EsVehicle;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *     车辆VO
 * </pre>
 *
 * @author liuyuyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class EsVehicleVO extends EsVehicle {
    private static final long serialVersionUID = 1L;

    /**
     * 社区名称
     */
    public String sqmc;


    /**
     * 设备名称
     */
    public String deviceName;

    /**
     * 号牌颜色中文
     */
    public String plateColorCn;

    /**
     * 车辆来源 登记车辆/外来车辆
     */
    public String vehicleSource;

    /**
     * 车身颜色中文
     */
    public String vehicleColorCn;

    /**
     * 车辆类型中文
     */
    public String vehicleTypeCn;

    /**
     * 车辆宽度
     */
    public Double vehicleWidth;

    /**
     * 号牌种类中文
     */
    public String plateTypeCn;

    /**
     * 通行方向
     */
    public String passDirection;

    /**
     * 通行结果
     */
    public String passResult;

    /**
     * 经度
     */
    public Double longitude;

    /**
     * 纬度
     */
    public Double latitude;
}
