package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.AccountService;
import com.kakacl.product_service.service.CasAccountService;
import com.kakacl.product_service.utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/{version}/account")
public class AccountController {

    @Value("${sys-name}")
    private String sysName;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CasAccountService casAccountService;

    /*
     *
     * 根据手机号码发送验证码
     * @author wangwei
     * @date 2019/1/8
      * @param phoneNum
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("sendPhoneCode")
    public Resp sendPhoneCode(@RequestParam(name="phoneNum", required=true) String phoneNum){
        java.util.Map params = new HashMap();
        accountService.selectById(params);
        String code = "666666";
        return Resp.success(code);
    }

    /*
     *
     * 用户注册
     * @author wangwei
     * @date 2019/1/8
      * @param phoneNum
     * @param idCode
     * @param phoneCode
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("register")
    public Resp register(
            @RequestParam(name="phoneNum", required=true)String phoneNum,
            @RequestParam(name="idCode", required=true)String idCode,
            @RequestParam(name="phoneCode", required=true)String phoneCode,
            @RequestParam(name="password", required=true)String password){
        Map<String, Object> params = new HashMap<>();
        Random randdata=new Random();
        int num = randdata.nextInt(90000) + 110000;
        // 如果咔咔号和手机号都没有注册过，则允许注册; 如果咔咔号存在，则再一次获取一个咔咔号
        params.clear();
        params.put("kaka_num", num);
        Map result = casAccountService.selectOne(params);
        if(result != null) {
            num = randdata.nextInt(90000) + 110000;
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
        params.put("pass_word", SymmetricEncoder.AESEncode(sysName, password));
        params.put("status", "1");
        params.put("del_flag", "0");
        params.put("create_time", System.currentTimeMillis()/1000);
        params.put("create_by", sysName);
        params.put("id_card", idCode);
        boolean flag = casAccountService.insert(params);
        if(flag) {
            return Resp.success(num);
        } else {
            return Resp.fail();
        }
    }

    @RequestMapping("login")
    public Resp login(
            @RequestParam(name="account", required=true)String account,
            @RequestParam(name="pass", required=true)String pass,
            String time){
            java.util.Map params = new HashMap();
        params.put("kaka_num", account);
        params.put("phone_num", account);
        params.put("pass_word", SymmetricEncoder.AESEncode(sysName, pass));
        Map result = casAccountService.selectOne(params);
        if(result != null) {
            result.put("pass_word", "");
            String jwt = JWTUtils.createJWT(result.get("id").toString(), result.get("phone_num").toString(),result.toString(), 1000 * 60 * 30);
            result.put("token", jwt);
            return Resp.success(result);
        } else {
            return Resp.fail(ErrorCode.CODE_450);
        }
    }

    /*
     *
     * 获取用户信息
     * @author wangwei
     * @date 2019/1/9
      * @param account
     * @param pass
     * @param time
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("findInfo")
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

    /*
     *
     * 用户自己修改密码
     * @author wangwei
     * @date 2019/1/9
      * @param token
     * @param new_pass
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("rePass")
    public Resp rePass(
            @RequestParam(name="token", required=true)String token,
            @RequestParam(name="new_pass", required=true)String new_pass,
            String time){
        java.util.Map params = new HashMap();
        try {
            Claims claims = JWTUtils.parseJWT(token);
            String id = claims.getId();
            params.put("id", id);
            params.put("pass_word",  SymmetricEncoder.AESEncode(sysName, new_pass));
            int flag = casAccountService.updateOnePassById(params);
            if(flag == 1) {
                params.put("pass_word",  "");
                return Resp.success(params);
            } else {
                return Resp.fail(ErrorCode.CODE_6800);
            }
        } catch (Exception e) {
            return Resp.fail(ErrorCode.UNLOGIN_ERROR);
        }
    }

    /*
     *
     * 根据主键查询账户基本信息
     * @author wangwei
     * @date 2019/1/7
      * @param id
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("selectById")
    public Resp selectById(@RequestParam(name="phoneNum", required=true)String id){
        java.util.Map params = new HashMap();
        params.put("id", id);
        return Resp.success(accountService.selectById(params));
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

/*class CallableThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int i = 0;
        for(;i<100;i++){
            System.out.println(Thread.currentThread().getName()+" "+i);
        }
        return i;
    }

    public static void main(String[] args) {
        CallableThread ctt = new CallableThread();
        FutureTask<Integer> ft = new FutureTask<>(ctt);
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " 的循环变量i的值" + i);
            if (i == 20) {
                new Thread(ft, "有返回值的线程").start();
            }
        }
        try {
            System.out.println("子线程的返回值：" + ft.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}*/
