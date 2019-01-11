package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.BaseController;
import com.kakacl.product_service.service.BackCardService;
import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.IDUtils;
import com.kakacl.product_service.utils.Resp;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 我的收入控制器
 * @date 2019-01-11
 */
@RestController
@RequestMapping("/api/rest/{version}/myincome")
public class MyInComeController extends BaseController {

    @Autowired
    private BackCardService backCardService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 添加银行卡的同步锁
    private final static String countKey="redis:lock:add_back_num";

    /**
     * showdoc
     * @catalog v1.0.1/我的收入
     * @title 我的收入-添加银行卡
     * @description 根据token添加银行卡
     * @method post
     * @url /api/rest/v1.0.1/myincome/addBackCard
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param cardNum 必选 string 银行卡号
     * @param name 必选 string 银行卡拥有者名称
     * @param phoneNum 必选 int 银行卡保留手机号
     * @param idCard 必选 string 银行卡拥有者身份证号码
     * @param opanCardBack 必选 string 开户行标志
     * @return {"status":"200","message":"请求成功","data":null,"page":null,"ext":null}
     * @return_param status string 状态
     * @return_param message string 消息
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(value = "addBackCard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp addBackCard(HttpServletRequest request,
                            @RequestParam(name = "time", required = true)String time,
                            String token,
                            @RequestParam(name = "cardNum", required = true) String cardNum,
                            @RequestParam(name = "name", required = true)String name,
                            @RequestParam(name = "phoneNum", required = true)String phoneNum,
                            @RequestParam(name = "idCard", required = true)String idCard,
                            @RequestParam(name = "opanCardBack", required = true)String opanCardBack) {
        Map params = new HashMap<>();
        String userId = getUserid(request);
        System.out.println(userId);

        // 只有当当前key不存在的时候，SETNX 会成功 – 此时相当于获取到可以对这个资源进行操作的同步锁
        final String key=String.format("redis:add_card_num:id:%s", userId +"");
        Boolean res = true;
        while(res){
            String value = UUID.randomUUID().toString()+System.nanoTime();
            res = stringRedisTemplate.opsForValue().setIfAbsent(key,value);
            if (res) {
                stringRedisTemplate.opsForValue().increment(countKey, 1L);
                try {
                    // 执行业务
                    res = false;
                    // 1.查询用户是否已经绑定过相同的银行卡,根据银行卡判断
                    params.put("card_num", cardNum);
                    List<Map>  result = backCardService.selectBackCarcdExist(params);
                    if(result != null && result.size() > Constants.CONSTANT_0) {
                        // 已经有绑定过
                        log.info("基于redis实战分布式锁-成功：stock={} 这个银行卡已经有账户绑定过 ",cardNum);
                        return Resp.fail(ErrorCode.CODE_6010);
                    } else {
                        // 继续执行
                        log.info("继续执行：stock={}  ",cardNum);

                        // 2.判断系统中，当前用户身份证号码是否一致。
                        params.clear();
                        params.put("id_card", idCard);
                        List<Map> userList = backCardService.selectUSerByIdcard(params);
                        if(userList.size() ==  Constants.CONSTANT_0) {
                            return Resp.fail(ErrorCode.CODE_451);
                        }

                        Map data = userList.get(Constants.CONSTANT_0);
                        log.info("继续执行 身份证号码验证通过：stock={}  ", data.get("id_card"));

                        // 3.判断银行卡和开户行是否一致

                        // 4.银行卡四要素验证
                        // 5.保存数据
                        params.clear();
                        params.put("id", IDUtils.genHadId());
                        params.put("user_id", userId);
                        params.put("back_type", opanCardBack);
                        params.put("phone_num", phoneNum);
                        params.put("card_num", cardNum);
                        params.put("id_card", idCard);
                        params.put("create_time", System.currentTimeMillis() / Constants.CONSTANT_1000);
                        params.put("create_by", userId);
                        boolean flag = backCardService.addCard(params);
                        if(flag) {
                            return Resp.success();
                        } else {
                            return Resp.fail(ErrorCode.CODE_6800);
                        }
                    }
                } catch (Exception e) {
                    return Resp.fail();
                } finally {
                    // 释放锁-释放当时自己获取到的锁-value
                    String redisValue = stringRedisTemplate.opsForValue().get(key);
                    if (StringUtils.isNotBlank(redisValue) && redisValue.equals(value)){
                        stringRedisTemplate.delete(key);
                    }
                }
            } else{
                res=true;
                try{
                    Thread.sleep(500L);
                } catch (Exception e) {}
            }
        }
        return Resp.fail(ErrorCode.CODE_9999);
    }

    /**
     * showdoc
     * @catalog v1.0.1/我的收入
     * @title 我的收入-获取我的收入列表
     * @description 根据token获取我的收入列表
     * @method post
     * @url /api/rest/v1.0.1/myincome/findInfo
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":{"data":[{"del_flag":0,"amount":5001.0,"user_id":"1547006424247526","income_type":1,"id":"1","detail_id":"1","speed":"10%","status":1}]},"page":null,"ext":null}
     * @return_param status string 状态
     * @return_param message string 消息
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(value = "findInfo", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findInfo(HttpServletRequest request, String token, String time) {
        Map params = new HashMap();
        Map result = new HashMap();
        params.put("user_id", getUserid(request));
        // 获取银行卡信息，如果有

        List<Map> data = backCardService.selectIncomeByUserid(params);
        result.put("data", data);

        return Resp.success(result);
    }


}
