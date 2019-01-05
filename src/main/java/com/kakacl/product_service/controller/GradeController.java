package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.GradeService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/{version}/grade")
public class GradeController {
    @Autowired
    private GradeService gradeService;

    @RequestMapping("selectById")
    public Object selectById(String user_id){
        java.util.Map params = new HashMap();
        params.put("user_id", user_id);
        return Resp.success(gradeService.selectById(params));
    }
}
