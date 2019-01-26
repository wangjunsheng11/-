package com.kakacl.product_service.task;

import com.kakacl.product_service.service.JobTrajectoryAutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 求职者工作轨迹任务定时器
 * @date 2019-01-26
 */
@Component
@Configurable
@EnableScheduling
public class JobTrajectoryAutoTask {

    // 求职轨迹服务类
    @Autowired
    private JobTrajectoryAutoService jobTrajectoryAutoService;

    /*
     * 求职者轨迹定时任务
     *
     * 步骤：
     *  1. 获取最多50条门店系统中求职者手机号、求职轨迹主键、求职者当时的手机号、开始工作时间、补贴截止时间，条件：周周发轨迹表中没有记录编号no的数据，删除标记为没有删除，截止时间不为空，时间按降序获取。
     *  2. 根据门店系统求职者当时的手机号码，获取门店系统求职者主键。
     *  3. 根据门店系统求职者当时的手机号码查询出求职者的状态。
     *  4. 保存门店系统收集的数据到周周发求职者历史求职轨迹中。
     *
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
    public void employeeOrbitTask(){
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
            Map dataMap = jobTrajectoryAutoService.findPhoneByUserInfo(params);
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
//                System.out.println(String.format("work_status: %s", work_status));
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
}
