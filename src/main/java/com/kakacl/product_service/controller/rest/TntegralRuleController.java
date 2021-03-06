package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.TntegralRuleService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 积分规则控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/rest/{version}/tntegralrule")
@RefreshScope
public class TntegralRuleController extends BaseController {

    @Autowired
    private TntegralRuleService tntegralRuleService;

    /**
     * showdoc
     * @catalog v1.0.1/积分规则控制器
     * @title 获取积分规则
     * @description 获取积分规则
     * @method get
     * @url /api/rest/v1.0.1/tntegralrule/findList
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "findList", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findList(@RequestParam(name = "token", required = true) String token, String time) {
        List<Map> result = tntegralRuleService.selectList(null);
        return Resp.success(result);
    }
}
