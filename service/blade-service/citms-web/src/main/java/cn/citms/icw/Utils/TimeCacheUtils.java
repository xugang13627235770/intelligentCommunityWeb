package cn.citms.icw.Utils;

import com.xiaoleilu.hutool.cache.impl.TimedCache;

/**
 * @author cyh
 */
public class TimeCacheUtils {

    /**
     * 字典缓存 1分钟
     */
    public static TimedCache<String, String> DICT_VALUE_CACHE = new TimedCache<>(1*60*1000L);
}
