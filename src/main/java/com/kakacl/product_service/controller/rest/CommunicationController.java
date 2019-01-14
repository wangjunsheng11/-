package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.controller.BaseController;
import com.kakacl.product_service.utils.Resp;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 沟通的控制器
 * @date 2019-01-12
 */
@RestController
@RequestMapping("/api/rest/{version}/communication")
public class CommunicationController extends BaseController {

    /**
     * showdoc
     * @catalog v1.0.1/用户和客服聊天
     * @title 用户申诉
     * @description 类似于用户和客服聊天，发送消息
     * @method post
     * @url /api/open/rest/v1.0.1/do/sendPhoneCode
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param title 必选 string 标题-按模块分，可以默认，例如-关于薪资的的咨询
     * @param content 必选 string 内容
     * @param receiveid 必选 string 接收者ID
     * @param type 必选 string 消息类型-这里默认传递-申诉
     * @return {"status":"200","message":"请求成功","data":171330,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(value = "addInfo", method = RequestMethod.POST)
    public Resp addBackCard(HttpServletRequest request,
                            @RequestParam(name = "time", required = true)String time,
                            @RequestParam(name = "token", required = true) String token,
                            @RequestParam(name = "title", required = true) String title,
                            @RequestParam(name = "content", required = true) String content,
                            @RequestParam(name = "receiveid") String receiveid,
                            @RequestParam(name = "type") String type){

        return Resp.success();
    }

}
