package cn.citms.icw.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     车辆搜索条件
 * </pre>
 * @author liuyuyang
 */
@Data
public class VehicleSearchVO {

    /**
    * 社区Id
    */
    public String communityId ;

    /**
    * 社区编号
    */
    public List<String> xqbh ;

    /**
    * 号牌号码
    */
    public String plateNo ;

    /**
    * 号牌颜色
    */
    public String plateColor ;

    /**
    * 开始时间
    */
    public Date startTime ;

    /**
    * 截止时间
    */
    public Date endTime ;

    /**
    * 设备编号
    */
    public String deviceNo ;

    /**
    * 车辆身份
    */
    public String vehicleSource ;

    /**
    * 每页大小
    */
    public Integer pageSize ;

    /**
    * 第几页
    */
    public Integer pageIndex ;
}
