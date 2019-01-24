package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.AccountService;
import com.kakacl.product_service.service.CasAccountService;
import com.kakacl.product_service.utils.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/rest/{version}/account")
@RefreshScope
public class AccountController extends BaseController {

    @Value("${sys-name}")
    private String sysName;

    @Value("${version}")
    private String version;

    @Value("${sms-account}")
    private String account;

    @Value("${sms-pwd}")
    private String pswd;

    @Value("${account-paaakey}")
    private String account_paaakey;

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
     * @url /api/rest/v1.0.1/account/findInfo
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
    @RequestMapping(value = "findInfo", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findInfo(
            @RequestParam(name="token", required=true)String token,
            String time){
        Map result = new HashMap();
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
     * @url /api/rest/v1.0.1/account/rePassByPhonenum
     * @param phone_num 必选 string 指定用户的手机号码
     * @param new_pass 必选 string 用户新设置的密码
     *  @param phoneCode 必选 string 用户输入的手机验证码
     * @return {"status":"200","message":"请求成功","data":null,"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "rePassByPhonenum", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp rePassByPhonenum(
            @RequestParam(name="phone_num", required=true)String phone_num,
            @RequestParam(name="new_pass", required=true)String new_pass,
            @RequestParam(name="phoneCode", required=true)String phoneCode){
        Map params = new HashMap();
        params.put("phone_num", phone_num);
        try {
            params.put("pass_word", SecurityUtil.encrypt(new_pass, account_paaakey));
        } catch (Exception e) {
            log.info("${}", e.getMessage());
        }
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
     * @url /api/rest/v1.0.1/account/rePass
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
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "rePass", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp rePass(
            @RequestParam(name="token", required=true)String token,
            @RequestParam(name="new_pass", required=true)String new_pass,
            @RequestParam(name="pass", required=true)String pass,
            String time){
        Map params = new HashMap();
        try {
            Claims claims = JWTUtils.parseJWT(token);
            String id = claims.getId();
            params.put("id", id);
            try {
                params.put("pass_word", SecurityUtil.encrypt(new_pass, account_paaakey));
            } catch (Exception e) {
                log.info("${}", e.getMessage());
            }
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
     * @url /api/rest/v1.0.1/account/selectById
     * @param user_id 必选 string 用户主键
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":{"userinfo":{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547028105,"user_name":"anonymous","roleid":"2","kaka_num":"171330","id_card":"222223","phone_num":"13800138002","id":"1547028105160700","account_status":52000,"introduction":"没有简介"}},"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "selectById", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp selectById(@RequestParam(name="user_id", required=true)String user_id, String token){
        Map params = new HashMap();
        Map result = new HashMap();
        params.put("user_id", user_id);
        Map userinfo = accountService.selectById(params);
        result.put("userinfo", userinfo);
        return Resp.success(result);
    }

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 查询账户分页数据
     * @description 查询账户分页数据
     * @method get
     * @url /api/rest/v1.0.1/account/list
     * @param currentPage 可选 int 当前页-默认1
     * @param pageSize 可选 int 每页条数-默认5
     * @return {"status":"200","message":"请求成功","data":{"pageNum":1,"pageSize":5,"size":3,"startRow":1,"endRow":3,"total":3,"pages":1,"list":[{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547028105,"user_name":"anonymous","roleid":"2","kaka_num":"171330","id_card":"222223","phone_num":"13800138002","id":"1547028105160700","account_status":52000,"introduction":"没有简介"},{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547008191,"user_name":"anonymous","roleid":"2","kaka_num":"149386","id_card":"22221","phone_num":"13800138001","id":"1547008191643825","account_status":52000,"introduction":"没有简介"},{"create_by":"1","hear_path":"","del_flag":0,"create_time":1547006424,"user_name":"anonymous","roleid":"1","kaka_num":"128643","id_card":"2222","phone_num":"13800138000","id":"1547006424247526","account_status":52000,"introduction":"没有简介"}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"firstPage":1,"lastPage":1},"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp list(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "5")int pageSize){
        return Resp.success(accountService.selectByPageAndSelections(currentPage, pageSize));
    }
}
