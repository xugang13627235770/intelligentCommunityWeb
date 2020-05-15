package cn.citms.icw.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 统计（一个类型，类型对应数量,以及该类型的总数量）
 *
 * @author liuyuyang
 */
@Data
@Builder
public class StatisticTypeTotalDTO implements Serializable {
    private static final long serialVersionUID = -9109700359318139406L;


    /**
     * 类型
     */
    public String type;

    /**
     * 数量
     */
    public double count;

    /**
     * 总数量
     */
    public double total;
}
