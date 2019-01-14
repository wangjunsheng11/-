package com.kakacl.product_service.controller.open.rest;

import com.kakacl.product_service.controller.BaseController;
import com.kakacl.product_service.service.ProtocolService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 协议控制器
 * @date 2019-01-14
 */
@RestController
@RequestMapping("/api/open/rest/{version}/protocol")
public class ProtocolController extends BaseController {

    @Autowired
    private ProtocolService protocolService;

    /**
     * showdoc
     * @catalog v1.0.1/协议相关
     * @title 根据group获取协议
     * @description 协议获取相关
     * @method post
     * @url /api/open/rest/v1.0.1/protocol/findProtocol
     * @param group_name 必选 string 获取哪个组的协议
     * @param time 必选 string 当前时间戳
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message String 消息
     * @return_param status string 状态
     * @return_param title string 标题
     * @return_param content string 协议内容
     * @remark 备注信息
     * @number 99
     */
    @RequestMapping(value = "findProtocol", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findProtocol(
            @RequestParam(name="group_name", required=true) String group_name,
            @RequestParam(name="time", required=true)String time) {
        Map params = new HashMap<>();
        params.put("group_name", group_name);
        List<Map> data = protocolService.findProtocol(params);
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/协议相关
     * @title 获取协议组
     * @description 协议获取相关
     * @method post
     * @url /api/open/rest/v1.0.1/protocol/findGroup
     * @param type 必选 string 获取协议的属性，这里默认传递all
     * @param time 必选 string 当前时间戳
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message String 消息
     * @return_param status string 状态
     * @remark 详细的协议根据group_name获取
     * @number 99
     */
    @RequestMapping(value = "findGroup", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findGroup(
            @RequestParam(name="type", required=true, defaultValue = "all") String type,
            @RequestParam(name="time", required=true)String time) {
        List<Map> data = protocolService.findProtocol(null);
        return Resp.success(data);
    }

}
