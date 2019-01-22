package com.kakacl.product_service.config;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 配置类
 * @date 2019-01-09
 */
public class Constant {
    public static final String JWT_SECRET = "7786df7fc3a34e26a61c034d5ec8245d";

    // 公钥
    public static final String SIGN_SECRETKEY = "mysecret123456";

    // 私钥
    public static final String SIGN_APPKEY= "mykey123456";


    // -------------------------------------------------REDIS ----------------------------------------------------------------------------------------------------

    // 测试容器
    public final static String COUNTKEY = "redis:lock:test";

    // 用户每天登录的容器
    public final static String EVERY_LOGIN_CONTENT = "redis:every_login_area:container";

    // 注册的容器
    public final static String REGISTER_CONTENT = "redis:register_area:container";

}
