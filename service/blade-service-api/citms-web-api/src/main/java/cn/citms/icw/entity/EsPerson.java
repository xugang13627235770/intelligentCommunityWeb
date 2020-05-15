package cn.citms.icw.entity;

import com.frameworkset.orm.annotation.ESId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.frameworkset.elasticsearch.entity.ESBaseData;



/**
 * <pre>
 *       行人通行数据
 *  </pre>
 *
 * @author liuyuyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EsPerson extends ESBaseData {
    private static final long serialVersionUID = 1L;
    /**
     * 自动生成唯一UUID
     */
    @ESId(persistent = false, readSet = true)
    public String uuid;

    /**
     * 数据来源
     */
    public String source;

    /**
     * 身份证号
     */
    public String idCardNo;

    /**
     * 姓名
     */
    public String name;

    /**
     * 性别
     */
    public int gender;

    /**
     * 年龄
     */
    public int age;

    /**
     * 民族
     */
    public int nation;

    /**
     * 相似度
     */
    public double similarity;

    /**
     * 出生日期
     */
    public String birthday;

    /**
     * 身份证有效期起始日期
     */
    public String termStartDate;

    /**
     * 身份证有效期结束日期
     */
    public String termEndDate;

    /**
     * 发证机关
     */
    public String issuedBy;

    /**
     * 登记地址
     */
    public String address;

    /**
     * 是否验证通过
     */
    public boolean isValidate;

    /**
     * 检查结果(0:正常 1：人证不一 2:无证 3：重点人员)
     */
    public int checkResult;

    /**
     * 抓拍照片1
     */
    public String pictureUrl1;

    /**
     * 抓拍照片2
     */
    public String pictureUrl2;

    /**
     * 身份证照片1
     */
    public String imageUrl1;

    /**
     * 身份证照片2
     */
    public String imageUrl2;

    /**
     * 身份证照片3
     */
    public String imageUrl3;

    /**
     * url字段列表
     */
    public String _urls;

    /**
     * 外部字段
     */
    public String ext;

    /**
     * 设备id
     */
    public String deviceId;

    /**
     * 设备ip
     */
    public String deviceIp;

    /**
     * 设备编号
     */
    public String deviceNo;

    /**
     * 设备名称
     */
    public String deviceName;

    /**
     * 经度
     */
    public double longitude;

    /**
     * 纬度
     */
    public double latitude;

    /**
     * 数据采集时间
     */
    public String captureTime;
}
