package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.service.TntegralService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 用户积分控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/rest/{version}/tntegral")
@RefreshScope
public class TntegralController  extends BaseController {

    @Autowired
    private TntegralService tntegralService;

    /**
     * showdoc
     * @catalog v1.0.1/用户相关
     * @title 获取当前用户积分
     * @description 获取当前用户积分
     * @method get
     * @url /api/rest/v1.0.1/tntegral/findUsTertntegral
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping("findUsTertntegral")
    public Resp findUsTertntegral(HttpServletRequest request) {
        Map params = new HashMap();
        params.put("user_id", getUserid(request));
        Map result = tntegralService.selectOneByUserid(params);
        return Resp.success(result);
    }

}
