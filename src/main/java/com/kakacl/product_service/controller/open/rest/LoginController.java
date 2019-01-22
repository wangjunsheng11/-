package com.kakacl.product_service.controller.open.rest;

import com.kakacl.product_service.config.ConstantSMSMessage;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.AccountService;
import com.kakacl.product_service.service.CasAccountService;
import com.kakacl.product_service.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 登录控制器
 * @date 2019-01-11
 */
@RestController
@RequestMapping("/api/open/rest/{version}/do")
@RefreshScope
public class LoginController extends BaseController {

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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final static String countKey = "redis:lock:test";

    private final static String every_login_content = "redis:every_login_area:container";

    @AccessLimit(limit = Constants.CONSTANT_1,sec = Constants.CONSTANT_1)
    @RequestMapping(value = "test", name = "test")
    public Resp test() {
        final String key = String.format("redis:test:id:%s", "a" +"");
        Boolean res = true;
        while(res){
            String value = UUID.randomUUID().toString()+System.nanoTime();
            res = stringRedisTemplate.opsForValue().setIfAbsent(key,value);
            if (res) {
                stringRedisTemplate.opsForValue().increment(countKey, 1L);
                try {
                    // 执行业务
                    res = false;
                } catch (Exception e) {

                } finally {
                    // 释放锁-释放当时自己获取到的锁-value
                    String redisValue = stringRedisTemplate.opsForValue().get(key);
                    if (StringUtils.isNotBlank(redisValue) && redisValue.equals(value)){
                        stringRedisTemplate.delete(key);
                    }
                }
            } else {
                res=true;
                try{
                    Thread.sleep(500L);
                } catch (Exception e) {}
            }
        }
        return Resp.success();
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户相关
     * @title 根据手机号码发送验证码
     * @description 手机号码发送验证码的接口
     * @method post
     * @url /api/open/rest/v1.0.1/do/sendPhoneCode
     * @param phoneNum 必选 string 手机号码
     * @param type 必选 string 发送类型register默认为注册(register)-找回密码(refindpass)-绑定非法银行卡(IllegalBindingBackCard)
     * @return {"status":"200","message":"请求成功","data":171330,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_1,sec = Constants.CONSTANT_1)
    @RequestMapping(value = "sendPhoneCode", method = RequestMethod.POST)
    public Resp sendPhoneCode(
            @RequestParam(name="phoneNum", required=true) String phoneNum,
            @RequestParam(name="type", required=false, defaultValue = "register") String type){
        String msg_model = "";
        if(type.equals("register")) {
            msg_model = ConstantSMSMessage.CONSTANT_REGIATER;
        } else if(type.equals("refindpass")){
            msg_model = ConstantSMSMessage.CONSTANT_REPASSWORD;
        } else if(type.equals("IllegalBindingBackCard")){
            msg_model = ConstantSMSMessage.CONSTANT_ACCOUNT_ILLEGAL_BIND_BACK_NUM;
        }
        java.util.Map params = new HashMap();
        params.put("phone_num", phoneNum);
        Map result = accountService.selectByPhone(params);
        if(result != null) {
            // 手机号码已经注册过
            return Resp.fail(ErrorCode.CODE_6001);
        }
        String code = NumberUtils.getRandomNumber(Constants.CONSTANT_100000, Constants.CONSTANT_999999) + "";
        List sms_params = new ArrayList();
        sms_params.add(phoneNum);
        sms_params.add(phoneNum);
        sms_params.add(code);
        sms_params.add(Constants.CONSTANT_10);
        new HttpSendSMSUtils().SendVariable(ConstantSMSMessage.SEND_URL, msg_model, sms_params, account, pswd);
        return Resp.success(code);
    }

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 用户注册
     * @description 用户注册
     * @method post
     * @url /api/open/rest/v1.0.1/do/register
     * @param phoneNum 必选 string 手机号码
     * @param idCode 必选 string 身份证号码
     * @param phoneCode 必选 string 手机验证码
     * @param password 必选 string 密码
     * @return {"status":"200","message":"请求成功","data":171330,"page":null,"ext":null}
     * @return_param data int 内容
     * @return_param status string 状态
     * @remark 用户注册data中仅返回用户的咔咔号。
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_1,sec = Constants.CONSTANT_1)
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Resp register(
            @RequestParam(name="phoneNum", required=true)String phoneNum,
            @RequestParam(name="idCode", required=true)String idCode,
            @RequestParam(name="phoneCode", required=true)String phoneCode,
            @RequestParam(name="password", required=true)String password){
        Map<String, Object> params = new HashMap<>();
        int num = NumberUtils.getRandomNumber(Constants.CONSTANT_110000, Constants.CONSTANT_200000);
        // 如果咔咔号和手机号都没有注册过，则允许注册; 如果咔咔号存在，则再一次获取一个咔咔号
        params.clear();
        params.put("kaka_num", num);
        Map result = casAccountService.selectOne(params);
        if(result != null) {
            num = NumberUtils.getRandomNumber(Constants.CONSTANT_110000, Constants.CONSTANT_200000);
            params.clear();
            params.put("kaka_num", num);
            if(casAccountService.selectOneByKakanum(params) != null) {
                return Resp.fail(ErrorCode.CODE_6000);
            }
        }
        params.clear();
        params.put("phone_num", phoneNum);
        result = casAccountService.selectOneByPhonenum(params);
        if(result!= null) {
            return Resp.fail(ErrorCode.CODE_6001);
        }
        params.put("id", IDUtils.genHadId());
        params.put("sys_type", sysName);
        params.put("kaka_num", num);
        params.put("phone_num", phoneNum);
        params.put("email", "");
        // linux上无法使用 保持一致
//        params.put("pass_word", SymmetricEncoder.AESEncode(sysName, password));
        try {
            params.put("pass_word", SecurityUtil.encrypt(password, account_paaakey));
        } catch (Exception e) {
            log.info("${}", e.getMessage());
        }
//        params.put("pass_word", password);
        params.put("status", Constants.CONSTANT_1);
        params.put("del_flag", Constants.CONSTANT_0);
        params.put("create_time", NumberUtils.getCurrentTimes());
        params.put("create_by", sysName);
        params.put("id_card", idCode);
        boolean flag = casAccountService.insert(params);
        if(flag) {
            return Resp.success(num);
        } else {
            return Resp.fail();
        }
    }

