//package com.kakacl.product_service.test;
//
//import com.kakacl.product_service.utils.SignUtil;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.UnsupportedEncodingException;
//import java.util.*;
//
///**
// * @author wangwei
// * @version v1.0.0
// * @description 签名测试
// * @date 2019-01-22
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SignTest {
//
//    /** 加密密钥 */
//    public String appkey = "mykey123456";
//
//    // 间隔时间
//    public int timeout = 1 * 30 * 1000;
//
//    /*
//     *
//     * 测试sign签名的的测试方法
//     *
//     * 参数 time sign 不在签名参数中。
//     * 修改参数会修改sign参数才会成功。
//     *
//     * @author zzf@kakacl.com
//     * @date 2019/1/22
//      * @param  sign 签名
//     * @return void
//     */
//    @Test
//    public void testSign() throws Exception {
//        Map params = new HashMap();
//        params.put("sign", "89e4d9da9c3b6e8ffb1c80848c1f8d32");
//        params.put("time", System.currentTimeMillis() + "");
//        params.put("secretKey", "mysecret123456");
//        // Thread.sleep(500L);
//        System.out.println(params.toString());
//        System.out.println(verify(params));;
//    }
//
//    public Map verify(Map<String, String> params) {
//        Map result = new HashMap();
//        String sign = "";
//        if (params.get("sign") != null) {
//            sign = params.get("sign");
//        }else {
//            result.put("sign", "sign is null");
//            return result;
//        }
//        String timestamp = "";
//        if (params.get("time") != null) {
//            timestamp = params.get("time");
//        }else {
//            result.put("time", "time is null");
//            return result;
//        }
//        //过滤空值、sign
//        Map<String, String> sParaNew = SignUtil.paraFilter(params);
//        //获取待签名字符串
//        String preSignStr = SignUtil.createLinkString(sParaNew);
//        //获得签名验证结果
//        String mysign = DigestUtils.md5Hex(SignUtil.getContentBytes(preSignStr + appkey, "UTF-8"));
//        if (mysign.equals(sign)) {
//            //是否超时
//            long curr = System.currentTimeMillis();
//            if ((curr - Long.valueOf(timestamp)) > timeout){
//                result.put("time", "api is time out " + curr);
//                result.put("sysTime", curr);
//                result.put("sysSign", mysign);
//                return result;
//            }
//            result.put("time", "sign success .");
//            result.put("sysSign", mysign);
//            return result;
//        } else {
//            result.put("sysSign", mysign);
//            result.put("sysSign_tips", "sign error .");
//            return result;
//        }
//    }
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
//            stringRedisTemplate.opsForValue().set(daytime.toString(),"1");
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
//        }
//    }
//
//}
