package cn.citms.icw.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     行人搜索条件
 * </pre>
 * @author liuyuyang
 */
@Data
public class PersonSearchVO implements Serializable {
    private static final long serialVersionUID = -1742170070547352772L;
    /**
    * 社区Id
    */
    public String communityId ;

    /**
    * 身份证号
    */
    public String idCardNo ;

    /**
    * 姓名
    */
    public String name ;

    /**
    * 社区编码
    */
    public List<String> exts ;

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
    * 行人身份
    */
    public String isValidate ;

    /**
    * 每页大小
    */
    public Integer pageSize ;

    /**
    * 第几页
    */
    public Integer pageIndex ;
}
