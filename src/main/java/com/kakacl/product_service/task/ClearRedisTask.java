package com.kakacl.product_service.task;

import com.kakacl.product_service.config.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 定期清除Redis缓存数据的任务
 * @date 2019-02-12
 */
@Component
@Configurable
@EnableScheduling
public class ClearRedisTask {

    @Autowired
    private RedisTemplate redisTemplate;

    // 每天凌晨定期清除用户首次登录的数据
    @Scheduled(cron = "0 0 0 * * ?")
    public void employeeOrbitTask(){
        String key = String.format(Constant.EVERY_LOGIN_CONTENT + ":%s", "*");
        Set s = redisTemplate.keys(key);
        redisTemplate.delete(s);
    }
}
