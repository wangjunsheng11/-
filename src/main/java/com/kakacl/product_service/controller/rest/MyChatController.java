package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.ConstantDBStatus;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.AccountService;
import com.kakacl.product_service.service.ChatService;
import com.kakacl.product_service.service.GradeService;
import com.kakacl.product_service.service.TalentService;
import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.IDUtils;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AccountService accountService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private TalentService talentService;

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 查询好友
     * @description 查询好友
     * @method get
     * @url /api/rest/rest/v1.0.1/mychat/findFriends
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param search_key 必选 咔咔号或者手机号
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findFriends", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findFriends(HttpServletRequest request, String token, String time,
                            @RequestParam(name="search_key") String search_key, Map params) {
        params.put("search_key", search_key);
        List<Map> data = accountService.selectByPhoneORKakaNum(params);
        return Resp.success(data);
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
    public Resp addFriends(HttpServletRequest request,
                           String  token,
                           @RequestParam(name="friend_id")String friend_id,
                           @RequestParam(name="group_name") String group_name,
                           @RequestParam(name="time")String time,
                           Map params) {
        // 添加到某个组-添加组里必须需要最少一个好友
        params.put("id", IDUtils.genHadId());
        params.put("my_id", getUserid(request));
        params.put("friend_id", friend_id);
        params.put("group_name", group_name);
        params.put("create_time", System.currentTimeMillis() / Constants.CONSTANT_1000);
        params.put("create_by", getUserid(request));
        boolean flag = chatService.addFriend(params);
        if(flag) {
            return Resp.success();
        } else {
            return Resp.success(ErrorCode.CODE_6801);
        }
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 获取申请好友的列表
     * @description  获取申请好友的列表，包括提交申请的，忽略的
     * @method get
     * @url /api/rest/v1.0.1/mychat/findAddFriends
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findAddFriends", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findAddFriends(HttpServletRequest request,
                           String  token,
                           @RequestParam(name="time")String time,
                           Map params) {
        params.put("status_01", Constants.CONSTANT_50200);
        params.put("status_02", Constants.CONSTANT_50203);
        params.put("user_id", getUserid(request));
        List<Map> data = chatService.findAddFriends(params);
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 同意添加单个好友
     * @description  同意添加单个好友，如果同意者需要添加申请者为好友
     * @method get
     * @url /api/rest/v1.0.1/mychat/agreeOne
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "agreeOne", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp agreeOne(HttpServletRequest request,
                               String  token,
                               @RequestParam(name="time")String time,
                                @RequestParam(name="friend_id")String friend_id,
                               Map params) {
        params.put("friend_id", friend_id);
        params.put("status_01", Constants.CONSTANT_50201);
        params.put("user_id", getUserid(request));
        boolean flag = chatService.agreeOne(params);
        if(flag) {
            return Resp.success();
        } else {
            return Resp.fail(ErrorCode.CODE_6801);
        }
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 查询我的组
     * @description 查询我的组
     * @method get
     * @url /api/rest/v1.0.1/mychat/findGroup
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findGroup", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findGroup(HttpServletRequest request, String token, String time, Map params) {
        params.put("user_id", getUserid(request));
        List<Map> data = chatService.findGroup(params);
        return Resp.success(data);
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
    public Resp findFriendDetail(HttpServletRequest request, String token, String time,
                                 @RequestParam(value = "friend_id", required = false)String friend_id,
                                 Map params, Map data) {
        // 根据好友主键查询好友详情
        params.put("user_id", friend_id);
        Map userMap = accountService.selectById(params);
        // 石锤能力 - 天赋
        //params.put("user_id", friend_id);
        Map gradeMap = gradeService.selectById(params);
        //params.put("user_id", friend_id);
        List<Map> talentMap = talentService.selectList(params);

        data.put("userMap", userMap);
        data.put("gradeMap", gradeMap);
        data.put("talentMap", talentMap);
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 发送消息-过时
     * @description 发送消息-聊天使用接口-communication/addInfo
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
    public Resp sendMessage(HttpServletRequest request, String token, String time,
                            @RequestParam(value = "to_friend_id", required = true)String to_friend_id,
                            @RequestParam(value = "content", required = true)String content,
                            @RequestParam(value = "type", required = false)String type,
                            @RequestParam(value = "title", required = false)String title) {
        return Resp.fail();
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
                             @RequestParam(value = "scope", required = false, defaultValue = "global")String scope,
                                Map params) {
        params.put("search_key", content);
        params.put("user_id", getUserid(request));
        List<Map> data = chatService.findMessages(params);
        return Resp.success(data);
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
     * @param del_flag 必选 string 删除标志-1为删除， 0 为不删除
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "delFriends", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp delFriends(HttpServletRequest request, String  token,
                           @RequestParam(value = "friend_id", required = true)String friend_id,
                           @RequestParam(value = "del_flag", required = true)String del_flag,
                           @RequestParam(value = "time", required = false)String time, Map params) {
        params.put("friend_id", friend_id);
        params.put("del_flag", del_flag);
        params.put("my_id", getUserid(request));
        boolean flag = chatService.updateFriend(params);
        if(flag) {
            return Resp.success();
        } else {
            return Resp.fail(ErrorCode.CODE_6801);
        }
    }
}
