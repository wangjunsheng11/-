package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.TalentService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 天赋控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/{version}/talent")
@RefreshScope
public class TalentController extends BaseController {

    @Autowired
    private TalentService talentService;

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
        List<Map> result = talentService.selectList(params);
        return Resp.success(result);
    }

}
