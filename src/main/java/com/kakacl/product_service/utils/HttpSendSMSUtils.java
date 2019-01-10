package com.kakacl.product_service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 发送验证码工具类
 * @date 2010-01-10
 */
public class HttpSendSMSUtils {

    public void SendVariable(String url, String msg_model, List params, String account, String pswd) {
        boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
        String extno = null;	  // 扩展码(可选参数,可自定义)
        try {
            String param = params.toString().substring(1, params.toString().length() -1);
            String returnString = HttpSender.batchSendVariable(url,account, pswd,msg_model,param,needstatus,extno);
            System.out.println(returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
