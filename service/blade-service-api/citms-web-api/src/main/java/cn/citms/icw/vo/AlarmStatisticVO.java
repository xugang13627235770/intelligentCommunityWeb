package cn.citms.icw.vo;

import cn.citms.icw.dto.StatisticTypeCountDTO;
import cn.citms.icw.dto.StatisticTypeTotalDTO;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     检测预警统计查询信息显示对象
 * </pre>
 * @author liuyuyang
 */
@Data
@Builder
public class AlarmStatisticVO implements Serializable {

    private static final long serialVersionUID = -3354356302956568375L;

    /**
   * 社区名称
    */
    public String sqmc ;

    /**
   * 预警总数
    */
    public double alarmTotal ;

    /**
   * 报警类型构成
    */
    public List<StatisticTypeCountDTO> alarmTypeTj ;


    /**
   * 检测预警统计查询-年月日
    */
    public List<StatisticTypeTotalDTO> data ;
}
