package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.service.*;
import com.kakacl.product_service.utils.JWTUtils;
import com.kakacl.product_service.utils.Resp;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 我的资料模块控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/rest/{version}/accountinfo")
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

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 用户基础信息查询
     * @description 根据token查询账户基本信息
     * @method get
     * @url /api/rest/v1.0.1/accountinfo/findInfo
     * @param time 必选 string 当前时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":{"subject":"{menu_base=[{isopen=1, code=system, pcode=0, num=1, icon=fa-user, sys_type=kakacl_zzf, url=#, tips=, pcodes=[0],, name=系统管理, id=1, ismenu=1, levels=1, status=1}], cas_base={create_by=kakacl_zzf, del_flag=0, create_time=1547006424, pass_word=, kaka_num=128643, phone_num=13800138000, id=1547006424247526, sys_type=kakacl_zzf, email=, status=52000}}","sys_account_info":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547006424,"user_name":"anonymous","roleid":"1","kaka_num":"128643","id_card":"2222","phone_num":"13800138000","id":"1547006424247526","account_status":52000,"introduction":"没有简介"},"grade":{"create_by":"1","del_flag":0,"create_time":1547008191,"user_id":"1547006424247526","grade":1,"id":"2","fraction":0},"ability":[{"create_by":"1","del_flag":0,"user_id":"1547006424247526","img_path":"http://www.baidu.com./1.png","name":"1","remark":"2","id":"1","create_date":1547008191,"order":2},{"create_by":"1","del_flag":0,"user_id":"1547006424247526","img_path":"http://www.baidu.com./1.png","name":"嗯厉害1","remark":"很厉害 很厉害1","id":"2","create_date":1547008191,"order":1}]},"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(value = "findInfo" , method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findInfo(@RequestParam(name = "token", required = true) String token, String time){
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

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 我的等级
     * @description 我的等级
     * @method get
     * @url /api/rest/v1.0.1/accountinfo/findgrade
     * @param time 必选 string 当前时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":{"subject":"{menu_base=[{isopen=1, code=system, pcode=0, num=1, icon=fa-user, sys_type=kakacl_zzf, url=#, tips=, pcodes=[0],, name=系统管理, id=1, ismenu=1, levels=1, status=1}], cas_base={create_by=kakacl_zzf, del_flag=0, create_time=1547006424, pass_word=, kaka_num=128643, phone_num=13800138000, id=1547006424247526, sys_type=kakacl_zzf, email=, status=52000}}","sys_account_info":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547006424,"user_name":"anonymous","roleid":"1","kaka_num":"128643","id_card":"2222","phone_num":"13800138000","id":"1547006424247526","account_status":52000,"introduction":"没有简介"},"grade":{"create_by":"1","del_flag":0,"create_time":1547008191,"user_id":"1547006424247526","grade":1,"id":"2","fraction":0},"ability":[{"create_by":"1","del_flag":0,"user_id":"1547006424247526","img_path":"http://www.baidu.com./1.png","name":"1","remark":"2","id":"1","create_date":1547008191,"order":2},{"create_by":"1","del_flag":0,"user_id":"1547006424247526","img_path":"http://www.baidu.com./1.png","name":"嗯厉害1","remark":"很厉害 很厉害1","id":"2","create_date":1547008191,"order":1}]},"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(value = "findgrade" , method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findgrade (HttpServletRequest request, String time) {
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

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 获取当前用户积分
     * @description 获取当前用户积分
     * @method get
     * @url /api/rest/v1.0.1/accountinfo/findUsTertntegral
     * @param time 必选 string 当前时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":{"tntegral":{"create_by":"1","del_flag":0,"create_time":1547008191,"user_id":"1547006424247526","grade":1,"id":"1547006424247526","fraction":0},"tntegralList":[{"del_flag":0,"create_time":1547008191,"description":"上班正常签到","id":"1","order":1,"fraction":5},{"del_flag":0,"create_time":1547008191,"description":"随手扔垃圾","id":"2","order":2,"fraction":-2},{"del_flag":0,"create_time":1547008191,"description":"离职6个月内未办理离职手续","id":"3","order":3,"fraction":-10}]},"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(value = "findUsTertntegral" , method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findUsTertntegral(HttpServletRequest request) {
        Map result = new HashMap();
        Map params = new HashMap();
        params.put("user_id", getUserid(request));
        result.put("tntegral", tntegralService.selectOneByUserid(params));
        result.put("tntegralList", tntegralRuleService.selectList(null));
        return Resp.success(result);
    }
}
