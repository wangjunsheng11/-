package com.kakacl.product_service.controller.open.rest;

import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.HttpClient;
import com.kakacl.product_service.utils.Resp;
import com.kakacl.product_service.utils.SignUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    @AccessLimit(limit = Constants.CONSTANT_1,sec = Constants.CONSTANT_1)
    @GetMapping(value = "send", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @AccessLimit(limit = Constants.CONSTANT_1,sec = Constants.CONSTANT_1)
    @GetMapping(value = "checkSign", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp checkSign(HttpServletRequest request, String time, String sign){
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
            Map data = verify(map);
            map.put("sign", data.toString());
            map.put("params", map.toString());
            return Resp.fail(map);
        }
    }

    // 对外的密钥
    public String secretkey = Constant.SIGN_SECRETKEY;

    /** 加密密钥 */
    public String appkey = Constant.SIGN_APPKEY;

    // 间隔时间
    public int timeout = 1 * 30 * 1000;

    public Map verify(Map<String, String> params) {
        Map result = new HashMap();
        Logger logger = LoggerFactory.getLogger(this.getClass());
        String sign = "";
        if (params.get("sign") != null) {
            sign = params.get("sign");
        }else {
            logger.info("sign is null");
            result.put("sign", "sign is null");
            return result;
        }
        String timestamp = "";
        if (params.get("time") != null) {
            timestamp = params.get("time");
        }else {
            result.put("time", "time is null");
            return result;
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
                result.put("time", "api is time out " + curr);
                result.put("sysTime", curr);
                result.put("sysSign", mysign);
                return result;
            }
            result.put("time", "sign success .");
            result.put("sysSign", mysign);
            return result;
        } else {
            result.put("sysSign", mysign);
            result.put("sysSign_tips", "sign error .");
            return result;
        }
    }

}

