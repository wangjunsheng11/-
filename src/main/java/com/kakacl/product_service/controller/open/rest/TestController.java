package com.kakacl.product_service.controller.open.rest;

import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.domain.Product;
import com.kakacl.product_service.service.ProductService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 测试控制器
 * @date 2019-01-11
 */
@RestController
@RequestMapping("/api/open/rest/{version}/test")
public class TestController extends BaseController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /*@RequestMapping(value = "get", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp get(HttpServletRequest request, String time, String key) {
        String redisValue=stringRedisTemplate.opsForValue().get(key);
        return Resp.success(redisValue);
    }

    @RequestMapping(value = "set", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp set(HttpServletRequest request, String time, String key) {
        Boolean res=true;
        res=stringRedisTemplate.opsForValue().setIfAbsent(key,key + System.nanoTime());
        return Resp.success(res);
    }*/

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
