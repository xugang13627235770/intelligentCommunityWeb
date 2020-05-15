package cn.citms.icw.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "设备检测模型")
public class DeaviceCheckLogVO {

    private String id;
    private String status;
    private String deviceId;
    private String deviceIp;
    private String port;
    private String deviceNo;
    private Date checkTime;

}
