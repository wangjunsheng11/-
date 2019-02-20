package com.kakacl.product_service.controller.open.rest;

import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.AppVersionService;
import com.kakacl.product_service.service.StartImageService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    @Autowired
    private AppVersionService appVersionService;

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
    @AccessLimit(limit = Constants.CONSTANT_1,sec = Constants.CONSTANT_1)
    @GetMapping(value = "list", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp list(HttpServletRequest request, String time,
                     @RequestParam(name = "apptype", required = true)String apptype,
                     @RequestParam(name = "ios_version", required = true)String ios_version){
        return Resp.success(startImageService.list(null));
    }

    /**
     * showdoc
     * @catalog v1.0.1/启动模块
     * @title 获取最新版本
     * @description 查询进入APP前的启动图片
     * @method get
     * @url /api/open/rest/v1.0.1/boot/getNowVersion
     * @param time 必选 string 请求时间戳
     * @param apptype 必选 string 客户机型号，例如荣耀x8，dl100等，数据作为人工参考用
     * @param iso_version 必选 string 客户机系统版本
     * @param type 必选 string 客户机机型-华为，小米等，根据不同的机型推荐不同的应用市场
     * @return {"status":"200","message":"请求成功","data":{"del_flag":0,"update_path":"http://www.kakacl.com/file/update.apk","create_time":113,"id":3,"type":"ALL","version":"1.0.1","forch":0},"page":null,"ext":null}
     * @return_param version string 当前最新版本
     * @return_param type string 客户端机型
     * @return_param forch string 是否强制更新0否-1是
     * @return_param update_path string 更新地址
     * @remark 备注
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_1,sec = Constants.CONSTANT_1)
    @GetMapping(value = "getNowVersion", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp getNowVersion(HttpServletRequest request, String time,
                     @RequestParam(name = "apptype", required = true)String apptype,
                     @RequestParam(name = "iso_version", required = true)String iso_version,
                     @RequestParam(name = "type", required = true)String type,
                              Map params){
        Map result = new HashMap<>();
        params.put("type", type);
        List<Map> data = appVersionService.findNowVersion(params);
        if(data.size() == Constants.CONSTANT_0) {
            params.put("type", Constant.APP_TYPE);
            data = appVersionService.findNowVersion(params);
        }
        return Resp.success(data.get(Constants.CONSTANT_0));
    }

}
