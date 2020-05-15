package cn.citms.icw.entity;

import lombok.Data;

@Data
public class StatisticTypeTotal {

    /// <summary>
    /// 类型
    /// </summary>
    public String type;

    /// <summary>
    /// 数量
    /// </summary>
    public double count;

    /// <summary>
    /// 总数量
    /// </summary>
    public double total;

}
