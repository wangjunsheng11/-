package com.kakacl.product_service.interceptor;

import com.alibaba.fastjson.JSON;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.Resp;
import com.kakacl.product_service.utils.redis.FlowLimit;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 请求限流拦截器
 * @date 2019-01-19
 */
public class AccessLimitInterceptor implements HandlerInterceptor {

    //使用RedisTemplate操作redis
//    @Resource
//    public RedisTemplate<String, Integer> redisTemplate;

//    @Autowired
//    public RedisTemplate<String, Integer> redisTemplate;

//    @Autowired
//    public StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(handler.getClass());
        Class clazz = handler.getClass();
        System.out.println(clazz.getName());
        if (handler instanceof org.springframework.web.method.HandlerMethod) {
            org.springframework.web.method.HandlerMethod handlerMethod = (org.springframework.web.method.HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (!method.isAnnotationPresent(AccessLimit.class)) {
                return true;
            }
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }

            int limit = accessLimit.limit();
            int sec = accessLimit.sec();
            String key = getIpAddress(request) + request.getRequestURI();

            boolean flag = new FlowLimit().invoke(key, sec, limit);
            if(!flag) {
                JSON.toJSONString(Resp.fail(ErrorCode.CODE_432));
                output(response, JSON.toJSONString(Resp.fail(ErrorCode.CODE_432)));
                return false;
            } else {
                return true;
            }

            /*Object data = redisTemplate.opsForValue().get(key);
            System.out.println(String.format("data : " + data.toString()));
            Integer maxLimit = redisTemplate.opsForValue().get(key);
            if (maxLimit == null) {
                //set时一定要加过期时间
                redisTemplate.opsForValue().set(key, 1, sec, TimeUnit.SECONDS);
            } else if (maxLimit < limit) {
                redisTemplate.opsForValue().set(key, maxLimit + 1, sec, TimeUnit.SECONDS);
            } else {
                output(response, "请求太频繁!");
                return false;
            }*/
        }
        return true;
    }

    public void output(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(msg.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}

