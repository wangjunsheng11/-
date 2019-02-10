package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.PingCardService;
import com.kakacl.product_service.utils.IDUtils;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
     * @title 获取当前用户最后的的打卡类型
     * @description 当前用户上一次的打卡类型-用户可能忘记打卡的情况，用户可能会手动切换打卡类型-返回值-ping_type始终存在，如果用户没有打过卡，这里ping_type默认返回下班。
     * @method get
     * @url /api/rest/v1.0.1/pingcard/findLastPingCardType
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":{"create_by":"1547006424247526","del_flag":0,"create_time":1549252601,"user_id":"1547006424247526","ping_type":"上班","id":"1549252601622393"},"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param ping_type string 上一次打卡类型
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "findLastPingCardType", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findLastPingCardType(HttpServletRequest request,
                             @RequestParam(value = "time", required = true)String time,
                             String token, Map params) {
        // 如果没有上班，则为上班卡，有上班则为下班卡。
        params.put("user_id", getUserid(request));
        Map data = pingCardService.slectLastPingType(params);
        if(data == null) {
            data = new HashMap();
            data.put("ping_type", Constant.PING_TYPE_DOWN);
        }
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户打卡
     * @title 用户打卡
     * @description 当前用户打卡
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
     * @param ping_type 必选 string 当前打卡类型，上班OR下班
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
                     @RequestParam(value = "ping_type", required = true)String ping_type,
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
        params.put("ping_type", ping_type);
        boolean flag = pingCardService.insertPingCard(params);
        if(flag) {
            return Resp.success(params);
        } else {
            return Resp.fail(params);
        }
    }

}
