package com.kakacl.product_service.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangwei
 * @version v1.0.0
 * @description
 * @date
 */
@Component
public class FlowLimit {

    private static Map<String, CacheValidate> cache = new HashMap<String, CacheValidate>();

    /**
     * 1. 接口限流实现 有一个API网关，出于对API接口的保护，需要建立一个流控功能，根据API名称，
     * 每秒钟最多只能请求指定的次数，超过限制则这分钟内返回错误，但下一分钟又可以正常请求。 接口定义
     * 对invoke方法进行调用，超过限制则return false
     *
     */
    public boolean invoke(String apiName, int sec, int limit) {
        if(apiName==null){
            return false;
        }
        CacheValidate cacheValidate = null;
        // 增加缓存中的值
        synchronized (cache) {
            cacheValidate = cache.get(apiName);
            if(cacheValidate==null){
                cacheValidate = new CacheValidate();
                cacheValidate.setTime(System.currentTimeMillis() / 1000 + sec);
                cacheValidate.setInvokeNum(1);
                cache.put(apiName, cacheValidate);
                return true;
            }
            return cacheValidate.isValidate(limit);
        }
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.submit(getTask());
        }
        service.shutdown();
    }

    public static Runnable getTask(){
        return new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    FlowLimit fLimit = new FlowLimit();
                    System.err.println(fLimit.invoke("aaa", 1, 1));
                }
            }
        };
    }

}
