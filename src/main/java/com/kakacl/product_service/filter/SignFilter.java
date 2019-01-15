package com.kakacl.product_service.filter;

import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.SignUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 签名过滤器, 签名不拦截open下的接口
 * @date 2019-01-12
 */
@Component
@WebFilter(filterName="SignFilter", urlPatterns="/*")
@RefreshScope
public class SignFilter implements Filter {

    @Value("${version}")
    private String version;

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getRequestURI();

        if(path.indexOf("/api/open/")> -1 ){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Map<String, String> map = SignUtil.toVerifyMap(request.getParameterMap(),false);
        String secretKey =  map.get("secretKey");
        if (StringUtils.isEmpty(secretKey) || !map.get("secretKey").equals(SignUtil.getInstance().secretkey)){
            System.out.println("secretKey is err");
            PrintWriter writer = null;
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("text/html; charset=utf-8");
            try {
                writer = servletResponse.getWriter();
                String userJson = "{\"code\":\" "+ ErrorCode.CODE_431.getCode() +"\", \"message\": \""+ ErrorCode.CODE_431.getMessage() +"\"}";
                writer.print(userJson);
            } catch (IOException e1) {
            } finally {
                if (writer != null)
                    writer.close();
            }
        }
        if (SignUtil.getInstance().verify(map)){
            // 签名成功
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }else {
            PrintWriter writer = null;
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("text/html; charset=utf-8");
            try {
                writer = servletResponse.getWriter();
                String userJson = "{\"code\":\" "+ ErrorCode.CODE_430.getCode() +"\", \"message\": \""+ ErrorCode.CODE_430.getMessage() +"\"}";
                writer.print(userJson);
            } catch (IOException e1) {
            } finally {
                if (writer != null)
                    writer.close();
            }
        }


    }

    @Override
    public void destroy() {

    }
}
