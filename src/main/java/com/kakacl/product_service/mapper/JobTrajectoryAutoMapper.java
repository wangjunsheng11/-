package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import java.util.*;

public interface JobTrajectoryAutoMapper {

    /*
     * 查询 0-50条昨天入职的人员信息
     *
     * @author wangwei
     * @date 2019/1/25
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM store_subsidy WHERE del_flag = 0 AND DATE_FORMAT( start_wark_date,'%Y-%m-%d') = DATE_FORMAT(CURDATE()-1,'%Y-%m-%d') AND end_wark_date IS NOT NULL AND `no` NOT IN (SELECT `no` FROM zzf_user_employee_hitory WHERE `no` IS NOT NULL) ORDER BY start_wark_date ASC LIMIT 0, 50")
    List<Map> findListo0_50(Map params);

    /*
     * 根据求职者手机号码查询求职者信息
     *
     * @author wangwei
     * @date 2019/1/25
      * @param params
     * @return java.util.Map
     */
    @Select("SELECT * FROM zzf_user_info WHERE id_card = (SELECT card_no FROM store_account_id_card WHERE account_id = (SELECT id FROM store_account WHERE phone = #{phone} LIMIT 0, 1) LIMIT 0, 1)")
    Map findUserInfoByPhone(Map params);

    /*
     * 根据用户信息用户状态
     *
     * params user_id SA.phone  company_id
     * @author wangwei
     * @date 2019/1/25
      * @param params
     * @return java.util.Map
     */
    // @Select("SELECT SS.id, SA.id AS account_id, SA.work_status, SS.sys_user_id, SC.id AS company_id  FROM store_account SA JOIN store_account_id_card SAID ON SAID.account_id = SA.id JOIN store_settlement SS ON SS.store_account_id = SA.id JOIN store_clerk_join SCJ ON SCJ.store_clerk_id = SS.sys_user_id JOIN store_job SJ ON SJ.id = SS.job_id JOIN store_company SC ON SC.id = SJ.company_id WHERE 1 = 1 AND SS.store_account_id = #{user_id} AND SJ.company_id = #{company_id} and SA.phone = #{phone} LIMIT 0, 1")
    @Select("SELECT SA.work_status FROM store_account SA WHERE SA.phone = #{phone} LIMIT 0, 1")
    Map findUserBaseInfo(Map params);

    /*
     * 根据求职者的手机号码查询用于的基础信息
     *
     * @author wangwei
     * @date 2019/1/25
     * @param params
     * @return java.util.Map
     */
    @Select("SELECT id FROM store_account WHERE phone = #{phone} LIMIT 0, 1")
    Map findPhoneBuUserInfo(Map params);

    /*
     * 添加用户轨迹到周周发系统
     *
     * @author wangwei
     * @date 2019/1/25
      * @param params
     * @return boolean
     */
    @Insert("INSERT INTO zzf_user_employee_hitory (id, `no`, user_id, orbit_id, company_id, work_status, entry_time, resignation_time, create_time, create_by) " +
            "VALUES (#{id}, #{no}, #{user_id}, #{orbit_id}, #{company_id}, #{work_status}, #{entry_time}, #{resignation_time}, #{create_time}, #{create_by})")
    boolean insertOne(Map params);

}
