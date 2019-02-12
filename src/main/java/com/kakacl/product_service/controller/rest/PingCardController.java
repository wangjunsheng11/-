package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.PingCardService;
import com.kakacl.product_service.utils.ErrorCode;
import com.kakacl.product_service.utils.IDUtils;
import com.kakacl.product_service.utils.LatLonUtil;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.*;

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
     * @title 公司打卡规则设置,GCJ-02标准
     * @description 根据公司这只公司打卡规则，设置公司的坐标点;一个公司可以设置多个坐标点，可能公司有分公司，子公司，办事处等。
     * @method post
     * @url /api/rest/v1.0.1/pingcard/pingCardScopeRule
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param company_id 必选 string 公司主键
     * @param longitude 必选 string 经度
     * @param latitude 必选 string 纬度
     * @param effective_radius 可选 int 有效半，默认0
     * @param order 可选 int 顺序，默认99
     * @return
     * @return_param {"status":"200","message":"请求成功","data":{"id":"1549945941237165","company_id":"2","order":1,"longitude":"30.40628917093897","latitude":"118.51529848521174"},"page":null,"ext":null}
     * @return_param ping_type string 上一次打卡类型
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "pingCardScopeRule", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp pingCardScopeRule(HttpServletRequest request,
                                  @RequestParam(value = "time", required = true)String time,
                                  String token,
                                  @RequestParam(value = "company_id", required = true)String company_id,
                                  @RequestParam(value = "longitude", required = true)String longitude,
                                  @RequestParam(value = "latitude", required = true)String latitude,
                                  @RequestParam(value = "effective_radius", required = true, defaultValue = "0")String effective_radius,
                                  @RequestParam(value = "order", required = true, defaultValue = "99")int order,
                                  Map params) {
        params.put("id", IDUtils.genHadId());
        params.put("company_id", company_id);
        params.put("order", order);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("effective_radius", effective_radius);
        boolean flag = pingCardService.insertPingCardScopeRule(params);
        if(flag) {
            return Resp.success(params);
        } else {
            return Resp.fail(params);
        }
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户打卡
     * @title 判断用户当前是否允许打卡,GCJ-02标准
     * @description 根据当前用户坐标和公司，判断用户是否允许打卡，打卡前需要先调用此方法，否则可能打卡不成功，此方法返回成功前，打卡按钮无效。
     * @method get
     * @url /api/rest/v1.0.1/pingcard/pingCardValidate
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param company_id 必选 string 公司主键
     * @param longitude 必选 string 经度
     * @param latitude 必选 string 纬度
     * @return
     * @return_param {"status":"200","message":"请求成功","data":null,"page":null,"ext":null}
     * @return_param ping_type string 上一次打卡类型
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "pingCardValidate", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp pingCardValidate(HttpServletRequest request,
                                 @RequestParam(value = "time", required = true)String time,
                                 String token,
                                 @RequestParam(value = "company_id", required = true)String company_id,
                                 @RequestParam(value = "longitude", required = true)String longitude,
                                 @RequestParam(value = "latitude", required = true)String latitude,
                                 Map params) {
        params.put("company_id", company_id);
        List<Map> data = pingCardService.selectCompanyLocation(params);
        for (int i = Constants.CONSTANT_0; i < data.size(); i++) {
            double e_longitude = Double.valueOf(data.get(i).get("longitude").toString());
            double e_latitude = Double.valueOf(data.get(i).get("latitude").toString());
            double[] data_scope = LatLonUtil.GetAround(e_longitude, e_latitude, Integer.parseInt(data.get(i).get("effective_radius").toString()));
            double lo_1 = data_scope[Constants.CONSTANT_0];
            double la_1 = data_scope[Constants.CONSTANT_1];
            double lo_2 = data_scope[Constants.CONSTANT_2];
            double la_2 = data_scope[Constants.CONSTANT_3];
            if(lo_1 < Double.valueOf(longitude) && lo_2 > Double.valueOf(longitude)
                    && la_1 < Double.valueOf(latitude) && la_2 > Double.valueOf(latitude)) {
                return Resp.success();
            }
        }
        return Resp.fail(ErrorCode.CODE_460);
    }

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
