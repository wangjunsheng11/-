package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.TntegralRuleService;
import com.kakacl.product_service.utils.Resp;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RequestMapping("/api/{version}/tntegralrule")
@RefreshScope
public class TntegralRuleController {

    @Autowired
    private TntegralRuleService tntegralRuleService;

    @RequestMapping("findList")
    public Resp findList(@RequestParam(name = "token", required = true) String token) {
        List<Map> result = tntegralRuleService.selectList(null);
        return Resp.success(result);
    }
}
