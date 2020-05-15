package cn.citms.icw.Utils;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author cyh
 */
public class ExcelUtils {

    private ExcelUtils(){}

    public enum ExcelEnum{
        /**住户导入模板文件路径**/
        PERSON_XLS("person", "excelTemplate/住户批量注册.xls", "住户批量注册"),
        /**单元导入模板文件路径**/
        UNIT_XLS("unit", "excelTemplate/单元批量注册.xls", "单元批量注册"),
        /**巡更导入模板文件路径**/
        PATROL_XLS("patrolpoint","excelTemplate/巡更点批量注册.xls", "巡更点批量注册"),
        /**建筑导入模板文件路径**/
        BUILDING_XLS("building","excelTemplate/楼栋批量注册.xls", "楼栋批量注册"),
        /**社区导入模板文件路径**/
        COMMUNITY_XLS("community","excelTemplate/社区批量注册.xls", "社区批量注册"),
        /**设备导入模板文件路径**/
        DEVICE_XLS("device","excelTemplate/设备批量注册.xls", "设备批量注册"),
        /**车辆导入模板文件路径**/
        CAR_XLS("Vehicle","excelTemplate/车辆批量导入.xls", "车辆批量导入"),
        /**门户导入模板文件路径**/
        HOUSE_XLS("house","excelTemplate/门户批量注册.xls", "门户批量注册");

        private String type;
        private String templatePath;
        private String typeName;
        ExcelEnum(String type, String templatePath, String typeName){
            this.type = type;
            this.templatePath = templatePath;
            this.typeName = typeName;
        }

        /**
         * @param type
         * @param pathOrName 1-path,2-name
         * @return
         */
        public static String getTemplatePathOrName(String type, int pathOrName){
            String name = "";
            for (ExcelEnum value : ExcelEnum.values()) {
                if(value.getType().equals(type)) {
                    if(pathOrName == 1) {
                        name = value.getTemplatePath();
                    }
                    if(pathOrName == 2) {
                        name = value.getTypeName();
                    }
                    break;
                }
            }
            return name;
        }

        public String getType() {
            return type;
        }

