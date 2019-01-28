package com.kakacl.product_service.controller.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.BackCardService;
import com.kakacl.product_service.utils.BackUtils;
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
     * @param openCardBack 必选 string 开户行标志-如ICBC\ABC
     * @return {"status":"200","message":"请求成功","data":null,"page":null,"ext":null}
     * @return_param status string 状态
     * @return_param message string 消息
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "addBackCard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp addBackCard(HttpServletRequest request,
                            @RequestParam(name = "time", required = true)String time,
                            String token,
                            @RequestParam(name = "cardNum", required = true) String cardNum,
                            @RequestParam(name = "name", required = true)String name,
                            @RequestParam(name = "phoneNum", required = true)String phoneNum,
                            @RequestParam(name = "idCard", required = true)String idCard,
                            @RequestParam(name = "openCardBack", required = true)String openCardBack) {
        Map params = new HashMap<>();
        String userId = getUserid(request);

        // 只有当当前key不存在的时候，SETNX 会成功 – 此时相当于获取到可以对这个资源进行操作的同步锁
        final String key = String.format("redis:add_card_num:id:%s", userId +"");
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
                        log.info("lock：stock={} 这个银行卡已经有账户绑定过 ",cardNum);
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
                        String backResult = BackUtils.getCardDetail(cardNum);
                        log.info("银行卡接口返回值 ： %s", backResult);
                        JSONObject obj = JSON.parseObject(backResult);
                        if(obj != null && obj.get("validated").toString().equals("true")) {
                            if(obj.get("cardType").toString().toLowerCase().contains("dc")) {
                                String bank = obj.get("bank").toString(); // CCB
                                if(!bank.equals(openCardBack)) {
                                    return Resp.fail(ErrorCode.CODE_454);
                                }
                                String cardType = obj.get("cardType").toString(); // DC 储蓄卡； CC 信用卡, 贷记卡 ；
                                String validated = obj.get("validated").toString(); // true
                                String stat = obj.get("stat").toString(); // ok
                                String.format(" bank: %s ,  ", bank);
                                // System.out.println(String.format(" bank: %s , cardType %s ,  validated %s , stat %s ", bank, cardType, validated, stat));
                                log.info("bank {} , cardType {} , validated {} , stat {}  ", bank, cardType, validated, stat);
                            } else {
                                return Resp.fail(ErrorCode.CODE_452);
                            }
                        } else {
                            return Resp.fail(ErrorCode.CODE_453);
                        }
                        // 4.银行卡四要素验证

                        // 5.保存数据
                        params.clear();
                        params.put("id", IDUtils.genHadId());
                        params.put("user_id", userId);
                        params.put("back_type", openCardBack);
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
     * @method get
     * @url /api/rest/v1.0.1/myincome/findInfo
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":{"back_data":[{"card_num":612541,"create_by":"1547006424247526","del_flag":0,"create_time":1547184695,"user_id":"1547006424247526","back_type":"2","id_card":"2222","phone_num":"135","id":"1547184663241923"},{"card_num":612541222,"create_by":"1547006424247526","del_flag":0,"create_time":1547184770,"user_id":"1547006424247526","back_type":"2","id_card":"2222","phone_num":"135","id":"1547184770627902"}],"income_data":[{"del_flag":0,"amount":5001.0,"user_id":"1547006424247526","income_type":1,"id":"1","detail_id":"1","speed":"10%","status":1}]},"page":null,"ext":null}
     * @return_param status string 状态
     * @return_param message string 消息
     * @return_param back_data Object 银行卡对象数据集合
     * @return_param income_data string 收入数据集合
     * @return_param card_num string 银行卡卡号
     * @return_param back_type string 银行标志
     * @return_param amount string 收入
     * @return_param income_type string 收入类型
     * @return_param speed string 进度
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findInfo", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findInfo(HttpServletRequest request, String token, String time) {
        Map params = new HashMap();
        Map result = new HashMap();
        params.put("user_id", getUserid(request));
        // 获取银行卡信息，如果有

        List<Map> back_data = backCardService.selectList(params);
        result.put("back_data", back_data);
        List<Map> income_data = backCardService.selectIncomeByUserid(params);
        result.put("income_data", income_data);

        return Resp.success(result);
    }

    /**
     * showdoc
     * @catalog v1.0.1/我的收入
     * @title 我的收入-根据卡号删除银行卡
     * @description 根据token和卡号删除银行卡
     * @method get
     * @url /api/rest/v1.0.1/myincome/delBackcard
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param  backCardNum 必选 string 银行卡卡号
     * @return {"status":"200","message":"请求成功","data":null,"page":null,"ext":null}
     * @return_param status string 状态
     * @return_param message string 消息
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "delBackcard", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp delBackcard(HttpServletRequest request, String token, String time,
                            @RequestParam(name = "backCardNum", required = true) String backCardNum) {
        Map params = new HashMap();
        params.put("user_id", getUserid(request));
        params.put("card_num", backCardNum);
        params.put("del_flag", "1");
        boolean flag = backCardService.updateByUserIdAndBackcardNum(params);
        if(flag) {
            return Resp.success();
        } else {
            return Resp.fail(ErrorCode.CODE_6800);
        }
    }

    /**
     * showdoc
     * @catalog v1.0.1/我的收入
     * @title 我的收入-根据收入详情主键获取详情
     * @description 根据收入详情主键获取详情
     * @method get
     * @url /api/rest/v1.0.1/myincome/findDetail
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param  id 必选 string 主键
     * @return {"status":"200","message":"请求成功","data":{"start_time":151515,"del_flag":"0","amount":5001.0,"condition":"打卡50天","reference_ent_time":165656,"id":"1","type":"1","status":53003},"page":null,"ext":null}
     * @return_param status string 状态
     * @return_param message string 消息
     * @return_param start_time string 开始时间-秒
     * @return_param amount string 金额
     * @return_param condition string 条件
     * @return_param reference_ent_time string 参考结束时间
     * @return_param status string 当前补贴状态
     * @return_param type string 当前补贴类型
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findDetail", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findDetail(HttpServletRequest request, String token, String time,
                           @RequestParam(name = "id", required = true) String id) {
        Map params = new HashMap();
        params.put("id", id);
        Map data = backCardService.selectIncomeDetail(params);
        return Resp.success(data);
    }

}
