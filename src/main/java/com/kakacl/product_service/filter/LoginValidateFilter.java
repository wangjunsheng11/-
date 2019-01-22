package com.kakacl.product_service.filter;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 登录验证过滤器Filter
 * @date 2019-01-10
 */
@Component
@WebFilter(filterName="LoginValidateFilter", urlPatterns="/*")
@RefreshScope
public class LoginValidateFilter implements Filter {

    @Value("${version}")
    private String version;

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = "";
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Enumeration headerNames = request.getHeaderNames();
        // 遍历Header
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            if("token".equals(key)) {
                token = value;
            }
        }

        if(StringUtils.isBlank(token)) {
            token = servletRequest.getParameter("token");
        }

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getRequestURI();

        if(path.indexOf("/api/open/")> -1 ){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if(StringUtils.isNotBlank(token)) {
            // 判断是否过期 没有过期在header更新token
            try{
                Claims claims = JWTUtils.parseJWT(token);
                ((HttpServletResponse) servletResponse).setHeader("token", JWTUtils.createJWT(claims.getId(), claims.getIssuer(), claims.getSubject(), Constants.CONSTANT_1000 * Constants.CONSTANT_60 * Constants.CONSTANT_30));
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception e) {
                PrintWriter writer = null;
                servletResponse.setCharacterEncoding("UTF-8");
                servletResponse.setContentType("text/html; charset=utf-8");
                try {
                    writer = servletResponse.getWriter();
                    String userJson = "{\"code\":\" "+ ErrorCode.UNLOGIN_ERROR.getCode() +"\", \"message\": \""+ ErrorCode.UNLOGIN_ERROR.getMessage() +"\"}";
                    writer.print(userJson);
                } catch (IOException e1) {
                } finally {
                    if (writer != null)
                        writer.close();
                }
            }
        } else {
            PrintWriter writer = null;
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("text/html; charset=utf-8");
            try {
                writer = servletResponse.getWriter();
                String userJson = "{\"code\":\" "+ ErrorCode.PARAMETER_CHECK_ERROR.getCode() +"\", \"message\": \""+ ErrorCode.PARAMETER_CHECK_ERROR.getMessage() +"\"}";
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
