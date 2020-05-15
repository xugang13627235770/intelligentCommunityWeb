package cn.citms.icw.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     统计查询对象
 * </pre>
 *
 * @author liuyuyang
 */
@Data
public class StatisticsDefSearchVO implements Serializable {
    private static final long serialVersionUID = -7007364548103928669L;

    /**
     * 社区ID
     */
    public String community_Id;

    /**
     * 社区ID
     */
    public List<String> communityIds;

    /**
     * 楼栋ID
     */
    public String building_Id;

    /**
     * 单元ID
     */
    public String unit_Id;

    /**
     * 门户ID
     */
    public String house_Id;

    /**
     * 查询类型（年：1；月：2；日：3）
     */
    public String dateType;


    /**
     * 社区ID
     */
    public List<String> exts;

}
