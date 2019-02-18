package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface GradeMapper {

    @Insert("INSERT INTO zzf_user_grade (id, user_id, fraction, grade, create_time, create_by, del_flag) VALUES (#{id}, #{user_id}, '0', '1', #{create_time}, '1', '0')")
    boolean insert(java.util.Map params);

    // 增加用户的经验,只增加经验
    @Update("UPDATE zzf_user_grade SET fraction = (fraction + #{fraction}) WHERE (user_id = #{user_id})")
    boolean updateGrade(Map params);

    // 更新用户等级
    @Update("UPDATE zzf_user_grade SET grade = #{fraction} WHERE (user_id = #{user_id})")
    boolean updateGradegrade(Map params);

    // 根据用户主键查询信息
    @Select("SELECT * FROM zzf_user_grade WHERE user_id = #{user_id}")
    Map selectById(java.util.Map params);

    // 查询所有用户
    @Select("select * from zzf_user_grade where del_flag = 0")
    List<Map> selectList();
}
