package com.kakacl.product_service.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 总过滤器
 * @date 2019-10-12
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean buildBFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.setFilter(new SignFilter());
        filterRegistrationBean.setName("SignFilter");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean buildCFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.setFilter(new LoginValidateFilter());
        filterRegistrationBean.setName("LoginValidateFilter");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
