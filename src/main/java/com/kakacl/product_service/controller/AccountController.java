package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.ADService;
import com.kakacl.product_service.service.AccountService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/{version}/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("selectById")
    public Object selectById(String id){
        java.util.Map params = new HashMap();
        params.put("id", id);
        return Resp.success(accountService.selectById(params));
    }
}
