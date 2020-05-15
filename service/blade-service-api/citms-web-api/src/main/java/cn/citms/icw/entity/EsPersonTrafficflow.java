package cn.citms.icw.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *     行人通行数据
 * </pre>
 *
 * @author liuyuyang
 */
@Data
public class EsPersonTrafficflow implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 唯一标识
     */
    public String id;

    /**
     * 社区编码
     */
    public String ext;

    /**
     * 统计日期
     */
    public Date statDate;

    /**
     * 统计时间(小时)
     */
    public int statisticsTime;

    /**
     * 通行人数数量
     */
    public int flowPersonCount;

    /**
     * 通行人数总书数量
     */
    public int flowPersonTotal;

    /**
     * 业主人数
     */
    public int yzCount;

    /**
     * 住户人数
     */
    public int zhCount;

    /**
     * 来访人员人数
     */
    public int lfCount;

    /**
     * 行人（进入）数量
     */
    public int inCount;

    /**
     * 行人（出行）数量
     */
    public int outCount;

    /**
     * 行人（出行）粒度
     */
    public int granularity;
}
