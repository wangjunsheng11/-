package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.controller.BaseController;
import com.kakacl.product_service.utils.Resp;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    /*
     * 功能描述
     * 1.查询我的好友-联系人-分组概念
     * 2.消息列表-搜索某一消息
     * 3.查看某好友详情
     * 4.添加好友-手机号-咔咔号
     * 5.评分
     * 6.发送消息
     * 7.删除好友
     */

    @RequestMapping(value = "findFriends", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findFriends(HttpServletRequest request) {
        return Resp.success();
    }

    @RequestMapping(value = "addFriends", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp addFriends(HttpServletRequest request) {
        // 添加到某个组-添加组里必须需要最少一个好友
        return Resp.success();
    }

    @RequestMapping(value = "findFriendDetail", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findFriendDetail(HttpServletRequest request) {
        return Resp.success(); // 根据好友主键查询好友详情
    }

    // 发送消息
    @RequestMapping(value = "sendMessage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp sendMessage(HttpServletRequest request) {
        return Resp.success();
    }

    // 查询消息列表- 支持模糊查询
    @RequestMapping(value = "findMessages", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findMessages(HttpServletRequest request) {
        return Resp.success();
    }

    // 删除好友
    @RequestMapping(value = "delFriends", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp delFriends(HttpServletRequest request) {
        return Resp.success();
    }
}
