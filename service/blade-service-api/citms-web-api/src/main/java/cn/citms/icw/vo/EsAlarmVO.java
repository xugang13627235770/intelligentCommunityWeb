package cn.citms.icw.vo;

import cn.citms.icw.entity.EsAlarm;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <pre>
 *      检测预警数据VO
 * </pre>
 *
 * @author liuyuyang
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class EsAlarmVO extends EsAlarm {
    private static final long serialVersionUID = 1L;
    /**
     * 社区名称
     */
    public String sqmc;

    /**
     * 社区ID
     */
    public String communityId;

    /**
     * 楼栋单元
     */
    public String buildingUnit;

    /**
     * 检测类型
     */
    public String alarmTypeName;

    /**
     * 报警设备
     */
    public String deviceName;

    /**
     * 采集时间
     */
    public String captureTimeFormart;

    /**
     * 经纬度
     */
    public String jwd;
}
