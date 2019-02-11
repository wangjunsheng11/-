package com.kakacl.product_service.task;

import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.service.WalletService;
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
 * @description 钱包创建任务
 * @date 2019-02-11
 */
@Component
@Configurable
@EnableScheduling
public class WalletTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WalletService walletService;

    /*
     * 为没有钱包的用户创建一个钱包
     *
     * 如果用户的钱包已经删除，或用户删除，或钱包的状态不正常，这里不创建钱包。当前暂时设置的频率为一分钟一次，每次获取10条数据，从最早的数据开始获取。
     *
     * @author wangwei
     * @date 2019/2/11
      * @param
     * @return void
     */
    //每1分钟执行一次
    @Scheduled(cron = "0 */1 *  * * * ")
    public void createWalletTask(){
        final String key = String.format(Constant.WALLET_CREATE_TASK_AUTO_TASK, "walletCreateTask");
        Boolean res = true;
        while(res) {
            String value = UUID.randomUUID().toString() + System.nanoTime();
            res = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
            if (res) {
                stringRedisTemplate.opsForValue().increment(Constant.WALLET_CREATE_TASK_AUTO_TASK_INCREMENT, 1L);
                List<Map> data = walletService.selectListLimit10(null);
                Map params = new HashMap();
                for (int i = 0; i < data.size(); i++) {
                    params.clear();
                    String user_id = data.get(i).get("id") + "";
                    String id = IDUtils.genHadId();
                    long create_time = System.currentTimeMillis() / Constants.CONSTANT_1000;
                    params.put("id", id);
                    params.put("user_id", user_id);
                    params.put("create_time", create_time);
                    params.put("create_by", user_id);
                    boolean flag = walletService.insertOne(params);
                    if(!flag) {
                        // fail.
                    }
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
