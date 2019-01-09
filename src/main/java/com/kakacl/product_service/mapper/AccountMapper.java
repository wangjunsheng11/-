package com.kakacl.product_service.mapper;

import com.kakacl.product_service.domain.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import java.util.*;

public interface AccountMapper {

    @Select("SELECT * FROM zzf_user_info WHERE id = #{user_id}")
    Map selectByUserid(java.util.Map params);

    @Insert("INSERT INTO zzf_user_info (id, user_name, hear_path, phone_num, kaka_num, roleid, id_card, create_time, create_by, account_status, introduction, del_flag) " +
            "VALUES (#{id}, 'anonymous', '', #{phone_num}, #{kaka_num}, '0', #{id_card}, #{create_time}, '1', '1', '没有简介', '0')")
    boolean insert(Map<String, Object> params);

    @Select("SELECT id,user_name FROM zzf_user_info")
    List<Account> selectByPageAndSelections(java.util.Map params);
}
