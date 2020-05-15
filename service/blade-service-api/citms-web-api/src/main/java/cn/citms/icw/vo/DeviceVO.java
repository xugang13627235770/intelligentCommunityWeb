package cn.citms.icw.vo;

import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 设备信息
 *
 * @author cyh
 * @since 2020-04-26
 */
@Data
@ApiModel(value = "DeviceVO对象", description = "设备信息")
public class DeviceVO {
    private static final long serialVersionUID = 1L;

    /**
     * 是否有视频信息
     * @param functionType 功能类型
     * @return bool
     */
    public static boolean HasVideoInfo(String functionType) {
        return StrUtil.isNotBlank(functionType) && functionType.startsWith("09");
    }

    /**
     * 设备Id
     */
    @ApiModelProperty(value = "设备Id")
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;
    /**
     * 社区Id
     */
    @NotBlank(message = "社区CommunityId不能为空")
    @ApiModelProperty(value = "社区Id")
    private String communityId;
    /**
     * 楼栋Id
     */
    @ApiModelProperty(value = "楼栋Id")
    private String buildId;
    /**
     * 单元Id
     */
    @ApiModelProperty(value = "单元Id")
    private String unitId;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String deviceNo;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号")
    private String deviceModel;
    /**
     * 所属项目
     */
    @ApiModelProperty(value = "所属项目")
    private String projectId;
    /**
     * 生产厂商Id
     */
    @ApiModelProperty(value = "生产厂商Id")
    private String manufacturerId;
    /**
     * 功能类型，例如卡口相机，电子警察相机，信号机，诱导屏，工控机
     */
    @ApiModelProperty(value = "功能类型")
    private String functionType;

    /**
     * 经度坐标值
     */
    @ApiModelProperty(value = "经度坐标值")
    private Double longitude;

    /**
     * 纬度坐标值
     */
    @ApiModelProperty(value = "纬度坐标值")
    private Double latitude;

    /**
     * 连接信息,视频设备存 视频连接信息
     */
    @ApiModelProperty(value = "连接信息")
    private String connectionInfo;

    /**
     * 所属部门Id
     */
    @ApiModelProperty(value = "所属部门Id")
    private String departmentId;

    /**
     * 扩展字段
     */
    @ApiModelProperty(value = "扩展字段")
    private String ext;

    /**
     * 扩展字段1
     */
    @ApiModelProperty(value = "扩展字段1")
    private String ext1;

    /**
     * 扩展字段2
     */
    @ApiModelProperty(value = "扩展字段2")
    private String ext2;

    /**
     * 扩展字段3
     */
    @ApiModelProperty(value = "扩展字段3")
    private String ext3;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * IP地址
     */
    @ApiModelProperty(value = "IP地址")
    private String ip;

    /**
     * 运维状态
     */
    @ApiModelProperty(value = "运维状态")
    private String ywStatus;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String modifier;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date modifiedTime;

    /**
     * 运维电话
     */
    @ApiModelProperty(value = "运维电话")
    private String ywPhone;

    /**
     * 运维单位
     */
    @ApiModelProperty(value = "运维单位")
    private String ywDepartment;

    /**
     * 监理单位
     */
    @ApiModelProperty(value = "监理单位")
    private String jlDepartment;

    /**
     * 建设单位
     */
    @ApiModelProperty(value = "建设单位")
    private String jsDepartment;

    /**
     * 承建单位
     */
    @ApiModelProperty(value = "承建单位")
    private String cjDepartment;
    /**
     * 是否在社区首页显示该设备 1:是 0:否
     */
    @ApiModelProperty(value = "是否在社区首页显示该设备 1:是 0:否")
    private Integer communityShow;
}
