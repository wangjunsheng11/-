package com.kakacl.product_service.controller;

import com.kakacl.product_service.domain.Ability;
import com.kakacl.product_service.service.AbilityService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 能力控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/{version}/ability")
public class AbilityController extends  BaseController {

    @Autowired
    private AbilityService abilityService;

    /*
     *
     * 当前用户查询能力
     * @author wangwei
     * @date 2019/1/9
      * @param request
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("findOne")
    public Resp findOne(HttpServletRequest request) {
        Map params = new HashMap();
        params.put("user_id", getUserid(request));
        List<Map> data = abilityService.selectByUserid(params);
        return Resp.success(data);
    }

    /*
     *
     * 根据能力id编辑某一个能力
     * @author wangwei
     * @date 2019/1/9
      * @param request
     * @param id
     * @param name
     * @param remark
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("edit")
    public Resp edit(HttpServletRequest request,
                     @RequestParam String id,
                     @RequestParam String name,
                     @RequestParam String remark) {
        Map params = new HashMap();
        params.put("id", id);
        params.put("name", name);
        params.put("remark", remark);
        abilityService.updateOne(params);
        return Resp.success();
    }

}
