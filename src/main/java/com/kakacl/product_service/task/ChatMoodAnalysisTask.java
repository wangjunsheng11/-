package com.kakacl.product_service.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baidu.aip.nlp.AipNlp;
import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.service.ChatMoodAnalysisService;
import com.kakacl.product_service.utils.IDUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 情绪分析任务
 * @date 2019-02-12
 */
@Component
@Configurable
@EnableScheduling
public class ChatMoodAnalysisTask {

    public static final String APP_ID = Constant.APP_ID;
    public static final String API_KEY = Constant.API_KEY;
    public static final String SECRET_KEY = Constant.SECRET_KEY;

    @Autowired
    private ChatMoodAnalysisService chatMoodAnalysisService;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    /*
     * 情绪分析任务，一分钟执行一次，每次分析10条
     *
     * @author wangwei
     * @date 2019/2/12
      * @param
     * @return void
     */
    @Scheduled(cron = "0 */1 *  * * * ")
    public void chatMoodTask(){
        // 加锁
        String key = Constant.MOOD_ANALYSIS_TASK_AUTO_TASK;
        Boolean res = true;
        while(res) {
            String value = UUID.randomUUID().toString() + System.nanoTime();
            res = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
            if (res) {
                stringRedisTemplate.opsForValue().increment(Constant.EMPLOYEE_ORBIT_TASK_AUTO_LOCK, 1L);
                try {
                    res = false;
                    Map params = new HashMap();
                    List<Map> data = chatMoodAnalysisService.selectListLimit10(null);
                    for (int i = 0; i < data.size(); i++) {
                        String chat_id = data.get(i).get("id") + "";
                        String id = IDUtils.genHadId();
                        // 根据内容分析情绪
                        String mood = getMood(data.get(i).get("content") + "");
                        params.put("id", id);
                        params.put("chat_id", chat_id);
                        params.put("mood", mood);
                        chatMoodAnalysisService.insert(params);
                        try {
                            Thread.sleep(50L);
                        } catch (Exception e) {
                        }
                    }
                }catch (Exception e) {
                } finally {
                    String redisValue = stringRedisTemplate.opsForValue().get(key);
                    if (StringUtils.isNotBlank(redisValue) && redisValue.equals(value)) {
                        stringRedisTemplate.delete(key);
                    }
                }
            } else{
                res = true;
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                }
            }
        }
    }

    private static String getMood (String text) {
        try {
            AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
            JSONObject res = client.sentimentClassify(text, null);
            JSON jArray = JSON.parseArray(res.get("items").toString());
//        System.out.println("积极概率： " + JSON.parseObject(((JSONArray) jArray).get(0).toString()).get("positive_prob"));
//        System.out.println(res.toString(2));
            return JSON.parseObject(((JSONArray) jArray).get(0).toString()).get("positive_prob") + "";
        } catch (Exception e){
            return "0";
        }
    }
}
