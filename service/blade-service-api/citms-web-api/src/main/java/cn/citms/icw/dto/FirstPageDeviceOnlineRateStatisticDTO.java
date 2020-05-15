package cn.citms.icw.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "统计设备在线率的返回模型")
public class FirstPageDeviceOnlineRateStatisticDTO {

    /// 设备平均在线率
    public String deviceAvgRate;

    /// 设备在线率
    public String deviceRate;

    /// 设备在线
    public List<StatisticTypeCountDTO> deviceOnline;

}
