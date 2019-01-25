package com.kakacl.product_service.task;

import com.alibaba.fastjson.JSON;
import com.kakacl.product_service.service.JobTrajectoryAutoService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 定时任务测试
 * @date 2019-01-25
 */
@Component
@Configurable
@EnableScheduling
public class ScheduledTest{

    // 求职轨迹
    @Autowired
    private JobTrajectoryAutoService jobTrajectoryAutoService;

    /*
     * 需要将用户的入职信息从表store_employee_history - zzf_user_employee_hitory
     * 获取用户的工资信息 - 补贴信息 - 入职时间 - 离职信息 - 补贴到期时间 等信息
     * 工资详情暂时无获取路径
     *
     * 按照入职时间为昨天的进行查询
     * 根据身份证查询出门店系统中用户的主键，根据主键查询用户的轨迹，对比后获取用户的求职轨迹 store_subsidy-明确员工入职时间
     *
     * 需要的值 周周发用户主键 - 公司主键 - 当前工作状态 - 入职时间 - 离职时间(离职分 补贴未达成离职，补贴已达成离职， 离职异常) - -
     * store_employee_history - 是否离职，根据工作状态进行判断
     *
     */
    @Scheduled(fixedRate = 1000 * 10)
    public void test_10_s(){
        Map params = new HashMap();
//        System.out.println("查询用户的求职轨迹 - " + dateFormat ().format (new Date()));
        params.clear();
        List<Map> data = jobTrajectoryAutoService.findListo0_50(params);
        for (int i = 0; i < data.size(); i++) {
            String company_id = data.get(i).get("company_id").toString();
            // 求职轨迹主键
            String orbit_id = data.get(i).get("id").toString();
            // 求职者手机号
            String phone = data.get(i).get("party_b_phone_num").toString();

            Date start_wark_date = (Date)(data.get(i).get("start_wark_date"));
            Date end_wark_date = (Date)(data.get(i).get("end_wark_date"));
            String no = data.get(i).get("no").toString();
            // 根据求职者手机号查询的身份证号码
            params.clear();
            params.put("phone", phone);
            Map userinfo = jobTrajectoryAutoService.findUserInfoByPhone(params);
            if(userinfo == null) {
                return;
            };
//            System.out.println(JSON.toJSONString(userinfo));
            String zzf_userid = userinfo.get("id").toString();
//            System.out.println(String.format("zzf_userid : %s", zzf_userid));
//            System.out.println(String.format("start_wark_date : %s", start_wark_date));
//            System.out.println(String.format("company_id: %s", company_id));
//            System.out.println(String.format("no: %s", no));

            // 当前工作状态-从当前的状态中获取 离职时间
            // 根据求职者手机号码查询求职者的信息
            params.clear();
            params.put("phone", phone);
            Map dataMap = jobTrajectoryAutoService.findPhoneBuUserInfo(params);
            if(dataMap == null) {
                return;
            };
            params.clear();
            params.put("phone", phone);
            dataMap = jobTrajectoryAutoService.findUserBaseInfo(params);
            if(dataMap == null) {
                return;
            } else {
                String work_status = dataMap.get("work_status").toString();
                System.out.println(String.format("work_status: %s", work_status));
                // 增加信息
                params.clear();
                params.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
                params.put("no", no);
                params.put("user_id", zzf_userid);
                params.put("orbit_id", orbit_id);
                params.put("company_id", company_id);
                params.put("work_status", work_status);
                params.put("entry_time", start_wark_date.getTime() / 1000);
                params.put("resignation_time", end_wark_date.getTime() / 1000);
                params.put("create_time", System.currentTimeMillis() / 1000 + "");
                params.put("create_by", zzf_userid);
                // 保存用户轨迹数据到周周发
                jobTrajectoryAutoService.insertOne(params);
            }
        }
    }

    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*@Scheduled(fixedRate = 1000 * 30)
    public void reportCurrentTime(){
        System.out.println ("Scheduling Tasks Examples: The time is now " + dateFormat ().format (new Date()));
    }*/

    /*@Scheduled(fixedRate = 1000 * 1)
    public void a_1_s(){
        System.out.println ("Scheduling Tasks Examples: The time is now " + dateFormat ().format (new Date()));
    }*/

    //每1分钟执行一次
    @Scheduled(cron = "0 */1 *  * * * ")
    public void reportCurrentByCron(){
        System.out.println ("Scheduling Tasks Examples By Cron: The time is now " + dateFormat ().format (new Date ()));
    }

    private SimpleDateFormat dateFormat(){
        return new SimpleDateFormat ("HH:mm:ss");
    }

}
