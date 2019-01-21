package com.kakacl.product_service.limiting;

import java.lang.annotation.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 请求限流类
 * @date 2019-01-19
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    //标识 指定sec时间段内的访问次数限制
    int limit() default 1;

    //标识 时间段
    int sec() default 5;
}

