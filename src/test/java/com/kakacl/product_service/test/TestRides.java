//package com.kakacl.product_service.test;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Iterator;
//import java.util.Set;
//public class TestRides extends TmallApplicationTests{
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    public void setShiftJudgment(String user_id , String shift, String ping_type, Long create_time){
//        StringBuffer daytime = new StringBuffer(shift);
//        if(ping_type.equals("上班")){
//            daytime.append(user_id+"s_b");
//        }else{
//            daytime.append(user_id+"x_b");
//        }
//        System.out.println(daytime.toString());
//
//        stringRedisTemplate.opsForValue().set(daytime.toString(),"1");
//
//
//    }
//    @Test
//    public void testRedes() throws Exception {
//        setShiftJudgment("123456","DAYTIME","上班",1L);
//        setShiftJudgment("123456","DAYTIME","下班",1L);
//        stringRedisTemplate = new StringRedisTemplate();
//        Set set = redisTemplate.keys("DAYTIME*");
//        Iterator<String> it = set.iterator();
//        while (it.hasNext()) {
//            String str = it.next();
//            System.out.println("test:"+str);
//            redisTemplate.delete(str);
//        }
//        Set sets = redisTemplate.keys("DAYTIME*");
//        if (sets==null) {
//            System.out.println("test:null" + 1);
//        }else{
//            Iterator<String> its = sets.iterator();
//            while (its.hasNext()) {
//                String str = its.next();
//                System.out.println("test:" + str);
//            }
//        }
//    }
//
//
//}
