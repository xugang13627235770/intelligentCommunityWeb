package org.springblade.common.enums;

/**
 * 字典kind
 */
public enum DictEnum {

    PLATETYPE("车牌类型", "2001"),
    PLATECOLOR("车牌颜色", "2005"),
    VEHICLETYPE("车辆类型", "2004"),
    VEHICLECOLOR("车身颜色", "2003"),
    VEHICLEBRAND("车辆品牌", "2007"),
    VEHICLEPARENTTYPE("车辆大类", "17001"),
    VEHICLECHILDTYPE("车辆子类", "17002"),
    SCAEPPED("是否报废车辆", "17003"),
    OILTYPE("车用燃料类型", "17004"),
    GENDER("性别", "2002"),
    INSTOREREASON("入库原因", "20022007"),
    BLOODTYPE("血型", "28008"),
    EDUCATION("学历", "28009"),
    YJLX("预警类型","8031"),
    HPYS("号牌颜色","2005"),
    HPZL("号牌种类","2001"),
    CSYS("车身颜色","2003"),
    CLLX("车辆类型","2004"),

    XB("性别类型","2002");

    private String name;

    private String index;


    private DictEnum(String name, String index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }


}
