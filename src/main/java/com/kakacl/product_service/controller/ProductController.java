package com.kakacl.product_service.controller;

import com.kakacl.product_service.domain.Product;
import com.kakacl.product_service.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/{version}/product")
@RefreshScope
public class ProductController {

    @Value("${server.port}")
    private String port;

    @Value("${env}")
    private String env;

    @Value("${version}")
    private String version;

    @Autowired
    private ProductService productService;

    /**
     * 获取所有商品列表
     * http://127.0.0.1:9000/apigateway/product/api/v1/product/list?token=1&user_id=1&product_id=1
     * @return
     */
    @RequestMapping("list")
    public Object list(){
        return productService.listProduct();
    }


    /**
     * 根据id查找商品详情
     * @param id
     * @return
     */
    @RequestMapping("find")
    public Object findById(int id){

//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Product product = productService.findById(id);

        Product result = new Product();
        BeanUtils.copyProperties(product,result);
        result.setName( result.getName() + " data from port="+port +" env = "+env );
        return result;
    }

    @Autowired
    private ProductService mapper;

    @RequestMapping("test")
    public Object test(){
        return mapper.selectCountTotal();
    }

}
