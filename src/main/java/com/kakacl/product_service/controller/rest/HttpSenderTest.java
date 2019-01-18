package com.kakacl.product_service.controller.rest;

/**
 * @author wangwei
 * @version v1.0.0
 * @description
 * @date
 */

import com.kakacl.product_service.utils.HttpSender;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 普通短信发送测试Main
 * @author
 *
 */
public class HttpSenderTest {

    private static String account="N9960209"; // 发送短信的账号(非登录账号) (示例:N987654)
    private static String pswd="iFqXn8Etz";// 发送短信的密码(非登录密码)

    //调用Main() 方法前记得更改 —— 电话号码、 账号、密码  等信息
    public static void main(String[] args) throws UnsupportedEncodingException {

        //sendMessage();//调用发送普通短信方法

        //sendDelay(); //调用定时短信测试

        SendVariable();//调用变量短信测试

        //getReport();//拉取状态报告测试

        getbalance();//查询余额

        List sms_params = new ArrayList();
        sms_params.add("2");
        sms_params.add(10);

        System.out.println(sms_params.toString().substring(1, sms_params.toString().length() -1));
    }

    //发送普通短信方法
    public static void sendMessage(){
        String url = "http://sms.253.com/msg/send";  //应用地址 (无特殊情况时无需修改)
        String msg = "【253云通讯】测试短信,您的验证码是123456" ; //您的签名+短信内容

        String extno = null;     	// 扩展码(可选参数,可自定义)
        String phone="187********";	// 短信接收号码,多个号码用英文,隔开
        String rd="1";				// 是否需要状态报告(需要:1,不需要:0)
        try {
            String returnString2 = HttpSender.SendPost(url,account, pswd,msg.toString(),phone,rd,extno);
            System.out.println(returnString2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //定时短信测试
    public static void sendDelay() throws UnsupportedEncodingException {
        String url = "http://sms.253.com/msg/sendDelay";// 应用地址(无特殊情况时无需修改)
        String msg = "【253云通讯】测试定时短信内容";            // 短信内容
        String rd="1";					// 是否需要状态报告(需要:1,不需要:0)
        String ex = null;     			// 扩展码(可选参数,可自定义)
        String delay="201703091317";	//时间格式 yyyyMMDDHHmm (例如：201703091301——2017年3月9日13点00)
        String phone="187********";		// 短信接收号码

        try {
            String returnString = HttpSender.senddelay(url, account, pswd, phone, msg, rd, ex, delay);
            System.out.println(returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //变量短信测试
    public static void SendVariable() {
        String url = "http://sms.253.com/msg/HttpVarSM";// 应用地址(无特殊情况时无需修改)
        String msg = "尊敬的{$var},您好,这是一条测试变量短信,您的验证码是{$var},{$var}分钟内有效";// 短信内容
//        String params = "187********,田女士,654321,3;130********,李先生,859437,5";//第一个固定为手机号码,之后参数一一对应各个变量,每组参数中间用英文分号;分开
        String params = "17091987731,田女士,654321,3";
        boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
        String extno = null;	  // 扩展码(可选参数,可自定义)

        try {
            String returnString = HttpSender.batchSendVariable(url,account, pswd,msg,params,needstatus,extno);
            System.out.println(returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询账户余额
    public static void getbalance() {
        String url = "http://sms.253.com/msg/balance";// 应用地址(无特殊情况时无需修改)
        try {
            String returnString = HttpSender.query(url,account, pswd);
            System.out.println(returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //拉取状态报告
    public static void getReport() {
        String url = "http://stat.253.com/dragon_api/pull/report";// 应用地址(无特殊情况时无需修改)
        String key = "*******";		 //联系客服配置 key 方可拉取状态报告
        String count="2";			 //拉取状态报告个数
        try {
            String returnString = HttpSender.getFlockStatusReport(url, account, key, count);
            System.out.println(returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
