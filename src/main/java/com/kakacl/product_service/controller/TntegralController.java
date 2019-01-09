package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.TntegralService;
import com.kakacl.product_service.utils.JWTUtils;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 用户积分控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/{version}/tntegral")
@RefreshScope
public class TntegralController  extends  BaseController {

    @Autowired
    private TntegralService tntegralService;

    /*
     *
     * 获取当前用户积分
     * @author wangwei
     * @date 2019/1/9
      * @param request
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("findList")
    public Resp findList(HttpServletRequest request) {
        Map params = new HashMap();
        params.put("user_id", getUserid(request));
        Map result = tntegralService.selectOneByUserid(params);
        return Resp.success(result);
    }

}
