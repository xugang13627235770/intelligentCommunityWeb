package cn.citms.icw.controller;

import cn.citms.icw.Utils.ExcelUtils;
import cn.citms.icw.service.ExcelService;
import cn.citms.icw.vo.ExcelImportVO;
import cn.citms.icw.factory.ExcelImportFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 通知公告表 控制器
 *
 * @author Blade
 * @since 2020-04-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("Excel")
@Api(value = "Excel", tags = "Excel")
public class ExcelController extends BladeController {

    /**
     * 导出数据模板
     * @param response-response
     * @param type 业务类型
     */
    @GetMapping("/DownLoadTemplate")
    @ApiOperation(value = "导出数据模板", httpMethod = "GET", notes = "导出数据模板")
    public void exportTemplate(HttpServletResponse response, @RequestParam("type") String type) {
        try {
            String path = ExcelUtils.ExcelEnum.getTemplatePathOrName(type, 1);
            String name = ExcelUtils.ExcelEnum.getTemplatePathOrName(type, 2);
            byte[] data = ExcelUtils.toByteArray(path);
            response.reset();
            String fileName = URLEncoder.encode(name, "UTF-8");
            response.addHeader("Content-Length", "" + data.length);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName+".xls");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量导入数据
     * @param file-模板文件
     * @param type-业务类型
     * @return R
     */
    @PostMapping("/ImportTemplate")
    @ApiOperation(value = "批量导入模板数据",httpMethod = "POST",notes = "批量导入模板数据")
    public ExcelImportVO importStations(MultipartFile file, @RequestParam String type, HttpServletRequest request){
        try {
            ExcelService service = ExcelImportFactory.getTypeService(type);
            if(service == null) {
                return ExcelImportVO.fail("没有对应的导入模板");
            }
            String serverAddress = "http://"+ request.getServerName() + ":" + request.getServerPort();
            return service.importTemplate(file, serverAddress);
        } catch (Exception e) {
            e.printStackTrace();
            return ExcelImportVO.fail();
        }
    }

}
