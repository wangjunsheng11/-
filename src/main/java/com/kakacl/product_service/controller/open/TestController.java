package com.kakacl.product_service.controller.open;

import com.kakacl.product_service.controller.BaseController;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 测试
 * @date 2019-01-11
 */
@RestController
@RequestMapping("/api/open/{version}/test")
public class TestController extends BaseController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "get", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp get(HttpServletRequest request, String time, String key) {
        String redisValue=stringRedisTemplate.opsForValue().get(key);
        return Resp.success(redisValue);
    }

    @RequestMapping(value = "set", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp set(HttpServletRequest request, String time, String key) {
        Boolean res=true;
        res=stringRedisTemplate.opsForValue().setIfAbsent(key,key + System.nanoTime());
        return Resp.success(res);
    }

}
