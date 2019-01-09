package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.GradeService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;

/*
 *
 * 等级控制器
 * @author wangwei
 * @date 2019/1/9
  * @param null
 * @return
 */
@RestController
@RequestMapping("/api/{version}/grade")
public class GradeController {
    @Autowired
    private GradeService gradeService;

    /**
     * 查询单个等级
     * @param user_id
     * @return
     */
    @RequestMapping("selectById")
    public Object selectById(@RequestParam String user_id){
        java.util.Map params = new HashMap();
        params.put("user_id", user_id);
        return Resp.success(gradeService.selectById(params));
    }
}
