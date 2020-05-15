package cn.citms.icw.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询es 统计数据VO
 *
 * @author liuyuyang
 */
@Data
@Builder
public class SearchEsStatisticsAlarmVO implements Serializable {
    private static final long serialVersionUID = -3908283609105189030L;
     private String startTime;
     private String endTime;
     private String minDate;
     private String maxDate;
     private String dateField;
     private String dateFormat;
}
