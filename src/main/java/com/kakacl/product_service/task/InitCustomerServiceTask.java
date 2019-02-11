package com.kakacl.product_service.task;

import com.kakacl.product_service.config.ConstantDBStatus;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.service.CustomerService;
import com.kakacl.product_service.utils.IDUtils;
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
 * @description 初始化客服
 * @date 2019-02-11
 */
@Component
@Configurable
@EnableScheduling
public class InitCustomerServiceTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CustomerService customerService;

    /*
     * 为用户初始化一个客服
     *
     * 如果用户有一个客服，则不设置
     * 1分钟获取一次，每次获取10条。按时间先后顺序依次进行。
     *
     * @author wangwei
     * @date 2019/2/11
      * @param
     * @return void
     */
    @Scheduled(cron = "0 */1 *  * * * ")
    public void createCustomerServiceTask(){
        Map params = new HashMap<>();
        params.put("group_name", "客服");
        List<Map> data = customerService.selectListLimit10(params);
        for (int i = 0; i < data.size(); i++) {
            // 随机获取一个客服
            String user_id = data.get(i).get("id") + "";
            String id = IDUtils.genHadId();
            String friend_id = "1";
            int status = Constants.CONSTANT_50201;
            long create_time = System.currentTimeMillis() / Constants.CONSTANT_1000;
            params.put("id", id);
            params.put("my_id", user_id);
            params.put("friend_id", friend_id);
            params.put("group_name", "客服");
            params.put("status", status);
            params.put("create_time", create_time);
            params.put("create_by", user_id);
            boolean flag = customerService.insertOne(params);
            if(!flag) {
                // fail.
            }
        }
    }
}
