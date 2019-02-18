package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.ChatService;
import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.IDUtils;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.*;
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

    @Autowired
    private ChatService chatService;

    /**
     * showdoc
     * @catalog v1.0.1/聊天
     * @title 用户发送消息
     * @description 类似于用户和客服聊天，发送消息
     * @method post
     * @url /api/rest/v1.0.1/communication/addInfo
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
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "addInfo", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp addInfo(HttpServletRequest request,
                @RequestParam(name = "time", required = true)String time,
                @RequestParam(name = "token", required = true) String token,
                @RequestParam(name = "title", required = true) String title,
                @RequestParam(name = "content", required = true) String content,
                @RequestParam(name = "receiveid", required = true) String receiveid,
                @RequestParam(name = "type", required = true, defaultValue = "申诉") String type,
                        Map params){

        params.put("id", IDUtils.genHadId());
        params.put("send_id", getUserid(request));
        params.put("to_id", receiveid);
        params.put("content", content);
        params.put("title", title);
        params.put("create_by", getUserid(request));
        params.put("create_time", System.currentTimeMillis() / Constants.CONSTANT_1000);
        boolean flag = chatService.insert(params);
        if(flag) {
            return Resp.success();
        } else {
            return Resp.fail(ErrorCode.CODE_6801);
        }
    }

    /**
     * showdoc
     * @catalog v1.0.1/聊天
     * @title 查询和某一用户的聊天
     * @description 查询和某一用户的聊天，内容仅限于发送本人发送的消息内容。
     * @method get
     * @url /api/rest/v1.0.1/communication/findInfoBySendid
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param send_id 必选 string 发送者主键
     * @return {"status":"200","message":"请求成功","data":171330,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "findInfoBySendid", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findInfoBySendid(HttpServletRequest request,
                        @RequestParam(name = "time", required = true)String time,
                        @RequestParam(name = "token", required = true) String token,
                        @RequestParam(name = "send_id") String send_id,
                        Map params){

        params.put("to_id", getUserid(request));
        params.put("send_id", send_id);
        params.put("del_flag", Constants.CONSTANT_0);
        List<Map> data = chatService.findInfoBySendid(params);
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/聊天
     * @title 查询和某一用户的聊天,接收者
     * @description 查询和某一用户的聊天，内容仅限于本人发送的内容。
     * @method get
     * @url /api/rest/v1.0.1/communication/findInfoByToid
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param to_id 必选 string 接收者的用户主键
     * @return {"status":"200","message":"请求成功","data":171330,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "findInfoByToid", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findInfoByMyid(HttpServletRequest request,
                                 @RequestParam(name = "time", required = true)String time,
                                 @RequestParam(name = "token", required = true) String token,
                                 @RequestParam(name = "to_id") String to_id,
                                 Map params){

        params.put("to_id", to_id);
        params.put("send_id", getUserid(request));
        params.put("del_flag", Constants.CONSTANT_0);
        List<Map> data = chatService.findInfoBySendid(params);
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/聊天
     * @title 查询和某一用户的聊天,发送者，接收者
     * @description 查询和某一用户的聊天，发送者和接收者；data1-接收者user_id1， data2-接收者user_id2；data3是混合的用户消息。
     * @method get
     * @url /api/rest/v1.0.1/communication/findMessageByUserId
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param user_id1 必选 string 用户主键
     * @param user_id2 必选 string 用户主键
     * @return {"status":"200","message":"请求成功","data":171330,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "findMessageByUserId", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findMessageByUserId(HttpServletRequest request,
                               @RequestParam(name = "time", required = true)String time,
                               @RequestParam(name = "token", required = true) String token,
                               @RequestParam(name = "user_id1") String user_id1,
                               @RequestParam(name = "user_id2") String user_id2,
                               Map params){

        params.put("to_id", user_id1);
        params.put("send_id", user_id2);
        params.put("del_flag", Constants.CONSTANT_0);
        List<Map> data1 = chatService.findInfoBySendid(params);
        params.put("to_id", user_id2);
        params.put("send_id", user_id1);
        List<Map> data2 = chatService.findInfoBySendid(params);

        Map data = new HashMap();
        data.put("data1", data1);
        data.put("data2", data2);

        data1.addAll(data2);
//        Collections.sort(data1, new Comparator<Map<String, Object>>(){
//            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//                String data1 = (String)o1.get("id");//name1是从你list里面拿出来的一个
//                String data2 = (String)o2.get("id"); //name1是从你list里面拿出来的第二个name
//                return data1.compareTo(data2);
//            }
//        });
        data.put("data3", data1);
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/聊天
     * @title 标注聊天已读
     * @description 标注某一消息已读
     * @method post
     * @url /api/rest/v1.0.1/communication/updateInfo
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param id 必选 string 消息主键
     * @return {"status":"200","message":"请求成功","data":171330,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "updateInfo", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp updateInfo(HttpServletRequest request,
                        @RequestParam(name = "time", required = true)String time,
                        @RequestParam(name = "token", required = true) String token,
                        @RequestParam(name = "id", required = true) String id,
                        Map params){

        params.put("id", id);
        params.put("read_status", Constants.CONSTANT_1);
        params.put("update_by", getUserid(request));
        params.put("update_time", System.currentTimeMillis() / Constants.CONSTANT_1000);
        boolean flag = chatService.updateInfo(params);
        if(flag) {
            return Resp.success();
        } else {
            return Resp.fail(ErrorCode.CODE_6801);
        }
    }

}
