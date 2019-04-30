package com.kakacl.product_service;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
//@EnableConfigServer
@MapperScan("com.kakacl.product_service.mapper")
public class ProductServiceApplication {

    @Autowired
    private Environment env;

    /*@Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress(env.getProperty("redisson.address"));
        RedissonClient client = Redisson.create(config);
        return client;
    }*/

    /*@Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
            CommonsMultipartResolver resolver = new CommonsMultipartResolver();
            resolver.setDefaultEncoding("UTF-8");
            resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常  
            resolver.setMaxInMemorySize(40960);
            resolver.setMaxUploadSize(100 * 1024 * 1024);//上传文件大小 5M 5*1024*1024  
            return resolver;
    }*/

//    @Bean
//    public RedissonClient redissonClient(){
//        Config config=new Config();
//        config.useSingleServer().setAddress(env.getProperty("redisson.address"));
//        config.useSingleServer().setPassword("root");
//        RedissonClient client = Redisson.create(config);
//        return client;
//    }

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
