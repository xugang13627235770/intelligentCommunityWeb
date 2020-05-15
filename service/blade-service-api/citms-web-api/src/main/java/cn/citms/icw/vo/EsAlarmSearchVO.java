package cn.citms.icw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *     检测预警记录
 * </pre>
 *
 * @author liuyuyang
 */
@Data
public class EsAlarmSearchVO implements Serializable {

    private static final long serialVersionUID = 5049497758441269396L;
    /**
     * 社区Id
     */
    public String communityId;

    /**
     * 外部字段
     */
    public String[] exts;
    /**
     * 社区code
     */
    public String ext;
    /**
     * 预警类型
     */
    public String alarmType;

    /**
     * 设备名称
     */
    public String deviceNo;

    /**
     * 检测开始时间
     */
    @ApiModelProperty(value = "检测开始时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern= "yyyy-MM-dd HH:mm:ss")
    public Date startTime;

    /**
     * 到达时间结束时间
     */
    @ApiModelProperty(value = "到达时间结束时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern= "yyyy-MM-dd HH:mm:ss")
    public Date endTime;

    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小")
    public Integer pagesize;

    /**
     * 第几页
     */
    public Integer pageindex;

    /**
     * 条件
     */
    public String[] condition;
}
