package cn.citms.icw.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "excel导入结果对象", description = "excel导入结果对象")
public class ExcelImportVO {

    @JsonProperty("IsSuccess")
    private Boolean IsSuccess;
    @JsonProperty("Message")
    private String Message;

    ExcelImportVO(Boolean isSuccess, String msg){
        this.IsSuccess = isSuccess;
        this.Message = msg;
    }

    public static ExcelImportVO fail(){
        return new ExcelImportVO(false, "导入失败");
    }
    public static ExcelImportVO fail(String msg){
        return new ExcelImportVO(false, msg);
    }
    public static ExcelImportVO success(String msg){
        return new ExcelImportVO(true, msg);
    }
}
