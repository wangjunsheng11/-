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

    // -------------------------------------------------BAIDU API ----------------------------------------------------------------------------------------------------

    public static final String APP_ID = "15541912";

    public static final String API_KEY = "ZAbVWVqucF8p7fGloiljrS7N";

    public static final String SECRET_KEY = "KtcLpy9MxvyUqzgxwHqez1Xe1z3QSH9i";


    // -------------------------------------------------REDIS ----------------------------------------------------------------------------------------------------

    // 测试容器
    public final static String COUNTKEY = "redis:lock:test";

    // 用户每天登录的容器
    public final static String EVERY_LOGIN_CONTENT = "redis:zzf:every_login_area:container";

    // 注册的容器
    public final static String REGISTER_CONTENT = "redis:zzf:register_area:container";

    // 求职者轨迹任务自动锁
    public final static String EMPLOYEE_ORBIT_TASK_AUTO_LOCK ="redis:zzf:task:employee_orbit_task_auto_lock";


    public final static String WALLET_CREATE_TASK_AUTO_TASK = "redis:zzf:task:wallet_create_task_auto_task:id:%s";

    public final static String WALLET_CREATE_TASK_AUTO_TASK_INCREMENT = "redis:zzf:task:wallet_create_task_auto_lock";



    // ------------------------------------------------- PING Type ----------------------------------------------------------------------------------------------------


    public final static String PING_TYPE_UP = "上班";

    public final static String PING_TYPE_DOWN = "下班";

}
