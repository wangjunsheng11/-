package com.kakacl.product_service.task;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.service.ChatService;
import com.kakacl.product_service.service.CustomerService;
import com.kakacl.product_service.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 初始化客服给用户发送使用说明任务
 * @date 2019-02-27
 */
@Component
@Configurable
@EnableScheduling
public class InitCustomerServiceMessageTask extends BaseTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private CustomerService customerService;

    // 客服给用户发送使用说明。 获取用户和客户没有进行沟通的，增加一条消息
    @Scheduled(cron = "* 0/30 *  * * * ")
    public void createCustomerServiceTask() {
        Map params = new HashMap<>();
        params.put("group_name", "客服");
        List<Map> data = customerService.findCustomerList(params);
        for (int i = 0; i < data.size(); i++) {
            Object user_id = data.get(i).get("my_id");
            Object server_id = data.get(i).get("friend_id");
            params.put("user_id", user_id);
            params.put("server_id", server_id);
            boolean flag = customerService.findMessageExist(params);
            if(!flag) {
                params.put("id", IDUtils.genHadId());
                params.put("send_id", server_id);
                params.put("to_id", user_id);
                params.put("content", "尊敬的用户您好，欢迎使用周周发APP。为了方便您的使用，请认真阅读《<a href='http://211.149.226.29:8081/zzf/file/default/pdf/use.pdf'>周周发用户使用说明</a>》，如果您在使用过程中有任何问题，欢迎咨询客服寻求帮助。");
                params.put("title", "欢迎使用周周发APP");
                params.put("create_by", server_id);
                params.put("create_time", System.currentTimeMillis() / Constants.CONSTANT_1000);
                flag = chatService.insert(params);
                log.info("InitCustomerServiceMessageTask: {}", flag + "");
            }
        }
    }
}
