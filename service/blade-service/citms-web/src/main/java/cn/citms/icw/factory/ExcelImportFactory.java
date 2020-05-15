package cn.citms.icw.factory;

import cn.citms.icw.service.ExcelService;
import com.xiaoleilu.hutool.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * excel导入实现 工厂类
 * @author cyh
 */
public class ExcelImportFactory {

    private static Map<String, ExcelService> serviceMap = new ConcurrentHashMap<>(16);

    public static ExcelService getTypeService(String type){
        return serviceMap.get(type);
    }

    public static void initService(String type, ExcelService service) {
        if(StrUtil.isNotBlank(type)) {
            serviceMap.put(type, service);
        }
    }

}
