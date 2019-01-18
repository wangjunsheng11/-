package com.kakacl.product_service.controller.open.rest;

import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.controller.BaseController;
import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.HttpClient;
import com.kakacl.product_service.utils.Resp;
import com.kakacl.product_service.utils.SignUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 测试签名用
 * @date 2019-01-12
 */
@RestController
@RequestMapping("/api/open/rest/{version}/signtest")
public class SignTestController extends BaseController {

    /**
     * 模拟客户端请求API接口
     * @param request
     * @return
     */
    @RequestMapping("send")
    public String send(HttpServletRequest request){
        Map<String,String> param = new HashMap<>();
        param.put("userId","9527");
        param.put("amount","9.99");
        param.put("productId","9885544154");
        param.put("secretKey","mysecret123456");
        try {
            String postResult = HttpClient.get("http://127.0.0.1:8773/api/open/rest/v1.0.1/ad/list", SignUtil.getInstance().sign(param));
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * showdoc
     * @catalog v1.0.1/测试相关
     * @title 模拟服务的API接口
     * @description 模拟服务的API接口
     * @method post
     * @url /api/open/rest/v1.0.1/signtest/checkSign
     * @param secretKey 必选 string 秘钥
     * @param time 必选 string 请求时间戳
     * @param sign 必选 string 参数MD5签名
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping("checkSign")
    public Resp checkSign(HttpServletRequest request){
        //从request中获取参数列表，转成map
        Map<String, String> map = SignUtil.toVerifyMap(request.getParameterMap(),false);
        String secretKey =  map.get("secretKey");
        if (StringUtils.isEmpty(secretKey) || !map.get("secretKey").equals(SignUtil.getInstance().secretkey)){
            System.out.println("secretKey is err");
            return Resp.fail(ErrorCode.CODE_431);
        }
        if (SignUtil.getInstance().verify(map)){
            return Resp.success(this.verify(map));
        }else {
            return Resp.fail(ErrorCode.CODE_430);
        }
    }

    // 对外的密钥
    public String secretkey = Constant.SIGN_SECRETKEY;

    /** 加密密钥 */
    public String appkey = Constant.SIGN_APPKEY;

    // 间隔时间
    public int timeout = 1 * 30 * 1000;

    public String verify(Map<String, String> params) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        String sign = "";
        if (params.get("sign") != null) {
            sign = params.get("sign");
        }else {
            logger.info("sign is null");
            return "";
        }
        String timestamp = "";
        if (params.get("time") != null) {
            timestamp = params.get("time");
        }else {
            return "";
        }
        //过滤空值、sign
        Map<String, String> sParaNew = SignUtil.paraFilter(params);
        //获取待签名字符串
        String preSignStr = SignUtil.createLinkString(sParaNew);
        //获得签名验证结果
        String mysign = DigestUtils.md5Hex(SignUtil.getContentBytes(preSignStr + appkey, "UTF-8"));
        logger.info("mysign {}", mysign);
        if (mysign.equals(sign)) {
            //是否超时
            long curr = System.currentTimeMillis();
            if ((curr - Long.valueOf(timestamp)) > timeout){
                logger.info("api is time out " + curr);
                return "";
            }
            return mysign;
        } else {
            return "";
        }
    }

}

