package cn.citms.icw.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 视频连接信息
 *
 * @author cyh
 * @since 2020-04-26
 */
@Data
@ApiModel(value = "DeviceVideoInfoVO对象", description = "视频连接信息")
public class DeviceVideoInfoVO {

    /**
     * 实时视频连接字符串
     */
    private List<VideoConnectInfoVO> real;
    /**
     * 历史视频连接字符串
     */
    private List<VideoConnectInfoVO> history;

}
