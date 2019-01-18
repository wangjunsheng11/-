package com.kakacl.product_service.controller.base;

import com.kakacl.product_service.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 控制器基类
 * @date 2019-01-09
 * consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
 */
public class BaseController extends ApplicationObjectSupport {

    public Logger log = LoggerFactory.getLogger(this.getClass());

    /*
     *
     * 请求方式判断
     * @author wangwei
     * @date 2019/1/9
     * @param request
     * @return boolean
     */
    public boolean isAjaxRequest(HttpServletRequest request) {
        if (!(request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)
                || "XMLHttpRequest".equalsIgnoreCase(request.getParameter("X_REQUESTED_WITH")))) {
            return false;
        }
        return true;
    }

    /*
     *
     * 获取请求属性封装为Map类型
     * @author wangwei
     * @date 2019/1/9
     * @param request
     * @return java.util.HashMap<java.lang.String,java.lang.Object>
     */
    public HashMap<String, Object> getRequestMapSingle(HttpServletRequest request) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        Map map = request.getParameterMap();
        for (Object o : map.keySet()) {
            String key = (String) o;
            conditions.put(key, ((String[]) map.get(key))[0]);
        }
        return conditions;
    }

    public String getUserid(HttpServletRequest request) {
        String userid = "";
        try {
            String token = request.getHeader("token");
            if(token == null) {
                token = request.getParameter("token").toString();
            }
            userid = JWTUtils.parseJWT(token).getId();
        } catch (Exception e) {
            userid = "";
        }
        return userid;
    }

}

