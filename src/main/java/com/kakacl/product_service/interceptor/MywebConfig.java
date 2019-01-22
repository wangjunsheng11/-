package com.kakacl.product_service.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 *
 *
 * @author wangwei
 * @version v1.0.0
 * @description
 * @date
 */
@EnableWebMvc
@Configuration
public class MywebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 限流拦截器
        registry.addInterceptor(new AccessLimitInterceptor()).addPathPatterns("/**");
//        registry.addInterceptor(new AccessLimitInterceptor()).excludePathPatterns("/**");
//        registry.addInterceptor(new MyInterceptor())
//                .addPathPatterns("/**");
    }

    @Bean
    AccessLimitInterceptor localInterceptor() {
        return new AccessLimitInterceptor();
    }
}
