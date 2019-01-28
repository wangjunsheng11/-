package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.utils.Resp;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description Redis缓存控制器
 * @date 2019-01-26
 */
@RestController
@RequestMapping("/api/rest/{version}/redissite")
public class RedisSiteController extends BaseController {

    // 设置允许的银行卡规则
    /**
     * showdoc
     * @catalog v1.0.1/银行卡
     * @title 银行卡-设置银行卡规则-暂未实现
     * @description 设置周周求职者允许的银行卡规则，根据规则设置允许的银行卡信息
     *
     * @method post
     * @url /api/rest/v1.0.1/mypage/backSite
     * @param token 必选 string token
     * @param time 必选 int 请求时间戳
     * @param dir 可选 String 目录
     * @param bank 可选 Map 银行卡类型-多类型用string
     * @return {"status":"200","message":"请求成功","data":[{"image":"http://192.168.4.170:8081/1545188000331-762166088940527181.png","orbit_id":"8","entry_time":1547006424,"company_name":"苏州富通精密机械有限公司","resignation_time":1547006424,"work_status":"52110"},{"image":"http://192.168.4.170:8081/1545188000331-762166088940527181.png","orbit_id":"9","entry_time":1547006424,"company_name":"航天信息","resignation_time":1547006424,"work_status":"52100"}],"page":null,"ext":null}
     * @return_param data Object 职业轨迹数据
     * @return_param status string 状态
     * @return_param image String 图片地址
     * @return_param orbit_id String 当前这一求职纪录主键
     * @return_param entry_time String 日志时间-精确到秒
     * @return_param company_name String 企业名称
     * @return_param resignation_time String 离职日期
     * @return_param work_status String 当前这一职位状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "backSite", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp backSite(String token, String time, String dir, Map bank) {
        return Resp.success();
    }
}
