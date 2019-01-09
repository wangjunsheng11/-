package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.AbilityService;
import com.kakacl.product_service.service.AccountService;
import com.kakacl.product_service.service.GradeService;
import com.kakacl.product_service.utils.JWTUtils;
import com.kakacl.product_service.utils.Resp;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 我的资料模块控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/{version}/accountinfo")
public class AccountInfoController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private AbilityService abilityService;

    @Autowired
    private AccountService accountService;

    /*
     *
     * 用户基础信息查询
     * @author wangwei
     * @date 2019/1/9
      * @param token
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("findInfo")
    public Resp findInfo(@RequestParam(name = "token", required = true) String token){
        Map<String, Object> params = new HashMap();
        Map<String, Object> result = new HashMap();
        try {
            Claims claims = JWTUtils.parseJWT(token);
            String subject = claims.getSubject();
            String user_id = claims.getId();
            result.put("subject", subject);

            params.clear();
            params.put("user_id", user_id);
            // 获取当前系统的用户基本信息
            Map data =accountService.selectById(params);
            result.put("sys_account_info", data);
            // 用户等级
            data = gradeService.selectById(params);
            result.put("grade", data);
            // 我的能力列表数据
            params.clear();
            params.put("user_id", user_id);
            result.put("ability", abilityService.selectByUserid(params));
            return Resp.success(result);
        } catch (Exception e) {
            return Resp.fail();
        }
    }
}
