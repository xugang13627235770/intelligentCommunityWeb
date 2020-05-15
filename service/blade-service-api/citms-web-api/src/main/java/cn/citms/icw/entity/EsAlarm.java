package cn.citms.icw.entity;

import com.frameworkset.orm.annotation.ESId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.frameworkset.elasticsearch.entity.ESBaseData;

import java.io.Serializable;

/**
 * <pre>
 *     检测预警数据
 * </pre>
 * @author liuyuyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EsAlarm extends ESBaseData{

    private static final long serialVersionUID = 1L;
    /**
     *   自动生成唯一UUID
     */
    @ESId(persistent = false, readSet = true)
    public String uuid;

    /**
     *   url字段列表
     */
    private String ___urls;

    /**
     *   报警地点
     */
    private String location;

    /**
     *   报警类型
     */
    private String alarmType;

    /**
     *   照片1
     */
    private String pictureUrl1;

    /**
     *   照片2
     */
    private String pictureUrl2;

    /**
     *   照片3
     */
    private String pictureUrl3;


    /**
     *   数据来源
     */
    private String source;

    /**
     *   外部字段
     */
    private String ext;

    /**
     *   设备id
     */
    private String deviceId;

    /**
     *   设备ip
     */
    private String deviceNo;

    /**
     *   设备名称
     */
    private String deviceName;

    /**
     *   经度
     */
    private double longitude;

    /**
     *   纬度
     */
    private double latitude;

    /**
     *   数据采集时间
     */
    private String captureTime;
}
