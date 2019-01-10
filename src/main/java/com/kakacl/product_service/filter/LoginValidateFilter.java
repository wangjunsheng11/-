package com.kakacl.product_service.filter;

import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.JWTUtils;
import com.kakacl.product_service.utils.Resp;
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
import java.io.OutputStream;

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
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = ((HttpServletResponse) servletResponse).getHeader("token");
        if(StringUtils.isBlank(token)) {
            token = servletRequest.getParameter("token");
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getRequestURI();
        if(path.indexOf("/api/"+ version +"/account/sendPhoneCode")> -1 ){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if(path.indexOf("/api/"+ version +"/account/register")> -1 ){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if(path.indexOf("/api/"+ version +"/account/login")> -1 ){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if(StringUtils.isNotBlank(token)) {
            // 判断是否过期 没有过期在header更新token
            try{
                Claims claims = JWTUtils.parseJWT(token);
                ((HttpServletResponse) servletResponse).setHeader("token", JWTUtils.createJWT(claims.getId(), claims.getIssuer(), claims.getSubject(), 1000 * 60 * 30));
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception e) {
                servletResponse.setContentType("application/json; charset=utf-8");
                servletResponse.setCharacterEncoding("UTF-8");
                String userJson = Resp.fail(ErrorCode.UNLOGIN_ERROR).toString();
                OutputStream out = servletResponse.getOutputStream();
                out.write(userJson.getBytes("UTF-8"));
                out.flush();
            }
        } else {
            servletResponse.setContentType("application/json; charset=utf-8");
            servletResponse.setCharacterEncoding("UTF-8");
            String userJson = Resp.fail(ErrorCode.UNLOGIN_ERROR).toString();
            OutputStream out = servletResponse.getOutputStream();
            out.write(userJson.getBytes("UTF-8"));
            out.flush();
        }
    }

    @Override
    public void destroy() {
    }
}
