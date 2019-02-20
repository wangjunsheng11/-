package com.kakacl.product_service.controller.rest;

import com.github.pagehelper.PageInfo;
import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.config.ConstantDBStatus;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.*;
import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.IDUtils;
import com.kakacl.product_service.utils.Resp;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    @Autowired
    private AbilityService abilityService;

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
        params.put("del_flag", Constants.CONSTANT_1);
        boolean f = chatService.updateFriend(params);

        // 添加到某个组-添加组里必须需要最少一个好友
        params.put("id", IDUtils.genHadId());
        params.put("my_id", getUserid(request));
        params.put("friend_id", friend_id);
        params.put("group_name", group_name);
        params.put("create_time", System.currentTimeMillis() / Constants.CONSTANT_1000);
        params.put("create_by", getUserid(request));
        boolean flag = chatService.addFriend(params);
        if(f) {
            params.put("friend_id", friend_id);
            params.put("status_01", Constants.CONSTANT_50201);
            params.put("user_id", getUserid(request));
            chatService.agreeOneFriendAndMy(params);
        }
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
        // 获取申请我为好友的用户
        List<Map> data = chatService.findAddFriends(params);
        for (int i = Constants.CONSTANT_0; i < data.size(); i++) {
            // 这里是那个还有的主键
            Object friend_id = data.get(i).get("my_id");
            params.put("user_id", friend_id);
            Map friend = accountService.selectById(params);
            if(friend != null) {
                friend.remove("id_card");
            }
            data.get(i).put("friend", friend);
        }
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 同意添加单个好友
     * @description  同意添加单个好友，如果同意者需要添加申请者为好友;如果同意某一用户为好友，则被同意用户在同意用户的我的好友列表中。
     * @method post
     * @url /api/rest/v1.0.1/mychat/agreeOne
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param friend_id 必选 string 好友主键
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
        boolean flag = chatService.agreeOneFriendAndMy(params);

        if(flag) {
            // 将同意的还有存在我的好友列表中
            String group_name = Constant.MY_FRIENDS_GROUP_TITLE;
            params.put("id", IDUtils.genHadId());
            params.put("my_id", getUserid(request));
            params.put("friend_id", friend_id);
            params.put("group_name", group_name);
            params.put("create_time", System.currentTimeMillis() / Constants.CONSTANT_1000);
            params.put("create_by", getUserid(request));
            boolean flag02 = chatService.addFriend(params);

            // 同意为好友添加自己为好友
            params.put("friend_id", friend_id);
            params.put("status_01", Constants.CONSTANT_50201);
            params.put("user_id", getUserid(request));
            boolean flag03 = chatService.agreeOne(params);
            return Resp.success();
        } else {
            return Resp.fail(ErrorCode.CODE_6801);
        }
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 忽略好友
     * @description  忽略好友
     * @method post
     * @url /api/rest/v1.0.1/mychat/ignore
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
    @RequestMapping(value = "ignore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp ignore(HttpServletRequest request,
                         String  token,
                         @RequestParam(name="time")String time,
                         @RequestParam(name="friend_id")String friend_id,
                         Map params) {
        params.put("friend_id", friend_id);
        params.put("status_01", Constants.CONSTANT_50203);
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
        Map gradeMap = gradeService.selectById(params);
        List<Map> talentMap = talentService.selectList(params);
        List<Map> abilityMap = abilityService.selectByUserid(params);

        data.put("userMap", userMap);
        data.put("gradeMap", gradeMap);
        data.put("talentMap", talentMap);
        data.put("abilityMap", abilityMap);
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
     * @param content 可选 string 发送内容
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
     * @return {"status":"200","message":"请求成功","data":[{"del_flag":0,"create_time":1547008191,"user_name":"anonymous","roleid":"2","kaka_num":"149386","id_card":"22221","account_status":52000,"message":{"create_by":"1547006424247526","send_id":"1547006424247526","del_flag":0,"create_time":1549378177,"to_id":"1547008191643825","read_status":0,"id":"1549378177269689","title":"ttt","content":"ggg"},"create_by":"1","not_read_num":"1","head_path":"http://211.149.226.29:8081\\headFiles\\1547008191643825.jpg","phone_num":"13800138001","id":"1547008191643825","introduction":"没有简介"},{"del_flag":0,"create_time":1549206597,"user_name":"anonymous","roleid":"0","kaka_num":"152417","id_card":"782750","account_status":52000,"message":{"create_by":"1547006424247526","send_id":"1547006424247526","del_flag":0,"update_time":1550475103,"create_time":1550474954,"to_id":"1549206597769683","read_status":1,"id":"1550474954023574","title":"水","update_by":"1549206597769683","content":"水水"},"create_by":"1","not_read_num":"1","head_path":"http://211.149.226.29:8081/headFiles/1549206597769683.jpg","phone_num":"18556776796","id":"1549206597769683","introduction":"没有简介"},{"del_flag":0,"create_time":1549851523,"user_name":"客服115000","roleid":"5","kaka_num":"115000","id_card":"321283199502141218","account_status":52000,"message":{"create_by":"1547006424247526","send_id":"1547006424247526","del_flag":0,"create_time":1550324286,"to_id":"1","read_status":0,"id":"1550324286250123","title":"结算","content":"申请人姓名：王子\n手机号：13800138000\n金额：补贴进度：5001.00元\n参考开始时间：1970/01/03\n参考结束时间：1970/01/03\n条件：打卡50天\n备注：666666\n"},"create_by":"1","not_read_num":"1","head_path":"http://211.149.226.29:8081/zzf/file/user/head/images/default_female_03.png","phone_num":"13800138005","id":"1","introduction":"没有简介"},{"del_flag":0,"create_time":1550537584,"user_name":"-15500155000","roleid":"0","kaka_num":"114967","id_card":"110101199003077635","account_status":52000,"message":{"create_by":"1547006424247526","send_id":"1547006424247526","del_flag":0,"create_time":1550537760,"to_id":"1550537584576238","read_status":0,"id":"1550537760719904","title":"哈哈哈","content":"都饿了"},"create_by":"1","not_read_num":"1","head_path":"http://211.149.226.29:8081/headFiles/1550537584576238.jpg","phone_num":"15500155000","id":"1550537584576238","introduction":"没有简介"},{"del_flag":0,"create_time":1549377378,"user_name":"杨勇","roleid":"0","kaka_num":"190862","id_card":"410503198005132037","account_status":52000,"message":{"create_by":"1547006424247526","send_id":"1547006424247526","del_flag":0,"update_time":1550584866,"create_time":1550455812,"to_id":"1549377378941448","read_status":1,"id":"1550455812889450","title":"123","update_by":"1549377378941448","content":"123456"},"create_by":"1","not_read_num":"1","head_path":"http://211.149.226.29:8081/headFiles/1549377378941448.jpg","phone_num":"18662578122","id":"1549377378941448","introduction":"没有简介并不难"},{"del_flag":0,"create_time":1547006424,"user_name":"大哥#程浩南","roleid":"1","kaka_num":"128643","id_card":"320102199101013790","account_status":52000,"message":{"create_by":"1547006424247526","send_id":"1547006424247526","del_flag":0,"update_time":1548660204,"create_time":1548656108,"to_id":"1547006424247526","read_status":1,"id":"1548656108857614","title":"1","update_by":"1547006424247526","content":"1"},"create_by":"1","not_read_num":"1","head_path":"http://211.149.226.29:8081/headFiles/1547006424247526.jpg","phone_num":"13800138000","id":"1547006424247526","introduction":"帅 咯JOJOt"},{"del_flag":0,"create_time":1549851523,"user_name":"审判","roleid":"0","kaka_num":"164867","id_card":"321283199502141216","account_status":52000,"message":{"create_by":"1547006424247526","send_id":"1547006424247526","del_flag":0,"update_time":1550583506,"create_time":1550222916,"to_id":"1549851523970354","read_status":1,"id":"1550222916844784","title":"龙图","update_by":"1549851523970354","content":"拉拉裤"},"create_by":"1","not_read_num":"1","head_path":"http://211.149.226.29:8081/headFiles/1549851523970354.jpg","phone_num":"15764397609","id":"1549851523970354","introduction":"审判己身"}],"page":null,"ext":null}
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
                             @RequestParam(value = "content", required = false)String content,
                             @RequestParam(value = "scope", required = false, defaultValue = "global")String scope,
                             Map params) {
        params.put("search_key", StringEscapeUtils.escapeSql(content));
        params.put("user_id", getUserid(request));
        List<Map> data = chatService.findMessageByKey(params);
        /*for (int i = Constants.CONSTANT_0; i < data.size(); i++) {
            params.put("user_id", data.get(i).get("to_id"));
            Map user = accountService.selectById(params);
            if(user != null) {
                user.remove("id_card");
                data.get(i).put("user", user);
            }
        }*/

        // ---------------------------------------------
        List<Map> res = new ArrayList<>();
        for (int i = Constants.CONSTANT_0; i < data.size(); i++) {
            Object message = data.get(i);
//            log.info("user data {}", message);
            if(message != null) {
                // 如果发送ID是自己，则传递 to_id;如果发送ID是其他，则传递send_id
                String to_id = data.get(i).get("to_id").toString();
                String send_id = data.get(i).get("send_id").toString();
                if(getUserid(request).equals(send_id)) {
                    params.put("user_id", to_id);
                } else {
                    params.put("user_id", send_id);
                }
                Map user = accountService.selectById(params);
//                log.info("user {}", user);
                res.add(user);
            }
        }

        // 去重用户， 然后获取消息
//        log.info(" size " + res.size() + "");
        res = removeDuplicate(res);
//        log.info(" size last " + res.size() + "");

        // 根据好友id获取和当前用户的最后一条消息
        for (int i = 0; i < res.size(); i++) {
            Map obj = res.get(i);
            if(obj != null) {
                Object user_id = obj.get("id");
                params.put("to_id", getUserid(request));
                params.put("send_id", user_id);
                int notReadNum = chatService.findNotReadNum(params);
                obj.put("not_read_num", notReadNum);
//                log.info(" obj {}", obj);

                params.put("to_id", user_id);
                params.put("send_id", getUserid(request));
                Map message = chatService.findOneByUserid(params);
                res.get(i).put("message", message);
            }
        }

        for (int i = 0; i < res.size(); i++) {
            if(res.get(i) == null) {
                res.remove(i);
            }
        }

        return Resp.success(res);
//        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户聊天
     * @title 查询用户的消息列表-暂时不可用
     * @description 查询消息列表，每一个好友仅展示一条消息
     * @method get
     * @url /api/rest/v1.0.1/mychat/findListMessages
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param content 必选 string 发送内容
     * @param scope 可选 string 查找范围-默认全局-global
     * @return
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findListMessages", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findListMessages(HttpServletRequest request,
                             String time,
                             String token,
                             @RequestParam(value = "content", required = false)String content,
                             @RequestParam(value = "scope", required = false, defaultValue = "global")String scope,
                             Map params) {
        // 先查询消息，再查询消息中的用户，根据用户查询最近的一条消息
        params.put("search_key", StringEscapeUtils.escapeSql(content));
        params.put("user_id", getUserid(request));
        Map result = new HashMap();
        // 消息对象
        List<Map> data = chatService.findMessageByKey(params);

        // ------------------------------------------------------------------------------

        // 根据消息查询好友 如果已经查询就不再查询了
        List<Map> res = new ArrayList<>();
//        Map tm = new HashMap();

//        for (int i = 0; i < data.size(); i++) {
//            boolean f = true;
//            // 如果res中存在了数据 就不找了
//            for (int j = 0; j < res.size(); j++) {
//                Map uMap = (Map)res.get(j).get("user");
//                // 好友的主键
//                String user_id = uMap.get("id").toString();
//                String to_id = data.get(i).get("to_id").toString();
//                if(user_id.equals(to_id)) {
//                    f = false;
//                }
//            }
//            if(f) {
//                params.put("user_id", data.get(i).get("to_id"));
//                Map user = accountService.selectById(params);
//                tm.put("user", user);
//                // 根据用户查询最新的一条消息
//                params.put("send_id", data.get(i).get("to_id"));
//                params.put("to_id", data.get(i).get("to_id"));
//                Map messsage = chatService.findOneByUserid(params);
//                tm.put("message", messsage);
//                res.add(tm);
//            }
//        }


        // ------------------------------------
        // 消息
        log.info(" data {}", data);
        // 查询用户对象，如果发送ID是自己，则传递 to_id;如果发送ID是其他，则传递send_id
        for (int i = Constants.CONSTANT_0; i < data.size(); i++) {
            Object message = data.get(i);
            log.info("user data {}", message);
            if(message != null) {
                // 如果发送ID是自己，则传递 to_id;如果发送ID是其他，则传递send_id
                String to_id = data.get(i).get("to_id").toString();
                String send_id = data.get(i).get("send_id").toString();
                if(getUserid(request).equals(send_id)) {
                    params.put("user_id", to_id);
                } else {
                    params.put("user_id", send_id);
                }
                Map user = accountService.selectById(params);
                log.info("user {}", user);
            }
        }

        // 去重用户， 然后获取消息
        log.info(" size " + res.size() + "");
        res = removeDuplicate(res);
        log.info(" size last " + res.size() + "");
        return Resp.success(res);
    }

    private static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
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

        // 删除好友列表中的自己
        params.put("friend_id", getUserid(request));
        params.put("del_flag", del_flag);
        params.put("my_id", friend_id);
        boolean flag02 = chatService.updateFriend(params);

        if(flag) {
            return Resp.success();
        } else {
            return Resp.fail(ErrorCode.CODE_6801);
        }
    }
}
