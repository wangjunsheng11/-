package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.utils.Resp;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 聊天控制器
 * @date 2019-01-14
 */
@RestController
@RequestMapping("/api/rest/{version}/mychat")
public class MyChatController extends BaseController {

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 查询好友
     * @description 查询好友
     * @method get
     * @url /api/rest/rest/v1.0.1/mychat/findFriends
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findFriends", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findFriends(HttpServletRequest request, String token, String time) {
        return Resp.success();
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 添加好友
     * @description 添加好友
     * @method post
     * @url /api/rest/v1.0.1/mychat/addFriends
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param friend_id 必选 string 需要添加的好友主键
     * @param group_name 必选 string 添加到组的名称
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "addFriends", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp addFriends(HttpServletRequest request, String friend_id, String group_name, String time) {
        // 添加到某个组-添加组里必须需要最少一个好友
        return Resp.success();
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 查询好友信息
     * @description 查询好友信息
     * @method get
     * @url /api/rest/v1.0.1/mychat/findFriendDetail
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param friend_id 必选 string 需要查询的好友主键
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findFriendDetail", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findFriendDetail(HttpServletRequest request, String time, String friend_id) {
        return Resp.success(); // 根据好友主键查询好友详情
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 发送消息
     * @description 发送消息
     * @method post
     * @url /api/rest/v1.0.1/mychat/sendMessage
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param to_friend_id 必选 string 接收者主键
     * @param content 必选 string 发送内容
     * @param type 可选 string 发送类型
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "sendMessage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp sendMessage(HttpServletRequest request, String time,
                            @RequestParam(value = "to_friend_id", required = true)String to_friend_id,
                            @RequestParam(value = "content", required = true)String content,
                            @RequestParam(value = "type", required = false)String type,
                            @RequestParam(value = "title", required = false)String title) {
        return Resp.success();
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 查询消息列表
     * @description 查询消息列表
     * @method get
     * @url /api/rest/v1.0.1/mychat/findMessages
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param content 必选 string 发送内容
     * @param scope 可选 string 查找范围-默认全局-global
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findMessages", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findMessages(HttpServletRequest request,
                             String time,
                             String token,
                             @RequestParam(value = "content", required = true)String content,
                             @RequestParam(value = "scope", required = false, defaultValue = "global")String scope) {
        return Resp.success();
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 删除好友
     * @description 删除好友
     * @method get
     * @url /api/rest/v1.0.1/mychat/delFriends
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param friend_id 必选 string 好友主键
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "delFriends", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp delFriends(HttpServletRequest request,
                           @RequestParam(value = "friend_id", required = true)String friend_id,
                           @RequestParam(value = "time", required = false)String time) {
        return Resp.success();
    }
}
