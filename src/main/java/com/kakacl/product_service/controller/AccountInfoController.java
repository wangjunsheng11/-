package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.*;
import com.kakacl.product_service.utils.JWTUtils;
import com.kakacl.product_service.utils.Resp;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 我的资料模块控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/{version}/accountinfo")
public class AccountInfoController extends BaseController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private GradeRuleService gradeRuleService;

    @Autowired
    private AbilityService abilityService;

    @Autowired
    private AccountService accountService;

    // 积分
    @Autowired
    private TntegralService tntegralService;

    @Autowired
    private TntegralRuleService tntegralRuleService;

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

    /*
     *
     * 我的等级
     * @author wangwei
     * @date 2019/1/9
      * @param request
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("findgrade")
    public Resp findgrade (HttpServletRequest request) {
        Map<String, Object> params = new HashMap();
        Map<String, Object> result = new HashMap();
        String user_id = getUserid(request);
        params.put("user_id", user_id);
        Map grade = gradeService.selectById(params);
        result.put("grade", grade);
        List<Map> gradeRule = gradeRuleService.selectList(null);
        result.put("gradeRule", gradeRule);
        return Resp.success(result);
    }

    /*
     *
     * 获取当前用户积分
     * @author wangwei
     * @date 2019/1/9
     * @param request
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("findUsTertntegral")
    public Resp findUsTertntegral(HttpServletRequest request) {
        Map result = new HashMap();
        Map params = new HashMap();
        params.put("user_id", getUserid(request));
        result.put("tntegral", tntegralService.selectOneByUserid(params));
        result.put("tntegralList", tntegralRuleService.selectList(null));
        return Resp.success(result);
    }
}
