package com.kakacl.product_service.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 日志过滤器
 * @date 2019-01-14
 */
@Component
@WebFilter(filterName="LogFilter", urlPatterns="/*")
@RefreshScope
public class LogFilter implements Filter {

    public Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) request;
        String path = r.getQueryString();
        if (path != null) {
            Map<String, String> map = new HashMap<String, String>();
            Enumeration headerNames = ((HttpServletRequest) request).getHeaderNames();
            while (headerNames.hasMoreElements()) {//循环遍历Header中的参数，把遍历出来的参数放入Map中
                String key = (String) headerNames.nextElement();
                String value = ((HttpServletRequest) request).getHeader(key);
                map.put(key, value);
            }
            log.info("LogFilter参数Header ： ${}", map.toString());

            Map map1 = request.getParameterMap();
            Set keSet=map1.entrySet();
            for(Iterator itr=keSet.iterator();itr.hasNext();){
                Map.Entry me=(Map.Entry)itr.next();
                Object ok=me.getKey();
                Object ov=me.getValue();
                String[] value=new String[1];
                if(ov instanceof String[]){
                    value=(String[])ov;
                }else{
                    value[0]=ov.toString();
                }

                for(int k=0;k<value.length;k++){
                    log.info("LogFilter参数request ： ${}", ok + " - " + value[k]);
                }
            }
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
