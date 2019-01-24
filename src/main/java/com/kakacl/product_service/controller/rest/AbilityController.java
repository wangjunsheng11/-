package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.AbilityService;
import com.kakacl.product_service.utils.IDUtils;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 能力控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/rest/{version}/ability")
public class AbilityController extends BaseController {

    @Autowired
    private AbilityService abilityService;

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 当前用户查询能力
     * @description 当前用户查询能力
     * @method get
     * @url /api/rest/v1.0.1/ability/findOne
     * @param token 必选 string token
     * @param time 必选 string 时间
     * @return {"status":"200","message":"请求成功","data":{"subject":"{menu_base=[{isopen=1, code=system, pcode=0, num=1, icon=fa-user, sys_type=kakacl_zzf, url=#, tips=, pcodes=[0],, name=系统管理, id=1, ismenu=1, levels=1, status=1}], cas_base={create_by=kakacl_zzf, del_flag=0, create_time=1547006424, pass_word=, kaka_num=128643, phone_num=13800138000, id=1547006424247526, sys_type=kakacl_zzf, email=, status=52000}}","sys_account_info":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547006424,"user_name":"anonymous","roleid":"1","kaka_num":"128643","id_card":"2222","phone_num":"13800138000","id":"1547006424247526","account_status":52000,"introduction":"没有简介"},"grade":{"create_by":"1","del_flag":0,"create_time":1547008191,"user_id":"1547006424247526","grade":1,"id":"2","fraction":0},"ability":[{"create_by":"1","del_flag":0,"user_id":"1547006424247526","img_path":"http://www.baidu.com./1.png","name":"1","remark":"2","id":"1","create_date":1547008191,"order":2},{"create_by":"1","del_flag":0,"user_id":"1547006424247526","img_path":"http://www.baidu.com./1.png","name":"嗯厉害1","remark":"很厉害 很厉害1","id":"2","create_date":1547008191,"order":1}]},"page":null,"ext":null}
     * @return_param ability object 能力
     * @return_param grade object 等级
     * @return_param sys_account_info object 用户信息
     * @return_param subject object 单点系统中的用户信息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "findOne", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findOne(HttpServletRequest request, String time, String token) {
        Map params = new HashMap();
        params.put("user_id", getUserid(request));
        List<Map> data = abilityService.selectByUserid(params);
        return Resp.success(data);
    }

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 添加能力
     * @description 添加能力
     * @method post
     * @url /api/rest/v1.0.1/ability/add
     * @param token 必选 string token
     * @param time 必选 string 时间
     * @param name 必选 string 能力名称
     * @param remark 必选 string 能力介绍
     * @return {"status":"200","message":"请求成功","data":"","page":null,"ext":null}
     * @return_param ability object 能力
     * @return_param grade object 等级
     * @return_param sys_account_info object 用户信息
     * @return_param subject object 单点系统中的用户信息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "add", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp add(HttpServletRequest request,
                     String token,
                     @RequestParam(name = "name", required = true) String name,
                     @RequestParam(name = "remark", required = true) String remark,
                     @RequestParam(name = "time", required = true)String time) {
        Map params = new HashMap();
        params.put("id", IDUtils.genHadId());
        params.put("user_id", getUserid(request));
        params.put("name", name);
        params.put("img_path", "http//xx.jpg");
        params.put("remark", remark);
        params.put("create_date", System.currentTimeMillis() / Constants.CONSTANT_1000);
        params.put("create_by", getUserid(request));
        abilityService.insertOne(params);
        return Resp.success();
    }

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 编辑能力
     * @description 编辑能力
     * @method post
     * @url /api/rest/v1.0.1/ability/edit
     * @param token 必选 string token
     * @param time 必选 string 时间
     * @param id 必选 string 能力主键
     * @param name 必选 string 能力名称
     * @param remark 必选 string 能力介绍
     * @return {"status":"200","message":"请求成功","data":{"subject":"{menu_base=[{isopen=1, code=system, pcode=0, num=1, icon=fa-user, sys_type=kakacl_zzf, url=#, tips=, pcodes=[0],, name=系统管理, id=1, ismenu=1, levels=1, status=1}], cas_base={create_by=kakacl_zzf, del_flag=0, create_time=1547006424, pass_word=, kaka_num=128643, phone_num=13800138000, id=1547006424247526, sys_type=kakacl_zzf, email=, status=52000}}","sys_account_info":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547006424,"user_name":"anonymous","roleid":"1","kaka_num":"128643","id_card":"2222","phone_num":"13800138000","id":"1547006424247526","account_status":52000,"introduction":"没有简介"},"grade":{"create_by":"1","del_flag":0,"create_time":1547008191,"user_id":"1547006424247526","grade":1,"id":"2","fraction":0},"ability":[{"create_by":"1","del_flag":0,"user_id":"1547006424247526","img_path":"http://www.baidu.com./1.png","name":"1","remark":"2","id":"1","create_date":1547008191,"order":2},{"create_by":"1","del_flag":0,"user_id":"1547006424247526","img_path":"http://www.baidu.com./1.png","name":"嗯厉害1","remark":"很厉害 很厉害1","id":"2","create_date":1547008191,"order":1}]},"page":null,"ext":null}
     * @return_param ability object 能力
     * @return_param grade object 等级
     * @return_param sys_account_info object 用户信息
     * @return_param subject object 单点系统中的用户信息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "edit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp edit(HttpServletRequest request,
                     String token,
                     @RequestParam(name = "id", required = true) String id,
                     @RequestParam(name = "name", required = true) String name,
                     @RequestParam(name = "remark", required = true) String remark,
                     @RequestParam(name = "time", required = true)String time) {
        Map params = new HashMap();
        params.put("id", id);
        params.put("name", name);
        params.put("remark", remark);
        abilityService.updateOne(params);
        return Resp.success();
    }

}
