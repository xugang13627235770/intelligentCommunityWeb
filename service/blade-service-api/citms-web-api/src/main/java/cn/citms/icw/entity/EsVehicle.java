package cn.citms.icw.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.ESId;
import com.frameworkset.orm.annotation.ESMetaType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.frameworkset.elasticsearch.entity.ESBaseData;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 *       es 车辆数据
 *  </pre>
 *
 * @author liuyuyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EsVehicle extends ESBaseData {
    private static final long serialVersionUID = 1L;
    /**
     * 自动生成唯一UUID
     */
    @ESId(persistent = false, readSet = true)
    public String vehicleID;

    /**
     * 通道序列号/卡口编号/文件序列号
     */
    public String checkpoint_id;

    /**
     * 数据来源类型
     */
    public String data_src_type;

    /**
     * 整图的宽
     */
    public Double whole_width;

    /**
     * 整图的高
     */
    public Double whole_heigth;

    /**
     * 绝对开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    public Date abs_stime;

    /**
     * 绝对结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    public Date abs_etime;

    /**
     * 相对开始时间
     */
    public int relative_stime;

    /**
     * 相对开始时间
     */
    public int relative_etime;

    /**
     * 车牌号
     */
    public String plate_no;

    /**
     * 车牌颜色
     */
    public String plate_color;

    /**
     * 车牌类型
     */
    public String plate_type;

    /**
     * 车牌可信度
     */
    public int plate_confidence;

    /**
     * 运行方向
     */
    public String direction;

    /**
     * 亮度评价
     */
    public int vehicle_brightness;

    /**
     * 车身颜色
     */
    public String vehicle_color;

    /**
     * 车辆类型
     */
    public String vehicle_type;

    /**
     * 车辆品牌（车标）
     */
    public String vehicle_brand;

    /**
     * 车辆子品牌
     */
    public String vehicle_sub_brand;

    /**
     * 车辆年款id
     */
    public String vehicle_model_year;

    /**
     * 车辆款式名称
     */
    public String vehicle_model_name;

    /**
     * 车身区域
     */
    public List<Integer> vehicle_rect;

    /**
     * 车牌区域
     */
    public Integer[] plate_rect;


    /**
     * 车标区域
     */
    public String logo_rect;

    /**
     * 驾驶员区域
     */
    public String pilot_rect;

    /**
     * 副驾驶员区域
     */
    public String vice_pilot_rect;

    /**
     * 主驾是否系安全带
     */
    public String pilot_safebelt;

    /**
     * 副驾是否系安全带
     */
    public String vice_pilot_safebelt;

    /**
     * 驾驶员是否打手机
     */
    public String use_phone;

    /**
     * 标示物JSON字符串-挂件
     */
    public String pendant;

    /**
     * 标示物JSON字符串-纸巾盒
     */
    public String tissue_box;

    /**
     * 标示物JSON字符串-驾驶位遮阳板
     */
    public String pilot_sunvisor;

    /**
     * 标示物JSON字符串-副驾驶位遮阳板
     */
    public String vice_pilot_sunvisor;

    /**
     * 标示物JSON字符串-年检标
     */
    public String annual_tags;

    /**
     * 年检标粘贴形状
     */
    public String annual_tags_layout;

    /**
     * 标示物JSON字符串-车窗
     */
    public String marker_windows;

    /**
     * 有无挂件
     */
    public int i_pendant;

    /**
     * 有无纸巾盒
     */
    public int i_tissuebox;

    /**
     * 主驾驶遮阳板位置
     */
    public int i_pilot_sunvisor;

    /**
     * 副驾驶遮阳板位置
     */
    public int i_vice_sunvisor;

    /**
     * 车标
     */
    public int i_annual_tags;

    /**
     * 车窗
     */
    public int i_marker_windows;

    /**
     * 车道编号
     */
    public String lane_no;

    /**
     * 车道名称
     */
    public String lane_name;

    /**
     * 过车速度
     */
    public String vehicle_speed;

    /**
     * GIS坐标 x
     */
    public String location_x;

    /**
     * GIS坐标 y
     */
    public String location_y;

    /**
     * GIS坐标 z
     */
    public String location_z;

    /**
     * 记录类型，0正常过车；＞0对应布控表中的id
     */
    public String surveillance_id;

    /**
     * GIS区域哈希码
     */
    public String location_hashcode;

    /**
     * 采集地址
     */
    public String capture_address;

    /**
     * 采集人姓名
     */
    public String capture_user;

    /**
     * 过车时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(dataformat = "yyyy-MM-dd HH:mm:ss")
    public Date pass_timestamp;

    /**
     * 过车日期
     */
    public int pass_date;

    /**
     * 过车时间（性能测试比较）
     */
    public int pass_time;

    /**
     * 插入数据库前时间
     */
    public String save_time;

    /**
     * 主机编号（全局唯一）
     */
    public String host_id;

    /**
     * 状态
     */
    public String status;

    /**
     * 图片路径url1
     */
    public String image_url1;

    /**
     * 图片路径url2
     */
    public String image_url2;

    /**
     * 设备编号
     */
    public String deviceNo;

    /**
     * 社区编号
     */
    public String ext;
}
