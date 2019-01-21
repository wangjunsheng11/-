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

    @Bean
    public RedissonClient redissonClient(){
        Config config=new Config();
        config.useSingleServer().setAddress(env.getProperty("redisson.address"));
        RedissonClient client = Redisson.create(config);
        return client;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
