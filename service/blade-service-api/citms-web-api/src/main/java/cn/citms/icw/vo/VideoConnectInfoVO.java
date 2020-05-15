package cn.citms.icw.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 视频连接信息
 *
 * @author cyh
 * @since 2020-04-26
 */
@Data
@ApiModel(value = "VideoConnectInfoVO对象", description = "视频连接信息")
public class VideoConnectInfoVO {

    /**
     * 连接方式
     */
    private String provider;
    /**
     * 连接方式名称
     */
    private String providerName;
    /**
     * 连接字符串
     */
    private String connectionInfo;
    /**
     * 是否手动填写连接字符串
     */
    private Integer isRawURL;
}
