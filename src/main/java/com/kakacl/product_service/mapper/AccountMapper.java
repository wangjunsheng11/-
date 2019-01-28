package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import java.util.*;

public interface AccountMapper {

    @Select("SELECT * FROM zzf_user_info WHERE id = #{user_id}")
    Map selectByUserid(java.util.Map params);

    @Select("SELECT * FROM zzf_user_info WHERE phone_num = #{phone_num}")
    Map selectByPhone(java.util.Map params);

    @Select("SELECT * FROM zzf_user_info WHERE phone_num = #{search_key} or kaka_num = #{search_key}")
    List<Map> selectByPhoneORKakaNum(java.util.Map params);

    @Insert("INSERT INTO zzf_user_info (id, user_name, hear_path, phone_num, kaka_num, roleid, id_card, create_time, create_by, account_status, introduction, del_flag) " +
            "VALUES (#{id}, 'anonymous', '', #{phone_num}, #{kaka_num}, '0', #{id_card}, #{create_time}, '1', '52000', '没有简介', '0')")
    boolean insert(Map<String, Object> params);

    @Select("SELECT * FROM zzf_user_info WHERE del_flag = 0 ORDER BY create_time DESC")
    List<Map> selectByPageAndSelections(java.util.Map params);
}
