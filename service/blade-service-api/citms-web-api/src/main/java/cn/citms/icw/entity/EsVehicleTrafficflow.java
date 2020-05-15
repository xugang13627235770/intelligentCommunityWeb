package cn.citms.icw.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *     车辆通行数据
 * </pre>
 * @author liuyuyang
 */
@Data
public class EsVehicleTrafficflow implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
    * 唯一标识
    */
    public String id ;

    /**
    * 社区编码
    */
    public String ext ;

    /**
    * 统计日期
    */
    public Date statDate ;

    /**
    * 统计时间(小时)
    */
    public int statisticsTime ;

    /**
    * 通行车辆数量
    */
    public int vehiclePassCount ;

    /**
    * 通行车辆总数量
    */
    public int vehiclePassTotal ;

    /**
    * 登记车辆数量
    */
    public int djCount ;

    /**
    * 来访本地车辆数量
    */
    public int lfbdCount ;

    /**
    * 来访外地车辆数量
    */
    public int lfwdCount ;


    /**
    * 车辆（进入）数量
    */
    public int inCount ;

    /**
    * 车辆（出行）数量
    */
    public int outCount ;

    /**
     * 车辆（出行）粒度
     */
    public int granularity ;

    /**
     * 蓝牌通行数量
     */
    public int bluePlatePassCount ;

    /**
     * 黄牌通行数量
     */
    public int yellowPlatePassCount ;

    /**
     * 白牌通行数量
     */
    public int whitePlatePassCount ;

    /**
     * 蓝车牌通行数量
     */
    public int blackPlatePassCount ;

    /**
     * 绿牌通行数量
     */
    public int greenPlatePassCount ;

    /**
     * 其他颜色车牌通行数量
     */
    public int otherColorPlatePassCount ;

    /**
     * 属于军车的数量
     */
    public int belongArmyCount ;


}
