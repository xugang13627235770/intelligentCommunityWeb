package cn.citms.icw.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("通用的返回模型")
public class CommonCountDTO {

    private String keyName;

    private Integer count;

}
