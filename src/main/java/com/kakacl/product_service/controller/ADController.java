package com.kakacl.product_service.controller;

import com.kakacl.product_service.service.ADService;
import com.kakacl.product_service.service.ProductService;
import com.kakacl.product_service.utils.Resp;
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

    /*
     *
     * 查询所有没有删除的广告
     * @author wangwei
     * @date 2019/1/7
      * @param
     * @return com.kakacl.product_service.utils.Resp
     */
    @RequestMapping("list")
    public Resp list(){
        return Resp.success(adService.selectAD(null));
    }
}