        public String getTemplatePath() {
            return templatePath;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    public static byte[] toByteArray(String path) throws IOException {
        ClassPathResource cpr = new ClassPathResource(path);
        try(InputStream is = cpr.getInputStream(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len = -1;
            while((len = is.read(buffer)) != -1){
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String getUploadFilePath() throws UnsupportedEncodingException {
        String path = ExcelUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, "UTF-8");
        if(path.contains("jar")) {
            path = new File(path).getParentFile().getParentFile().getParent();
        } else {
            path = new File(path).getParent();
        }
        if (!File.separator.equals("/") && path.indexOf("file:\\") >= 0) {
            path = path.substring("file:\\".length());
        }else if(File.separator.equals("/")){
            path = path.substring("file:".length());
        }
        return path;
    }

    /**
     * 下拉选项校验
     * @param e
     * @return
     */
    public static String SelectVerify(Object e, String cellName, Map<String, Object> map, boolean ifNull){
        String value = (String) e;
        if(StrUtil.isNotBlank(value)) {
            if(!map.containsKey(value)) {
                return cellName + "下拉选项" + value + "不存在";
            }
        } else {
            if(!ifNull) {
                return cellName+"不能为空";
            }
        }
        return "";
    }

    /**
     * 唯一性校验
     * @param e 校验参数
     * @return 错误信息
     */
    public static String XqmcVerify(Object e, String extra, Map<String, Object> map, String cellName) {
        String value = (String) e;
        if(StrUtil.isBlank(value)){
            return cellName + "不能为空";
        }
        if(value.length() > 100) {
            return cellName + "最大长度为100";
        }
        Object obj = map.get(extra + value);
        Integer total = obj == null ? 1 : (Integer) obj + 1;
        map.put(extra + value, total);
        if(total > 1) {
            return cellName + ":“"+value+"”已经存在";
        }
        return "";
    }

    private static Pattern pattern3 = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$");
    private static Pattern pattern4 = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
    public static String DateVerify(Object e, String cellName) {
        String value = (String) e;
        if(StrUtil.isNotBlank(value)) {
            if(!pattern3.matcher(value).matches() && !pattern4.matcher(value).matches()) {
                return cellName + "格式不正确";
            }
        }
        return "";
    }

    /**
     * 数字校验
     * @param e
     * @return
     */
    private static Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
    public static String DoubleVerify(Object e, String cellName) {
        String value = (String) e;
        String result = "";
        if(StrUtil.isNotBlank(value) && value.length() > 100) {
            result = cellName + "最大长度为100";
        }
        if(StrUtil.isNotBlank(value) && !pattern.matcher(value).matches()) {
            result += cellName + "格式不正确";
        }
        return result;
    }

    public static String lenVerify(Object e, String cellName, int maxLen){
        String value = (String) e;
        if(StrUtil.isNotBlank(value) && value.length() > maxLen) {
            return cellName + "最大长度为"+maxLen;
        }
        return "";
    }
    public static String notNullVerify(Object e, String cellName, int maxLen){
        String value = (String) e;
        if(StrUtil.isBlank(value)) {
            return cellName + "不能为空";
        }
        if(value.length() > maxLen) {
            return cellName + "最大长度为" + maxLen;
        }
        return "";
    }

    /**
     * 数字校验
     * @param e
     * @param cellName
     * @return
     */
    private static Pattern pattern2 = Pattern.compile("^([1-9]\\d*|[0]{1,1})$");
    public static String NumberVerify(Object e, String cellName){
        String result = "";
        String value = (String) e;
        if(StrUtil.isNotBlank(value) && value.length() > 100) {
            result = cellName + "最大长度为100";
        }
        if(StrUtil.isNotBlank(value) && !pattern2.matcher(value).matches()) {
            result += cellName + "格式不正确";
        }
        return result;
    }

    private static Pattern PATTERN_CARD = Pattern.compile("^[1-9]{1}[0-9]{14}$|^[1-9]{1}[0-9]{16}([0-9]|[xX])$");
    private static Pattern PATTERN_CARD2 = Pattern.compile("^(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
    /**
     * 身份证校验
     * @param e
     * @param cellName
     * @return
     */
    public static String CardNoVerify(Object e, String cellName){
        String result = "";
        String value = (String) e;
        if(StrUtil.isNotBlank(value) && value.length() > 100) {
            result = cellName + "最大长度为100";
        }
        if(StrUtil.isNotBlank(value) && !PATTERN_CARD.matcher(value).matches()) {
            result += cellName + "格式不正确";
        }
        return result;
    }

    //手机号码
    private static Pattern PATTERN_Tel = Pattern.compile("^1\\d{10}$");
    //座机格式
    private static Pattern PATTERN_Tel2 = Pattern.compile("^\\d{3,4}(-?)\\d{7,8}$");
    /**
     * 电话校验
     * @param e
     * @param cellName
     * @return
     */
    public static String telVerify(Object e, String cellName){
        String result = "";
        String value = (String) e;
        if(StrUtil.isNotBlank(value) && value.length() > 100) {
            result = cellName + "最大长度为100";
        }
        if(StrUtil.isNotBlank(value)) {
            if(!PATTERN_Tel.matcher(value).matches() && !PATTERN_Tel2.matcher(value).matches()) {
                return cellName + "格式不正确";
            }
        }
        return result;
    }

    /**
     * 号牌号码校验
     * @param e
     * @return
     */
    private static String PlateNoVerify(Object e, String cellName) {
        String value = (String) e;
        if (StrUtil.isBlank(value)) {
            return cellName+"不能为空";
        }
        if (value.length() < 7) {
            return cellName+"格式错误";
        }
        return "";
    }

    /**
     * 楼栋单元校验
     * @return
     */
    public static String lddymcVerify(Object e){
        String result = "";
        String value = (String) e;
        if(StrUtil.isNotBlank(value)) {
            String[] val = value.split("-");
            if(val.length != 3) {
                result += "楼栋单元名称格式不正确,请使用正确的格式，例如“B7-1单元-101”,请注意横线";
            } else {
                if(StrUtil.isBlank(val[0])) {
                    result += "楼栋单元名称--“楼栋名称”不能为空";
                }
                if(StrUtil.isBlank(val[1])) {
                    result += "楼栋单元名称--“单元名称”不能为空";
                }
                if(StrUtil.isBlank(val[2])) {
                    result += "楼栋单元名称--“门户名称”不能为空";
                }
            }
        } else {
            result = "楼栋单元门户名称必须填写";
        }
        return result;
    }
    public static String lddymcVerify2(Object e){
        String result = "";
        String value = (String) e;
        if(StrUtil.isNotBlank(value)) {
            String[] val = value.split("-");
            if(val.length != 2) {
                result += "楼栋单元名称格式不正确,请使用正确的格式，例如“B7-1单元”,请注意横线";
            } else {
                if(StrUtil.isBlank(val[0])) {
                    result += "楼栋单元名称--“楼栋名称”不能为空";
                }
                if(StrUtil.isBlank(val[1])) {
                    result += "楼栋单元名称--“单元名称”不能为空";
                }
            }
        }
        return result;
    }

    /**
     * 将校验的错误信息写入excel 返回地址
     * @throws IOException
     */
    public static String writeErrorMsg(MultipartFile fileDoc, List<String> errorMsg, String serverAddress,
                                       int rowIndex, int cellNum) throws IOException {
        HSSFWorkbook hwb = new HSSFWorkbook(fileDoc.getInputStream());
        HSSFSheet sheet = hwb.getSheetAt(0);
        HSSFRow row = null;
        for (int i = rowIndex; i <= (errorMsg.size()+rowIndex); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            HSSFCell errorCell = row.getCell(cellNum);
            if (errorCell == null)  {
                errorCell = row.createCell(cellNum);
            }

            HSSFCellStyle copyStyle = row.getCell(0).getCellStyle();
            HSSFFont copyFont = copyStyle.getFont(hwb);

            HSSFCellStyle cellStyle = hwb.createCellStyle();
            HSSFFont font = hwb.createFont();
            font.setBold(true);
            font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
            font.setFontHeight(copyFont.getFontHeight());
            font.setFontName(copyFont.getFontName());
            cellStyle.setFont(font);
            cellStyle.setBorderBottom(copyStyle.getBorderBottomEnum());
            cellStyle.setBorderLeft(copyStyle.getBorderLeftEnum());
            cellStyle.setBorderRight(copyStyle.getBorderRightEnum());
            cellStyle.setBorderTop(copyStyle.getBorderTopEnum());
            cellStyle.setFillBackgroundColor(copyStyle.getFillBackgroundColor());
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            if(i == rowIndex){
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
            }

            errorCell.setCellStyle(cellStyle);
            if(i == rowIndex) {
                errorCell.setCellValue("错误信息");
            } else {
                errorCell.setCellValue(errorMsg.get(i-rowIndex-1));
            }
        }

        String userPath = System.getProperties().getProperty("user.home");
        File mediaPath = new File(userPath + "/TempFiles/ErrorExcel");
        if (!mediaPath.exists()) {
            mediaPath.mkdirs();
        }
        String nameExtra = StrUtil.format("{}{}{}{}{}", DateUtil.thisMonth(), DateUtil.thisDayOfMonth(),
                DateUtil.thisHour(true), DateUtil.thisMinute(), DateUtil.thisSecond());
        FileOutputStream out = new FileOutputStream(mediaPath + "/" +nameExtra+ fileDoc.getOriginalFilename());
        hwb.write(out);
        out.flush();
        out.close();

        return serverAddress + "/TempFiles/ErrorExcel/" +nameExtra+ fileDoc.getOriginalFilename();
    }
}
