package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.PingCardService;
import com.kakacl.product_service.utils.IDUtils;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wangwei<br />
 * @Description: 打卡控制器<br/>
 * @date 2019/2/4 11:06<br/>
 * ${TAGS}
 */
@RestController
@RequestMapping("/api/rest/{version}/pingcard")
public class PingCardController extends BaseController {

    @Autowired
    private PingCardService pingCardService;

    /**
     * showdoc
     * @catalog v1.0.1/用户打卡
     * @title 打卡
     * @description 打卡
     * @method post
     * @url /api/rest/v1.0.1/pingcard/ping
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param user_id 必选 string 打卡用户主键
     * @param ping_time 必选 string 打卡时间戳
     * @param longitude 必选 string 经度
     * @param latitude 必选 string 纬度
     * @param company_id 必选 string 归属公司主键
     * @param company_name 必选 string 归属公司名
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "ping", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp ping(HttpServletRequest request,
                     @RequestParam(value = "time", required = true)String time,
                     String token,
                     @RequestParam(value = "user_id", required = true)String user_id,
                     @RequestParam(value = "ping_time", required = true)String ping_time,
                     @RequestParam(value = "longitude", required = true)String longitude,
                     @RequestParam(value = "latitude", required = true)String latitude,
                     @RequestParam(value = "company_id", required = true)String company_id,
                     @RequestParam(value = "company_name", required = true)String company_name,
                     Map params
                     ) {
        params.put("user_id", user_id);
        params.put("ping_time", ping_time);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("company_id", company_id);
        params.put("company_name", company_name);
        params.put("id", IDUtils.genHadId());
        params.put("create_time", System.currentTimeMillis() / Constants.CONSTANT_1000);
        params.put("create_by", getUserid(request));
        boolean flag = pingCardService.insertPingCard(params);
        if(flag) {
            return Resp.success(params);
        } else {
            return Resp.fail(params);
        }
    }

}
