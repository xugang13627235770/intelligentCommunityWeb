package cn.citms.icw.Utils;

import com.xiaoleilu.hutool.date.DateTime;
import com.xiaoleilu.hutool.date.DateUtil;

import java.util.HashMap;
import java.util.Map;

public class DictUtils {

    private DictUtils(){}

    //登记户主
    public static final String DJHZ = "4";
    //户主家属
    public static final String HZJS = "100";
    //出租暂住
    public static final String CZZZ = "5";

    public static final String FORMATTER = "yyyy-MM";

    public static Map<String, Integer> getDicMonth(){
        Map<String, Integer> dicMonth = new HashMap<>(17);
        final int month = 12;
        DateTime date = DateUtil.date();
        for (int i = 0; i < month; i++) {
            dicMonth.put(DateUtil.format(date, FORMATTER), 0);
            date = DateUtil.offsetMonth(date, -1);
        }
        return dicMonth;
    }

    public static Map<String, String> getDictRy(){
        Map<String, String> dictRy = new HashMap<>(5);
        dictRy.put("登记户主", DJHZ);
        dictRy.put("户主家属", HZJS);
        dictRy.put("出租暂住", CZZZ);
        return dictRy;
    }
    public static Map<String, String> getDictTs(){
        Map<String, String> dictTs = new HashMap<>(9);
        dictTs.put("留守儿童", "1");
        dictTs.put("独居老人", "2");
        dictTs.put("少数民族", "3");
        dictTs.put("外籍人员", "4");
        dictTs.put("矫正人员", "5");
        dictTs.put("重点人员", "6");
        return dictTs;
    }
    public static Map<String, String> getDictZd(){
        Map<String, String> dictZd = new HashMap<>(9);
        dictZd.put("犯罪前科人员", "1");
        dictZd.put("涉稳人员", "2");
        dictZd.put("精神疾病患者", "3");
        dictZd.put("特定少数民族", "4");
        dictZd.put("涉恐人员", "5");
        dictZd.put("信访重点人员", "6");
        return dictZd;
    }
}
