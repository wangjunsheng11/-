package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.GradeRuleService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 等级规则控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/{version}/graderule")
public class GradeRuleController {

    @Autowired
    private GradeRuleService gradeRuleService;

    @RequestMapping("findList")
    public Resp findList(@RequestParam(name = "token", required = true) String token) {
        List<Map> result = gradeRuleService.selectList(null);
        return Resp.success(result);
    }
}
