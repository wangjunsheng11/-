package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;
import java.util.*;

public interface AccountMapper {

    @Select("SELECT * FROM zzf_user_info WHERE id = #{id}")
    Map selectById(java.util.Map params);
}
