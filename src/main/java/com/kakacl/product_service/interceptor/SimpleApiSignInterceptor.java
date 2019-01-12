package com.kakacl.product_service.interceptor;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Deprecated 过时弃用
 * @author wangwei
 * @version v1.0.0
 * @description 签名验证拦截器
 * @date 2019-01-12
 */
@SuppressWarnings("SuspiciousMethodCalls")
public class SimpleApiSignInterceptor extends HandlerInterceptorAdapter {

    // 签名超时时长，默认时间为5分钟，ms
    private static final int SIGN_EXPIRED_TIME = 5 * 60 * 1000;

    private static final String API_SIGN_KEY_CONFIG_PATH = "/mop/common/system/api_sign_key_mapping.properties";

    private static final String SIGN_KEY = "sign";

    private static final String TIMESTAMP_KEY = "timestamp";

    private static final String ACCESS_KEY = "accessKey";

    private static final String ACCESS_SECRET = "accessSecret";

    private static Map<String, String> map = new ConcurrentHashMap<String, String>();


    /*static {
        // 从zk加载key映射到内存里面
        try {
            String data = ZKClient.get().getStringData(API_SIGN_KEY_CONFIG_PATH);
            Properties properties = new Properties();
            properties.load(new StringReader(data));
            for (Object key : properties.keySet()) {
                map.put(String.valueOf(key), properties.getProperty(String.valueOf(key)));
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        String timestamp = request.getParameter(TIMESTAMP_KEY);
        String accessKey = request.getParameter(ACCESS_KEY);
        String accessSecret = map.get(accessKey);

        if (!org.apache.commons.lang.StringUtils.isNumeric(timestamp)) {
            result.put("code", 1000);
            result.put("msg", "请求时间戳不合法");
            //WebUtils.writeJsonByObj(result, response, request);
            return false;
        }

        // 检查KEY是否合理
        if (StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(accessSecret)) {
            result.put("code", 1001);
            result.put("msg", "加密KEY不合法");
            // WebUtils.writeJsonByObj(result, response, request);
            return false;
        }
        Long ts = Long.valueOf(timestamp);
        // 禁止超时签名
        if (System.currentTimeMillis() - ts > SIGN_EXPIRED_TIME) {
            result.put("code", 1002);
            result.put("msg", "请求超时");
            //WebUtils.writeJsonByObj(result, response, request);
            return false;
        }

        if (!verificationSign(request, accessKey, accessSecret)) {
            result.put("code", 1003);
            result.put("msg", "签名错误");
            //WebUtils.writeJsonByObj(result, response, request);
            return false;
        }
        return true;
    }

    private boolean verificationSign(HttpServletRequest request, String accessKey, String accessSecret) throws UnsupportedEncodingException {
        Enumeration<?> pNames = request.getParameterNames();
        Map<String, Object> params = new HashMap<String, Object>();
        while (pNames.hasMoreElements()) {
            String pName = (String) pNames.nextElement();
            if (SIGN_KEY.equals(pName)) continue;
            Object pValue = request.getParameter(pName);
            params.put(pName, pValue);
        }
        String originSign = request.getParameter(SIGN_KEY);
        String sign = createSign(params, accessSecret);
        return sign.equals(originSign);
    }

    private String createSign(Map<String, Object> params, String accessSecret) throws UnsupportedEncodingException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuilder temp = new StringBuilder();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = String.valueOf(value);
            }
            temp.append(valueString);
        }
        temp.append("&").append(ACCESS_SECRET).append("=").append(accessSecret);
        //return MD5Util.MD52(temp.toString()).toUpperCase();
        return "";
    }
}
