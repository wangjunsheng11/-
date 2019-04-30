//package com.kakacl.product_service.task;
//
//import com.kakacl.product_service.config.Constant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//
//@Component
//@Configurable
//@EnableScheduling
//public class ClearPingCardRedisTask {
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    // 每天凌晨定期清除用户首次登录的数据
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void deleteDayTimeKeys(){
//        String key = String.format("DAYTIME*");
//        Set s = redisTemplate.keys(key);
//        redisTemplate.delete(s);
//    }
//    @Scheduled(cron = "0 0 12 * * ?")
//    public void deleteKeys(){
//        String key = String.format("NIGHT*");
//        Set s = redisTemplate.keys(key);
//        redisTemplate.delete(s);
//    }
//}
