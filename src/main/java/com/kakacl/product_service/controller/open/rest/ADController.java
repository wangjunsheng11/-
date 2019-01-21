package com.kakacl.product_service.controller.open.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.ADService;
import com.kakacl.product_service.utils.Resp;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 广告接口控制器
 * @date 2019-01-11
 */
@RestController
@RequestMapping("/api/open/rest/{version}/ad")
public class ADController extends BaseController {

    @Autowired
    private ADService adService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    /**
     * showdoc
     * @catalog v1.0.1/广告模块
     * @title 查询进入APP前的广告
     * @description 查询进入APP前的广告
     * @method get
     * @url /api/open/rest/v1.0.1/ad/list
     * @param time 必选 string 请求时间戳
     * @param apptype 必选 string 客户机型号
     * @param ios_version 必选 string 客户机版本
     * @return {"status":"200","message":"请求成功","data":[{"path":"http://p2.qhimgs4.com/t016097142787a45ee6.jpg","del_flag":0,"target_path":"http://p2.qhimgs4.com/t016097142787a45ee6.jpg","order":"order"},{"path":"http://p1.qhimgs4.com/t018a4812e1405d4856.jpg","del_flag":0,"target_path":"http://p1.qhimgs4.com/t018a4812e1405d4856.jpg","order":"order"},{"path":"http://p1.qhimg.com/t0153c5b5c6b7f126c6.jpg","del_flag":0,"target_path":"http://p1.qhimg.com/t0153c5b5c6b7f126c6.jpg","order":"order"}],"page":null,"ext":null}
     * @return_param status string 状态
     * @return_param message string 消息
     * @return_param path string 图片地址
     * @return_param target_path string 目标地址
     * @return_param order string 排序字段
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_1, sec = Constants.CONSTANT_1)
    @GetMapping(value = "list", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp list(String time,
                     @RequestParam(name = "apptype", required = true)String apptype,
                     @RequestParam(name = "ios_version", required = true)String ios_version,
                     HttpServletRequest request){
        System.out.println(request.getRemoteAddr() + System.currentTimeMillis());
        return Resp.success(adService.selectAD(null));
    }

}
