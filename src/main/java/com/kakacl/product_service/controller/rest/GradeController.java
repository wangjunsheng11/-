package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.GradeRuleService;
import com.kakacl.product_service.service.GradeService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/*
 *
 * 等级控制器
 * @author wangwei
 * @date 2019/1/9
  * @param null
 * @return
 */
@RestController
@RequestMapping("/api/rest/{version}/grade")
public class GradeController extends BaseController {
    @Autowired
    private GradeService gradeService;

    @Autowired
    private GradeRuleService gradeRuleService;

    /**
     * 查询单个等级
     * @param user_id
     * @return
     */
    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户等级
     * @title 根据主键查询用户等级
     * @description 根据主键查询用户等级
     * @method get
     * @url /api/rest/v1.0.1/grade/selectById
     * @param userId 必选 string 用户主键
     * @param token 必选 string token
     * @param time 必选 string 请求时间戳
     * @return {"status":"200","message":"请求成功","data":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547008191,"user_name":"anonymous","roleid":"0","kaka_num":"149386","id_card":"2222","phone_num":"13800138001","id":"1547008191643825","account_status":1,"introduction":"没有简介"},"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "selectById", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object selectById(
            @RequestParam(name="userId",required=true) String userId,
            String token,
            String time){
        java.util.Map params = new HashMap();
        params.put("user_id", userId);
        return Resp.success(gradeService.selectById(params));
    }

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户等级
     * @title 获取当前用户的等级和经验
     * @description 获取当前用户的等级和经验，和下一等级和经验
     * @method get
     * @url /api/rest/v1.0.1/grade/selectGrade
     * @param token 必选 string token
     * @param time 必选 string 请求时间戳
     * @return {"status":"200","message":"请求成功","data":{"failed_rule":25,"grade":1,"fraction":10},"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @return_param grade string 当前等级
     * @return_param failed string 当前分数
     * @return_param failed_rule string 下一等级的分数
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "selectGrade", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object selectGrade(
            HttpServletRequest request,
            String token,
            String time){
        java.util.Map params = new HashMap();
        Map result = new HashMap<>();
        String user_id = getUserid(request);
        params.put("user_id", user_id);
        Map data = gradeService.selectById(params);
        log.info(" data {}", data);
        // 当前等级
        Object grade = data.get("grade");
        result.put("grade", grade);
        params.put("grade", grade);
        // 当前分数
        Object fraction = data.get("fraction");
        result.put("fraction", fraction);
         // 下一等级的分数
        Map gradeRule = gradeRuleService.selectMapByUpGrade(params);
        result.put("failed_rule", gradeRule.get("fraction"));
        return Resp.success(result);
    }
}
