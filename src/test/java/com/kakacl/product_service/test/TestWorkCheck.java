//package com.kakacl.product_service.test;
//
//import com.kakacl.product_service.service.AccountService;
//import com.kakacl.product_service.service.PingCardService;
//import com.kakacl.product_service.service.WorkCheckService;
//import com.kakacl.product_service.utils.DateUtil;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class TestWorkCheck extends TmallApplicationTests{
//
//    @Autowired
//    private WorkCheckService workCheckService;
//    @Autowired
//    PingCardService pingCardService;
//    @Autowired
//    private AccountService accountService;
//    @Test
//    public void tests(){
//        Map map =new HashMap();
//        Map map1 =new HashMap();
//        map.put("id",6);
//        map1 =workCheckService.findWorkCheckById(map);
//        String first_STime=map1.get("first_STime").toString();
//        first_STime = first_STime.replaceAll(":","");
//        System.out.println(first_STime+"长度"+first_STime.length());
//
//
//    }
//
//    @Test
//    public void test2(){
//        Map map =new HashMap();
//
//        map.put("user_id", "1547006424247526");
//        map.put("time", 2019042712);
//        map.put("time1", 2019042715);
//         Integer count =pingCardService.selectCountPingOfNight(map);
//         System.out.println(count);
//    }
//
//}