    /**
     * showdoc
     * @author wangwei
     * @date 2019/1/8
     *
     * @catalog v1.0.1/用户相关
     * @title 用户登录
     * @description 用户登录，用户登录成功后，或返回一个token，token在之后请求时，请传递次token在header中或者httpservletrquest中， *** 其他请求接口时，权限验证会更新token存在header中，每次请求请使用最新token，避免token中途失效。
     * @method post
     * @url /api/open/rest/v1.0.1/do/login
     * @param account 必选 string 手机号码或者咔咔号
     * @param pass 必选 string 密码
     * @return {"status":"200","message":"请求成功","data":{"menu_base":[{"isopen":1,"code":"system","pcode":"0","num":1,"icon":"fa-user","sys_type":"kakacl_zzf","url":"#","tips":"","pcodes":"[0],","name":"系统管理","id":"1","ismenu":1,"levels":1,"status":1}],"integral":{"message":"恭喜你，今日首次登陆，获取1积分。","fraction":1},"cas_base":{"create_by":"kakacl_zzf","del_flag":"0","create_time":1547006424,"pass_word":"","kaka_num":"128643","phone_num":"13800138000","id":"1547006424247526","sys_type":"kakacl_zzf","email":"","status":"52000"},"token":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNTQ3MDA2NDI0MjQ3NTI2IiwiaWF0IjoxNTQ3NzkwMjE4LCJzdWIiOiJ7bWVudV9iYXNlPVt7aXNvcGVuPTEsIGNvZGU9c3lzdGVtLCBwY29kZT0wLCBudW09MSwgaWNvbj1mYS11c2VyLCBzeXNfdHlwZT1rYWthY2xfenpmLCB1cmw9IywgdGlwcz0sIHBjb2Rlcz1bMF0sLCBuYW1lPeezu-e7n-euoeeQhiwgaWQ9MSwgaXNtZW51PTEsIGxldmVscz0xLCBzdGF0dXM9MX1dLCBjYXNfYmFzZT17Y3JlYXRlX2J5PWtha2FjbF96emYsIGRlbF9mbGFnPTAsIGNyZWF0ZV90aW1lPTE1NDcwMDY0MjQsIHBhc3Nfd29yZD0sIGtha2FfbnVtPTEyODY0MywgcGhvbmVfbnVtPTEzODAwMTM4MDAwLCBpZD0xNTQ3MDA2NDI0MjQ3NTI2LCBzeXNfdHlwZT1rYWthY2xfenpmLCBlbWFpbD0sIHN0YXR1cz01MjAwMH19IiwiaXNzIjoiMTM4MDAxMzgwMDAiLCJleHAiOjE1NDc3OTIwMTh9.L7NSguXJqMUIUpfdut7ilIwP930oW2xm9m8IbCafD0c"},"page":null,"ext":null}
     * @return_param token string token
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_1,sec = Constants.CONSTANT_1)
    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp login(
            @RequestParam(name="account", required=true)String account,
            @RequestParam(name="pass", required=true)String pass,
            String time){
        java.util.Map params = new HashMap();
        params.put("kaka_num", account);
        params.put("phone_num", account);
        // linux上无法使用 保持一致
//        params.put("pass_word", SymmetricEncoder.AESEncode(sysName, pass));
//        params.put("pass_word", pass);
        try {
            params.put("pass_word", SecurityUtil.encrypt(pass, account_paaakey));
        } catch (Exception e) {
            log.error("${}", e.getMessage());
        }
        Map result = casAccountService.selectOne(params);
        if(result != null) {
            Map cas_base = (Map)result.get("cas_base");
            cas_base.put("pass_word", "");
            String jwt = JWTUtils.createJWT(cas_base.get("id").toString(), cas_base.get("phone_num").toString(), result.toString(), Constants.CONSTANT_1000 * Constants.CONSTANT_60 * Constants.CONSTANT_30);
            result.put("token", jwt);
            // 首次登陆 redis 中查询用户是否在24小时中登录过 这里需要设置0点清除所有纪录
            String phone = stringRedisTemplate.opsForValue().get("enery_day_login" + cas_base.get("phone_num"));
            if(StringUtils.isBlank(phone)) {
                Map integral = new HashMap();
                int fraction = 1;
                integral.put("fraction", fraction);
                integral.put("message", String.format("恭喜你，今日首次登陆，获取%s积分。", fraction));
                result.put("integral", integral);
                stringRedisTemplate.opsForValue().set("enery_day_login" + cas_base.get("phone_num").toString(), cas_base.get("phone_num").toString(), Constants.CONSTANT_24, TimeUnit.HOURS);
            }
            return Resp.success(result);
        } else {
            return Resp.fail(ErrorCode.CODE_450);
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
     * @url /api/open/rest/v1.0.1/do/rePassByPhonenum
     * @param phone_num 必选 string 指定用户的手机号码
     * @param new_pass 必选 string 用户新设置的密码
     *  @param phoneCode 必选 string 用户输入的手机验证码
     * @return {"status":"200","message":"请求成功","data":null,"page":null,"ext":null}
     * @return_param data string data
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_1,sec = Constants.CONSTANT_1)
    @PostMapping(value = "rePassByPhonenum", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

}
