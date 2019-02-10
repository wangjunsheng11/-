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
    ACCOUNT_STATUS_52001(52001, "账号冻结"),

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

    // 周周发申请结算状态
    EMPLOYMENT_STATUS_53000(53000, "未结算"),
    EMPLOYMENT_STATUS_53001(53001, "已申请"),
    EMPLOYMENT_STATUS_53002(53002, "已结算"),
    EMPLOYMENT_STATUS_53003(53003, "未到期"),

    // 补贴方式
    B_50101(50101, "补贴打卡日"),
    B_50102(50102, "补贴工作日"),
    B_50103(50103, "补贴小时工"),
    B_50104(50104, "补贴自然日"),
    B_50105(50105, "补贴定期返"),
    B_50106(50106, "补贴总价"),
    B_50107(50107, "补贴差价"),
    // 1.2.3升级仅使用一下数据分别补贴方式
    B_50108(50108, "定期补贴"),
    B_50109(50109, "到期补贴"),
    B_50110(50110, "管理费补贴"),
    B_50111(50111, "总价补贴"),

    D_50200(50200, "提交申请好友"),
    D_50201(50201, "同意好友"),
    D_50202(50202, "删除好友"),
    D_50203(50203, "忽略好友"),
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
