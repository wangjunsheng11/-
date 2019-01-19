package com.kakacl.product_service.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wangwei
 * @version v1.0.0
 * @description
 * @date
 */
@Configuration
public class MywebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessLimitInterceptor()).addPathPatterns("/**");
//        registry.addInterceptor(new MyInterceptor())
//                .addPathPatterns("/**");
    }
}
