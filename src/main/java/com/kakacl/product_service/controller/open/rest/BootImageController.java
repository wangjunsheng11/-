package com.kakacl.product_service.controller.open.rest;

import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.service.StartImageService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 程序启动图片控制器
 * @date 2019-01-14
 */
@RestController
@RequestMapping("/api/open/rest/{version}/boot")
public class BootImageController extends BaseController {

    @Autowired
    private StartImageService startImageService;

    /**
     * showdoc
     * @catalog v1.0.1/启动模块
     * @title 查询进入APP前的启动图片
     * @description 查询进入APP前的启动图片
     * @method get
     * @url /api/open/rest/v1.0.1/boot/list
     * @param time 必选 string 请求时间戳
     * @param apptype 必选 string 客户机型号
     * @param ios_version 必选 string 客户机系统版本
     * @return {"status":"200","message":"请求成功","data":[{"path":"http://p2.qhimgs4.com/t016097142787a45ee6.jpg","del_flag":0,"target_path":"http://p2.qhimgs4.com/t016097142787a45ee6.jpg","order":"order"},{"path":"http://p1.qhimgs4.com/t018a4812e1405d4856.jpg","del_flag":0,"target_path":"http://p1.qhimgs4.com/t018a4812e1405d4856.jpg","order":"order"},{"path":"http://p1.qhimg.com/t0153c5b5c6b7f126c6.jpg","del_flag":0,"target_path":"http://p1.qhimg.com/t0153c5b5c6b7f126c6.jpg","order":"order"}],"page":null,"ext":null}
     * @return_param status string 状态
     * @return_param message string 消息
     * @return_param path string 图片地址
     * @return_param target_path string 目标地址
     * @return_param order string 排序字段
     * @remark 这里是备注信息
     * @number 99
     */
    @GetMapping(value = "list", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp list(HttpServletRequest request, String time,
                     @RequestParam(name = "apptype", required = true)String apptype,
                     @RequestParam(name = "ios_version", required = true)String ios_version){
        return Resp.success(startImageService.list(null));
    }
}
