package com.kakacl.product_service.config;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 短信模板常量
 * @date 2010-01-10
 */
public interface ConstantSMSMessage {
    // 发送短信的地址
    String SEND_URL = "http://sms.253.com/msg/HttpVarSM";

    // 注册验模板
    String CONSTANT_REGIATER = "尊敬的{$var}，您好！您注册验证码是{$var}，请在{$var}分钟内输入验证码，如果您没有操作，请忽略。";

    // 找回密码验证模板
    String CONSTANT_REPASSWORD = "尊敬的{$var}，您好！您申请找回密码，您的验证码是{$var}，请在{$var}分钟内输入验证码，如果您没有申请，请忽略。";

    // 登录账户尝试绑定非法银行卡
    String CONSTANT_ACCOUNT_ILLEGAL_BIND_BACK_NUM = "尊敬的{$var}，您好！您的账户尝试绑定非您名下银行卡，您的账户可能被盗，请及时修改密码或联系客服。";

}
