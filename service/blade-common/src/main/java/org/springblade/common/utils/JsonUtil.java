package org.springblade.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;


/**
 * ClassName:JsonUtil. <br/>
 * JSON工具类.
 * <p/>
 * 
 * @author onion
 * @version 1.0.0
 * @since JDK 1.6
 */
public abstract class JsonUtil {

    /**
     * readObject: 从json字符串中读取一个对象. <br/>
     *
     * @author onion
     * @param json
     *            需要转换成对象的json字符串
     * @param clazz
     *            要读取的对象类型
     * @return
     * @since JDK 1.6
     */
    public static <T> T readObject(String json, Class<T> clazz) {
        if (null == json || "".equals(json)) {
            return null;
        }
        Object obj = null;
        ObjectMapper om = new ObjectMapper();
        ObjectReader reader = om.reader(clazz);
        try {
            obj = reader.readValue(json);
            return clazz.cast(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * writeObject: 将一个对象转换成json字符串. <br/>
     *
     * @author onion
     * @param obj
     *            要转换成json的对象
     * @return 转换之后的json字符串
     * @since JDK 1.6
     */
    public static String writeObject(Object obj) {
        if (null == obj) {
            return null;
        }
        String json = null;
        ObjectMapper om = new ObjectMapper();
        ObjectWriter writer = om.writer();
        try {
            json = writer.writeValueAsString(obj);
        } catch (IOException e) {
            return null;
        }
        return json;
    }
}
