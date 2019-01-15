package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.GradeService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;

/*
 *
 * 等级控制器
 * @author wangwei
 * @date 2019/1/9
  * @param null
 * @return
 */
@RestController
@RequestMapping("/api/{version}/grade")
public class GradeController {
    @Autowired
    private GradeService gradeService;

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
     * @url /api/v1.0.1/grade/selectById
     * @param user_id 必选 string 用户主键
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547008191,"user_name":"anonymous","roleid":"0","kaka_num":"149386","id_card":"2222","phone_num":"13800138001","id":"1547008191643825","account_status":1,"introduction":"没有简介"},"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping("selectById")
    public Object selectById(@RequestParam String user_id, String time){
        java.util.Map params = new HashMap();
        params.put("user_id", user_id);
        return Resp.success(gradeService.selectById(params));
    }
}
