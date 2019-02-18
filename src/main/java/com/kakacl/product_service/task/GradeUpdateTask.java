package com.kakacl.product_service.task;

import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.service.GradeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.UUID;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 用户等级更新任务
 * @date 2019-02-18
 */
@Component
@Configurable
@EnableScheduling
public class GradeUpdateTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private GradeService gradeService;

    // 每天1点定期更新用户等级
    @Scheduled(cron = "0 0 1 * * ?")
    public void gradeTask(){
        // 加Redis同步锁
        final String key = String.format("redis:zzf:task:grade_task_auto_task:id:%s", "gradeTask");
        Boolean res = true;
        while(res) {
            String value = UUID.randomUUID().toString() + System.nanoTime();
            res = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
            if (res) {
                stringRedisTemplate.opsForValue().increment(Constant.EMPLOYEE_ORBIT_TASK_AUTO_LOCK, 1L);
                try {
                    res = false;
                    List<Map> data = gradeService.selectList();
                    for (int i = 0; i < data.size(); i++) {
                        Object user_id  = data.get(i).get("user_id");

                    }
                } catch (Exception e) {
                }finally {
                    // 释放锁
                    String redisValue = stringRedisTemplate.opsForValue().get(key);
                    if (StringUtils.isNotBlank(redisValue) && redisValue.equals(value)) {
                        stringRedisTemplate.delete(key);
                    }
                }
            } else {
                res = true;
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                }
            }
        }


    }

}
