package com.kakacl.product_service.controller.open.rest;


import com.kakacl.product_service.controller.BaseController;
import com.kakacl.product_service.service.StartUpService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 启动程序控制器
 * @date 2019-01-14
 */
@RestController
@RequestMapping("/api/open/rest/{version}/startup")
@RefreshScope
public class StartUPController extends BaseController {

    @Autowired
    private StartUpService startUpService;

    /**
     * showdoc
     * @catalog v1.0.1/启动模块
     * @title 查询进入APP前的启动图片
     * @description 查询进入APP前的启动图片
     * @method get
     * @url /api/rest/v1.0.1/startup/list
     * @param time 必选 string 请求时间戳
     * @param apptype 必选 string 客户机型号
     * @return {"status":"200","message":"请求成功","data":[{"path":"http://p2.qhimgs4.com/t016097142787a45ee6.jpg","del_flag":0,"target_path":"http://p2.qhimgs4.com/t016097142787a45ee6.jpg","order":"order"},{"path":"http://p1.qhimgs4.com/t018a4812e1405d4856.jpg","del_flag":0,"target_path":"http://p1.qhimgs4.com/t018a4812e1405d4856.jpg","order":"order"},{"path":"http://p1.qhimg.com/t0153c5b5c6b7f126c6.jpg","del_flag":0,"target_path":"http://p1.qhimg.com/t0153c5b5c6b7f126c6.jpg","order":"order"}],"page":null,"ext":null}
     * @return_param status string 状态
     * @return_param message string 消息
     * @return_param path string 图片地址
     * @return_param target_path string 目标地址
     * @return_param order string 排序字段
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(name = "list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp list(HttpServletRequest request, String time, String apptype){
        return Resp.success(startUpService.list(null));
    }
}
