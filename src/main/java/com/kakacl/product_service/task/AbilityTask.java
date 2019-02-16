package com.kakacl.product_service.task;

import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.service.AbilityService;
import com.kakacl.product_service.utils.IDUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 能力赋予任务,暂定给没有能力的用户都添加已个能力，正式中需要根据条件进行计算，赋予相关能力
 * @date 2019-02-16
 */
@Component
@Configurable
@EnableScheduling
public class AbilityTask {

    @Autowired
    private AbilityService abilityService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 */1 *  * * * ")
    public void createAbilityTask(){

        final String key = String.format(Constant.ABILITY_TASK_AUTO_TASK);
        Boolean res = true;
        while(res) {
            String value = UUID.randomUUID().toString() + System.nanoTime();
            res = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
            if (res) {
                stringRedisTemplate.opsForValue().increment(Constant.ABILITY_TASK_AUTO_TASK_INCREMENT, 1L);

                Map params = new HashMap();
                // 查询没有能力的用户，赋予能力
                List<Map> result = abilityService.selectAblityListTop10(null);

                List<Map> resultRule = abilityService.selectRuleList(null);

                for(Map tmp : result) {
                    String user_id = tmp.get("id").toString();
                    Random random = new Random();
                    int n = random.nextInt(resultRule.size());
                    Map data = resultRule.get(n);
                    params.put("id", IDUtils.genHadId());
                    params.put("user_id", user_id);
                    params.put("create_date", System.currentTimeMillis() / Constants.CONSTANT_1000);
                    params.put("create_by", user_id);
                    params.put("name", data.get("name"));
                    params.put("img_path", data.get("img_path"));
                    params.put("remark", data.get("remark"));
                    boolean flag = abilityService.insertOne(params);
                    System.out.println(flag + " - " +user_id);
                }

                try {
                    res = false;
                } catch (Exception e) {
                    e.getMessage();
                } finally {
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
