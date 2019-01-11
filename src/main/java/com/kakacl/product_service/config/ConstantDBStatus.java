package com.kakacl.product_service.config;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 数据库状态常量配置类
 * @date 2010-01-10
 */
public enum ConstantDBStatus {

    STATUS_DEL_FLAG_0(0, "正常"),
    STATUS_DEL_FLAG_1(1, "删除"),

    ACCOUNT_STATUS_52000(52000, "账号正常"),
    ACCOUNT_STATUS_52001(52000, "账号冻结"),

    // 入职状态 雇员的状态以此为唯一
    EMPLOYMENT_STATUS_52100(52100, "入职中"),
    EMPLOYMENT_STATUS_52101(52101,"待入职"),
    EMPLOYMENT_STATUS_52102(52102, "等待面试"),
    EMPLOYMENT_STATUS_52103(52103, "等待体检"),
    EMPLOYMENT_STATUS_52104(52104, "面试未通过"),
    EMPLOYMENT_STATUS_52105(52105, "体检未通过"),
    EMPLOYMENT_STATUS_52106(52106, "面试通过未入职"),
    EMPLOYMENT_STATUS_52107(52107, "体检通过未入职"),
    EMPLOYMENT_STATUS_52108(52108, "公司原因未入职"),
    EMPLOYMENT_STATUS_52109(52109, "待报道"),
    EMPLOYMENT_STATUS_52110(52110, "正常离职"),
    ;

    private final Integer value;
    private final String message;

    ConstantDBStatus(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public static String getName(int index) {
        for (ConstantDBStatus c : ConstantDBStatus.values()) {
            if (c.value == index) {
                return c.message;
            }
        }
        return "";
    }

    public static Integer getIndexByName(String name) {
        for (ConstantDBStatus c : ConstantDBStatus.values()) {
            if (c.message.equals(name)) {
                return c.value;
            }
        }
        return 0;
    }

}
