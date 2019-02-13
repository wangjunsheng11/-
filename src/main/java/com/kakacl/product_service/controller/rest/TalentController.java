package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.TalentService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 天赋控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/rest/{version}/talent")
@RefreshScope
public class TalentController extends BaseController {

    @Autowired
    private TalentService talentService;

    /**
     * showdoc
     * @catalog v1.0.1/用户相关
     * @title 获取当前用户天赋
     * @description 获取当前用户天赋
     * @method get
     * @url /api/rest/v1.0.1/talent/findList
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
    public Resp findList(HttpServletRequest request, String time, String token) {
        Map params = new HashMap();
        params.put("user_id", getUserid(request));
        List<Map> result = talentService.selectList(params);
        return Resp.success(result);
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户相关
     * @title 获取初始天赋
     * @description 获取初始天赋，天赋的名称name确定唯一性，不可重复。
     * @method get
     * @url /api/rest/v1.0.1/talent/selectListTalent
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "selectListTalent", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp selectListTalent(HttpServletRequest request, String time, String token) {
        Map params = new HashMap();
        List<Map> result = talentService.selectListTalent(params);
        return Resp.success(result);
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户相关
     * @title 编辑当前用户天赋
     * @description 编辑当前用户天赋-param_ids参数请调用获取初始天赋接口，用户默认天赋使用-天赋的名称-name字段进行关联。
     * @method post
     * @url /api/rest/v1.0.1/talent/edit
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param params_ids 必选 string 选择的参数主键,传递String数组
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "edit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp edit(HttpServletRequest request, String time, String token,
                     @RequestParam(value = "params_ids", required = true)String[] param_ids) {
        Map params = new HashMap();
        params.put("user_id", getUserid(request));
        params.put("param_ids", param_ids);
        boolean result = talentService.edit(params);
        return Resp.success(result);
    }

}
