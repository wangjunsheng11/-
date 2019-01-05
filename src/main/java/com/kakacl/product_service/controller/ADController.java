package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.ADService;
import com.kakacl.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/{version}/ad")
@RefreshScope
public class ADController {

    @Value("${version}")
    private String version;

    @Autowired
    private ADService adService;

    @RequestMapping("list")
    public Object list(){
        return adService.selectAD(null);
    }
}
