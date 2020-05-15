package cn.citms.icw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     es巡更记录条件
 * </pre>
 *
 * @author
 */
@Data
public class EsPatrollogSearchVO implements Serializable {

    private static final long serialVersionUID = 5049497758441269396L;

    @ApiModelProperty(value = "社区id")
    private String communityId;

    @ApiModelProperty(value = "社区code", hidden = true)
    public String ext;

    @ApiModelProperty(value = "社区编号集合")
    private List<String> exts;

    @ApiModelProperty(value = "巡更点编号")
    public  List<String>       pointNumbers;

    @ApiModelProperty(value = "巡更人姓名")
    public String staffName;

    @ApiModelProperty(value = "检测开始时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern= "yyyy-MM-dd HH:mm:ss")
    public Date startTime;

    @ApiModelProperty(value = "到达时间结束时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern= "yyyy-MM-dd HH:mm:ss")
    public Date endTime;

    @ApiModelProperty(value = "每页大小")
    public Integer pagesize;

    @ApiModelProperty(value = "第几页")
    public Integer pageindex;

}
