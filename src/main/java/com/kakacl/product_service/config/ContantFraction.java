package com.kakacl.product_service.config;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 积分配置类
 * @date 2019-01-22
 */
public interface ContantFraction {
    // 登录获取的积分
    int LOGIN = 5;

    // 上班打卡
    int UP_PING_CARD = 8;

    // 下班打卡
    int DOWN_PING_CARD = 8;

    // 补贴条件达成
    int SUBSIDY_CONDITION_ACHIEVE = 10;

    // 面试成功
    int INTERVIEW_SUCCESSFUL = 5;

    // 正常入职
    int ENTRY_WORK = 5;

    // 首次注册成功
    int TOP_REDISTER_SUCCESSFUL = 50;
}
