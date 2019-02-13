package com.kakacl.product_service.controller.rest;

import com.github.pagehelper.PageInfo;
import com.kakacl.product_service.config.ConstantDBStatus;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.*;
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
    private UserDataService userDataService;

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
     * @return {"status":"200","message":"请求成功","data":[{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547006424,"user_name":"test","roleid":"1","kaka_num":"128643","id_card":"2222","phone_num":"13800138000","id":"1547006424247526","account_status":52000,"introduction":"456"}],"page":null,"ext":null}
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

        // 查询好友是否已经存在，如果存在则先删除
        params.put("friend_id", friend_id);
        params.put("my_id", getUserid(request));
        params.put("del_flag", ConstantDBStatus.STATUS_DEL_FLAG_1);
        chatService.updateFriend(params);

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
     * @return {"status":"200","message":"请求成功","data":[{"create_by":"1547006424247526","del_flag":0,"friend_id":"1547006424247526","create_time":1548664467,"group_name":"同学","my_id":"1547006424247526","id":"1548664465580429","update_by":"50200","status":50200},{"create_by":"1547006424247526","del_flag":0,"friend_id":"1547006424247526","create_time":1548664507,"group_name":"同事","my_id":"1547006424247526","id":"1548664507173319","update_by":"50203","status":50200}],"page":null,"ext":null}
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
     * @method post
     * @url /api/rest/v1.0.1/mychat/agreeOne
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":[{"group_name":"1"},{"group_name":"同事"}],"page":null,"ext":null}
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
     * @return {"status":"200","message":"请求成功","data":{"user_id":"1547008191643825","userMap":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547008191,"user_name":"anonymous","roleid":"2","kaka_num":"149386","id_card":"22221","phone_num":"13800138001","id":"1547008191643825","account_status":52000,"introduction":"没有简介"},"gradeMap":{"create_by":"1","del_flag":0,"create_time":1547008191,"user_id":"1547008191643825","grade":1,"id":"1547008191666655","fraction":0},"talentMap":[]},"page":null,"ext":null}
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
     * @param type 可选 string 发送类型-默认为-note,帖子
     * @param title 可选 string 标题-默认为消息
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
                            @RequestParam(value = "type", required = true, defaultValue = "note")String type,
                            @RequestParam(value = "title", required = true, defaultValue = "消息")String title) {
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
     * @return {"status":"200","message":"请求成功","data":[{"create_by":"1547006424247526","send_id":"1547006424247526","del_flag":0,"create_time":1549378177,"to_id":"1547008191643825","read_status":0,"id":"1549378177269689","title":"ttt","user":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547006424,"user_name":"test","roleid":"1","kaka_num":"128643","phone_num":"13800138000","id":"1547006424247526","account_status":52000,"introduction":"456"},"content":"ggg"}],"page":null,"ext":null}
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
        for (int i = Constants.CONSTANT_0; i < data.size(); i++) {
            String user_id = data.get(i).get("send_id") + "";
            params.put("user_id", user_id);
            Map user = accountService.selectById(params);
            user.remove("id_card");
            data.get(i).put("user", user);
        }
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 查询好友-根据组名称
     * @description 查询好友-根据组名称
     * @method get
     * @url /api/rest/v1.0.1/mychat/findFriendsByGroupName
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param group_name 必选 string 群组名称
     * @return {"status":"200","message":"请求成功","data":[{"create_by":"1547006424247526","del_flag":0,"friend_id":"1547008191643825","account_id":"1547006424247526","create_time":1547006424,"group_name":"好友","workHistory":{"image":"http://oy98yiue4.bkt.clouddn.com/FnhEPHpDnZY0-OieCmRZYUpc8Amj","orbit_id":"73","entry_time":"1548325839","company_id":7,"company_name":"凯博电脑-昆山-有限公司","resignation_time":"4118493723","work_status":"52100"},"id":"1","user":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547008191,"user_name":"anonymous","roleid":"2","kaka_num":"149386","id_card":"22221","phone_num":"13800138001","id":"1547008191643825","account_status":52000,"introduction":"没有简介"},"order":1,"status":50201},{"create_by":"1547006424247526","del_flag":0,"friend_id":"1547006424247526","account_id":"1547006424247526","create_time":1547006424,"group_name":"好友","workHistory":{"image":"http://oy98yiue4.bkt.clouddn.com/FnhEPHpDnZY0-OieCmRZYUpc8Amj","orbit_id":"73","entry_time":"1548325839","company_id":7,"company_name":"凯博电脑-昆山-有限公司","resignation_time":"4118493723","work_status":"52100"},"id":"2","user":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547006424,"user_name":"anonymous","roleid":"1","kaka_num":"128643","id_card":"2222","phone_num":"13800138000","id":"1547006424247526","account_status":52000,"introduction":"没有简介"},"order":1,"status":50201}],"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findFriendsByGroupName", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findFriendsByGroupName (HttpServletRequest request, String token, String time, String group_name) {
        Map params = new HashMap();
        params.put("account_id", getUserid(request));
        params.put("group_name", group_name);
        params.put("status", Constants.CONSTANT_50201);
        List<Map> result = chatService.findFriends(params);
        params.put("userList", result);

        for (int i = Constants.CONSTANT_0; i < result.size(); i++) {
            Object friend_id = result.get(i).get("friend_id");
            params.put("user_id", friend_id);
            params.put("currentPage", Constants.CONSTANT_1);
            params.put("pageSize", Constants.CONSTANT_10);
            Map data = accountService.selectHistoryByYserId(params);
            result.get(i).put("workHistory", data);
            // params.put("data", data);

            params.put("user_id", friend_id);
            Map user = accountService.selectById(params);
            result.get(i).put("user", user);
        }

        params.put("userList", result);
        return Resp.success(result);
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
