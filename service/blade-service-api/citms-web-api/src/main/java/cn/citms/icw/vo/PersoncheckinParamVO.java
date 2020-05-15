package cn.citms.icw.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@ApiModel(value = "PersoncheckinParamVO", description = "住户信息")
public class PersoncheckinParamVO {

    private String id;
    private String xm;
    private String xbdm;
    private String rylx;
    private String community_Id;
    private String building_Id;
    private String unit_Id;
    private String house_Id;
    private String zhInfo;
    private String mhmc;
    private Integer pageSize;
    private Integer pageIndex;
    private Boolean isPagination;
    private String sortField;
    private String sortOrder;
    private Boolean searchCount;
    @ApiModelProperty(hidden = true)
    private List<String> communityIds;

}
