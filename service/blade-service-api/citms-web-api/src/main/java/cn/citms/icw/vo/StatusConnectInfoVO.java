package cn.citms.icw.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 状态连接信息
 *
 * @author cyh
 * @since 2020-04-26
 */
@Data
@ApiModel(value = "StatusConnectInfoVO对象", description = "状态连接信息")
public class StatusConnectInfoVO {

    /**
     * 状态监测方式
     */
    private String statusCheck;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 设备巡检连接字符串
     */
    private String pollConnectInfo;
}
