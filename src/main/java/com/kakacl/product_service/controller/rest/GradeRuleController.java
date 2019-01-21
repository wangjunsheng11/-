package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.GradeRuleService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 等级规则控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/rest/{version}/graderule")
public class GradeRuleController extends BaseController {

    @Autowired
    private GradeRuleService gradeRuleService;

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/等级规则
     * @title 等级规则获取
     * @description 等级规则获取
     * @method get
     * @url /api/rest/v1.0.1/graderule/findList
     * @param token 必选 string token
     * @param time 必选 string 时间
     * @return {"status":"200","message":"请求成功","data":[{"create_by":"kakacl_zzf","del_flag":0,"create_time":1547008191,"description":"每日首次登陆","id":"1","order":1,"fraction":1},{"create_by":"kakacl_zzf","del_flag":0,"create_time":1547008191,"description":"正常上下班成功打卡一次","id":"2","order":2,"fraction":8},{"create_by":"kakacl_zzf","del_flag":0,"create_time":1547008191,"description":"补贴条件达成","id":"3","order":3,"fraction":10},{"create_by":"kakacl_zzf","del_flag":0,"create_time":1547008191,"description":"离职手续办理完成","id":"4","order":4,"fraction":3},{"create_by":"kakacl_zzf","del_flag":0,"create_time":1547008191,"description":"有理申诉","id":"5","order":5,"fraction":5}],"page":null,"ext":null}
     * @return_param ability object 能力
     * @return_param grade object 等级
     * @return_param sys_account_info object 用户信息
     * @return_param subject object 单点系统中的用户信息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findList", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findList(@RequestParam(name = "token", required = true) String token,  String time) {
        List<Map> result = gradeRuleService.selectList(null);
        return Resp.success(result);
    }
}
