package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface GradeMapper {

    @Select("SELECT * FROM zzf_user_grade WHERE user_id = #{user_id}")
    Map selectById(java.util.Map params);
}
