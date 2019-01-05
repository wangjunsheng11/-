package com.kakacl.product_service.mapper;

import com.kakacl.product_service.domain.Account;
import org.apache.ibatis.annotations.Select;
import java.util.*;

public interface AccountMapper {

    @Select("SELECT * FROM zzf_user_info WHERE id = #{id}")
    Map selectById(java.util.Map params);

    @Select("SELECT id,user_name FROM zzf_user_info")
    List<Account> selectByPageAndSelections(java.util.Map params);
}
