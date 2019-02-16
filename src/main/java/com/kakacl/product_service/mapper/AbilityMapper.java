package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 账户能力mapper
 * @date 2019-01-09
 */
public interface AbilityMapper {

    // 获取能力规则
    @Select("select * from zzf_user_ability_rule where del_flag = 0")
    List<Map> selectRuleList(Map params);

    // 查询没有能力的10条用户 已经赋予能力,然后删除的能力不查询
    @Select("SELECT * FROM zzf_user_info WHERE id NOT IN (SELECT user_id FROM zzf_user_ability)")
    List<Map> selectAblityListTop10(Map params);

    // 根据主键查询规则
    @Select("select * from zzf_user_ability_rule where id = #{id}")
    Map selectRuleById(Map params);

    @Select("SELECT * FROM zzf_user_ability WHERE user_id = #{user_id}")
    List<Map> selectByUserid(java.util.Map params);

    @Update("UPDATE zzf_user_ability SET name=#{name}, remark=#{remark} WHERE id=#{id}")
    boolean updateOne(Map params);

    @Insert("INSERT INTO zzf_user_ability (id, user_id, `name`, img_path, remark, create_date, create_by) VALUES (#{id}, #{user_id}, #{name}, #{img_path}, #{remark}, #{create_date}, #{create_by})")
    public boolean insertOne(Map params);
}
