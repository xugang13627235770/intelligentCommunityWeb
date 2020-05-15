package cn.citms.icw.service;

import cn.citms.icw.vo.ExcelImportVO;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springblade.common.utils.ExcelImportUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cyh
 */
public abstract class ExcelService {

    /**
     * 数据开始读取的行数
     * 也是表头所在行号
     */
    private int ROW_INDEX;
    /**
     * 每行单元格数
     */
    private int READ_ROW_CELL_NUM;

    public ExcelService(int rowIndex, int readRowCellNum){
        this.ROW_INDEX = rowIndex;
        this.READ_ROW_CELL_NUM = readRowCellNum;
    }

    /**
     * 批量导入 模板方法
     * @param fileDoc
     * @return
     * @throws IOException
     */
    public ExcelImportVO importTemplate(MultipartFile fileDoc, String serverAddress) throws IOException {
        List<List<Object>> list = ExcelImportUtil.readExcel(fileDoc, READ_ROW_CELL_NUM);
        if(list.size() < ROW_INDEX) {
            return ExcelImportVO.fail("导入模板没有数据");
        }
        Map<String, Map<String, Object>> map = GetExtraInfo(list);
        boolean falg = false;
        List<String> errorMsg = new ArrayList<>(list.size());
        for (int i = ROW_INDEX; i < list.size(); i++) {
            String result = verify(list.get(i), map);
            errorMsg.add(result);
            if(!falg && StrUtil.isNotBlank(result)) {
                falg = true;
            }
        }
        //校验失败
        if(falg) {
            return ExcelImportVO.fail(writeErrorMsg(fileDoc, errorMsg, serverAddress));
        } else {
            //保存
            int num = saveData(list, map);
            return ExcelImportVO.success("成功导入"+num+"条数据");
        }
    }

    /**
     * 获取额外的校验所需信息
     * @param list-数据集
     */
    protected abstract Map<String, Map<String, Object>> GetExtraInfo(List<List<Object>> list);

    /**
     * 校验数据
     * @param list
     * @param map
     * @return
     */
    protected abstract String verify(List<Object> list, Map<String, Map<String, Object>> map);

    /**
     * 写入错误信息
     */
    protected abstract String writeErrorMsg(MultipartFile fileDoc, List<String> errorMsg, String serverAddress) throws IOException;

    /**
     * 保存数据
     * @param list
     * @param map
     * @return
     */
    protected abstract int saveData(List<List<Object>> list, Map<String, Map<String, Object>> map);

}
