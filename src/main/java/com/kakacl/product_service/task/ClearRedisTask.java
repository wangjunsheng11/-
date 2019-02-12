package com.kakacl.product_service.task;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

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

    // 每天凌晨定期清除用户登录的数据，获取积分


}
