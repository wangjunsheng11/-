package com.kakacl.product_service.utils;

/**
 * @Author: XiongFeng
 * @Description: 错误码
 * @Date: Created in 9:39 2018/4/10
 */
public enum ErrorCode {

    SYSTEM_ERROR(500, "系统错误"),
    PARAMETER_CHECK_ERROR(400, "参数校验错误"),
    AUTH_VALID_ERROR(701, "用户权限不足"),
    UNLOGIN_ERROR(401, "用户未登录或登录状态超时失效"),

    CODE_450(450, "账户或者密码不正确"),

    CODE_6000(6000, "数据繁忙，请再试一次吧"),
    CODE_6001(6001, "手机号码已经注册，如果您忘记密码，请找回密码"),

    CODE_6800(6800, "数据处理失败"),
    ;

    private final Integer value;
    private final String message;

    ErrorCode(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public String getCode() {
        return value.toString();
    }

    public static ErrorCode getByCode(Integer value) {
        for (ErrorCode _enum : values()) {
            if (_enum.getValue() == value) {
                return _enum;
            }
        }
        return null;
    }

}
