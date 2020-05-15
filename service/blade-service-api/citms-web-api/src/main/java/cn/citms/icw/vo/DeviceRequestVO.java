package cn.citms.icw.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 社区设备请求过滤VO
 *
 * @author Blade
 * @since 2020-04-22
 */
@Data
@ApiModel(value = "DeviceRequestVO对象", description = "社区设备请求过滤")
public class DeviceRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 设备名称/IP/设备编号 过滤
     * Like, FieldName = "DeviceName,IP,DeviceNo"
     */
    @ApiModelProperty(value = "设备名称/IP/设备编号 过滤")
    private String deviceInfo;
    /**
     * 社区Id
     */
    @ApiModelProperty(value = "社区Id")
    private String communityId;

    private List<String> communityIds;

    @ApiModelProperty(value = "单元Id")
    private String unitId;
    /**
     * 功能类型，例如卡口相机，电子警察相机，信号机，诱导屏，工控机
     */
    @ApiModelProperty(value = "功能类型")
    private String functionType;

    @ApiModelProperty(value = "设备状态")
    private String ywStatus;
    /**
     * 区域
     */
    @ApiModelProperty(value = "区域")
    private String areaId;
    /**
     * 运维单位/建设单位/监理单位/运维单位 过滤
     * Like, FieldName = "jsDepartment,jlDepartment,cjDepartment,ywDepartment"
     */
    @ApiModelProperty(value = "运维单位/建设单位/监理单位/运维单位")
    private String departmentInfo;

    private String sortField;
    private String sortOrder;

    /**
     * 功能类型 in
     */
    private List<String> functionTypes;

    private Integer pageSize;
    private Integer pageIndex;

    private String tagInfo;
}
