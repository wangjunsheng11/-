package com.kakacl.product_service.controller;

import com.kakacl.product_service.config.ConstantSMSMessage;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.service.AccountService;
import com.kakacl.product_service.service.CasAccountService;
import com.kakacl.product_service.utils.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/{version}/account")
@RefreshScope
public class AccountController {

    @Value("${sys-name}")
    private String sysName;

    @Value("${version}")
    private String version;

    @Value("${sms-account}")
    private String account;

    @Value("${sms-pwd}")
    private String pswd;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CasAccountService casAccountService;

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 获取用户信息根据token
     * @description 获取用户信息根据token
     * @method get
     * @url /api/v1.0.1/account/findInfo
     * @param token 必选 string token
     * @param time 必选 string 时间
     * @return {
     *     "status": "200",
     *     "message": "请求成功",
     *     "data": {"status":"200","message":"请求成功","data":{"subject":"{create_by=kakacl_zzf, del_flag=0, create_time=1547006424, pass_word=, kaka_num=128643, phone_num=13800138000, id=1547006424247526, sys_type=kakacl_zzf, email=, status=1}","sys_account_info":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547006424,"user_name":"anonymous","roleid":"0","kaka_num":"128643","id_card":"2222","phone_num":"13800138000","id":"1547006424247526","account_status":1,"introduction":"没有简介"},"grade":{"create_by":"1","del_flag":0,"create_time":1547008191,"user_id":"1547006424247526","grade":1,"id":"2","fraction":0},"ability":[{"create_by":"1","del_flag":0,"user_id":"1547006424247526","img_path":"http://www.baidu.com./1.png","name":"1","remark":"2","id":"1","create_date":1547008191,"order":2},{"create_by":"1","del_flag":0,"user_id":"1547006424247526","img_path":"http://www.baidu.com./1.png","name":"嗯厉害1","remark":"很厉害 很厉害1","id":"2","create_date":1547008191,"order":1}]},"page":null,"ext":null}
     * @return_param ability object 能力
     * @return_param grade object 等级
     * @return_param sys_account_info object 用户信息
     * @return_param subject object 单点系统中的用户信息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(value = "findInfo", method = RequestMethod.GET)
    public Resp findInfo(
            @RequestParam(name="token", required=true)String token,
            String time){
        java.util.Map result = new HashMap();
        try {
            Claims claims = JWTUtils.parseJWT(token);
            String id = claims.getId();
            String subject = claims.getSubject();
            String claimsIssuerr = claims.getIssuer();
            result.put("id", id);
            result.put("subject", subject);
            result.put("issuser", claimsIssuerr);
            return Resp.success(result);
        } catch (Exception e) {
            return Resp.fail(ErrorCode.UNLOGIN_ERROR);
        }
    }

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 根据手机号修改密码
     * @description 根据手机号修改密码
     * @method post
     * @url /api/v1.0.1/account/rePassByPhonenum
     * @param phone_num 必选 string 指定用户的手机号码
     * @param new_pass 必选 string 用户新设置的密码
     *  @param phoneCode 必选 string 用户输入的手机验证码
     * @return {"status":"200","message":"请求成功","data":null,"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @PostMapping(value = "rePassByPhonenum")
    public Resp rePassByPhonenum(
            @RequestParam(name="phone_num", required=true)String phone_num,
            @RequestParam(name="new_pass", required=true)String new_pass,
            @RequestParam(name="phoneCode", required=true)String phoneCode){
        java.util.Map params = new HashMap();
        params.put("phone_num", phone_num);
        params.put("pass_word", SymmetricEncoder.AESEncode(sysName, new_pass));
        int result = casAccountService.updateOnePassByPhonenum(params);
        if(result == Constants.CONSTANT_1) {
            return Resp.success();
        } else {
            return Resp.success();
        }
    }

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 用户修改自己的密码
     * @description 用户修改自己的密码
     * @method post
     * @url /api/v1.0.1/account/rePass
     * @param token 必选 string token
     * @param new_pass 必选 string 新密码
     * @param pass 必选 string 旧密码
     * @param time 必选 string 当前请求时间
     * @return {"status":"200","message":"请求成功","data":{"pass_word":"","id":"1547006424247526"},"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @PostMapping(value = "rePass")
    public Resp rePass(
            @RequestParam(name="token", required=true)String token,
            @RequestParam(name="new_pass", required=true)String new_pass,
            @RequestParam(name="pass", required=true)String pass,
            String time){
        java.util.Map params = new HashMap();
        try {
            Claims claims = JWTUtils.parseJWT(token);
            String id = claims.getId();
            params.put("id", id);
            params.put("pass_word",  SymmetricEncoder.AESEncode(sysName, new_pass));
            int flag = casAccountService.updateOnePassById(params);
            if(flag == Constants.CONSTANT_1) {
                params.put("pass_word",  "");
                return Resp.success(params);
            } else {
                return Resp.fail(ErrorCode.CODE_6800);
            }
        } catch (Exception e) {
            return Resp.fail(ErrorCode.UNLOGIN_ERROR);
        }
    }

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 根据主键查询账户基本信息
     * @description 根据主键查询账户基本信息
     * @method get
     * @url /api/v1.0.1/account/selectById
     * @param id 必选 string id
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547008191,"user_name":"anonymous","roleid":"0","kaka_num":"149386","id_card":"2222","phone_num":"13800138001","id":"1547008191643825","account_status":1,"introduction":"没有简介"},"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @GetMapping(value = "selectById")
    public Resp selectById(@RequestParam(name="id", required=true)String id, String token){
        java.util.Map params = new HashMap();
        java.util.Map result = new HashMap();
        params.put("user_id", id);
        Map userinfo = accountService.selectById(params);
        result.put("userinfo", userinfo);
        return Resp.success(result);
    }

    /*
     *
     * 查询账户分页数据
     * @author wangwei
     * @date 2019/1/7
      * @param
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("list")
    public Resp list(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "5")int pageSize){
        return Resp.success(accountService.selectByPageAndSelections(currentPage, pageSize));
    }
}
