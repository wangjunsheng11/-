package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.*;

public interface AccountMapper {

    // 根据用户主键查询最近的一条信息
    // @Select("SELECT * FROM zzf_user_employee_hitory WHERE user_id = #{user_id} ORDER BY create_time LIMIT 0, 1")
    @Select("SELECT sc.id as company_id, sc.company_name, sc.image, zueh.orbit_id, zueh.entry_time, zueh.resignation_time, zueh.work_status FROM zzf_user_employee_hitory zueh " +
            "            LEFT JOIN store_company sc ON zueh.company_id = sc.id " +
            "            WHERE zueh.user_id = #{user_id} ORDER BY zueh.create_time DESC LIMIT 0, 1")
    Map selectHistoryByYserId(Map params);

    @Select("SELECT * FROM zzf_user_info WHERE id = #{user_id}")
    Map selectByUserid(java.util.Map params);

    @Select("SELECT * FROM zzf_user_info WHERE phone_num = #{phone_num}")
    Map selectByPhone(java.util.Map params);

    @Select("SELECT * FROM zzf_user_info WHERE phone_num = #{search_key} or kaka_num = #{search_key}")
    List<Map> selectByPhoneORKakaNum(java.util.Map params);

    @Insert("INSERT INTO zzf_user_info (id, user_name, hear_path, phone_num, kaka_num, roleid, id_card, create_time, create_by, account_status, introduction, del_flag) " +
            "VALUES (#{id}, 'anonymous', '', #{phone_num}, #{kaka_num}, '0', #{id_card}, #{create_time}, '1', '52000', '没有简介', '0')")
    boolean insert(Map<String, Object> params);

    /*
     * 更新个人资料
     *
     * @author wangwei
     * @date 2019/2/10
      * @param params
     * @return boolean
     */
    @Update("UPDATE zzf_user_info SET user_name=#{user_name}, phone_num=#{phone_num}, id_card=#{id_card}, introduction=#{introduction} WHERE (id=#{id})")
    boolean updateInfo(Map params);

    @Select("SELECT * FROM zzf_user_info WHERE del_flag = 0 ORDER BY create_time DESC")
    List<Map> selectByPageAndSelections(java.util.Map params);
}
