package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.BackCardService;
import com.kakacl.product_service.utils.IDUtils;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 银行信息控制器
 * @date 2019-02-10
 */
@RestController
@RequestMapping("/api/rest/{version}/bank")
public class BankController extends BaseController {

    @Autowired
    private BackCardService bankCardService;

    /**
     * showdoc
     * @catalog v1.0.1/银行卡
     * @title 获取银行卡规则
     * @description 获取允许的银行卡
     * @method get
     * @url /api/rest/v1.0.1/bank/findBankRule
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":[{"del_flag":"0","bank_type":"国有银行","bank_name":"中国银行","sign_path":"https://ss3.bdstatic.com/yrwDcj7w0QhBkMak8IuT_XF5ehU5bvGh7c50/logopic/6166e0d3da394f8ef379fef5f810c2e8_fullsize.jpg@s_1,w_484,h_484","bank_sign":"BOC","id":"1","card_type":"DC","order":1},{"del_flag":"0","bank_type":"国有银行","bank_name":"中国工商银行","sign_path":"http://img3.imgtn.bdimg.com/it/u=3075259673,2777913422&fm=26&gp=0.jpg","bank_sign":"ICBC","id":"3","card_type":"DC","order":3}],"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "findBankRule", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findBankRule(
            HttpServletRequest request, String token,
            @RequestParam(name="time",required=true)String time){
        List<Map> data = bankCardService.selectBankRule(null);
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/银行卡
     * @title 设置银行卡规则
     * @description 设置银行卡规则
     * @method post
     * @url /api/rest/v1.0.1/bank/setBankRule
     * @param token 必选 string token
     * @param bank_name 必选 string 银行名称-中国银行
     * @param bank_type 必选 string 银行类型-国有银行
     * @param card_type 必选 string 卡类型-DC
     * @param bank_sign 必选 string 银行标志-BOC
     * @param sign_path 必选 string 标志地址
     * @param order 必选 int 顺序
     * @param time 必选 string 请求时间戳
     * @return {"status":"200","message":"请求成功","data":[{"del_flag":"0","bank_type":"国有银行","bank_name":"中国银行","sign_path":"https://ss3.bdstatic.com/yrwDcj7w0QhBkMak8IuT_XF5ehU5bvGh7c50/logopic/6166e0d3da394f8ef379fef5f810c2e8_fullsize.jpg@s_1,w_484,h_484","bank_sign":"BOC","id":"1","card_type":"DC","order":1},{"del_flag":"0","bank_type":"国有银行","bank_name":"中国工商银行","sign_path":"http://img3.imgtn.bdimg.com/it/u=3075259673,2777913422&fm=26&gp=0.jpg","bank_sign":"ICBC","id":"3","card_type":"DC","order":3}],"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "setBankRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp setBankRule(
            HttpServletRequest request, String token,
            @RequestParam(name="bank_name",required=true)String bank_name,
            @RequestParam(name="bank_type",required=true)String bank_type,
            @RequestParam(name="card_type",required=true)String card_type,
            @RequestParam(name="bank_sign",required=true)String bank_sign,
            @RequestParam(name="sign_path",required=true)String sign_path,
            @RequestParam(name="order",required=true)int order,
            @RequestParam(name="time",required=true)String time,
            Map params){
        params.put("id", IDUtils.genHadId());
        params.put("bank_name", bank_name);
        params.put("bank_type", bank_type);
        params.put("card_type", card_type);
        params.put("bank_sign", bank_sign);
        params.put("sign_path", sign_path);
        params.put("order", order);
        boolean flag = bankCardService.insertBankRule(params);
        if(flag){
            return Resp.success(params);
        } else {
            return Resp.fail(params);
        }
    }
}
