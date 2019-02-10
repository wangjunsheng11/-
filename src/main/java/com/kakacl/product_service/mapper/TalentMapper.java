package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.*;

/*
 *
 * 用户天赋mapper
 * @author wangwei
 * @date 2019/1/9
  * @param null
 * @return
 */
public interface TalentMapper {

    @Select("SELECT * FROM zzf_user_talent WHERE user_id = #{user_id} AND del_flag = 0")
    List<Map> selectList(Map params);

    // 获取天赋
    @Select("SELECT * FROM zzf_talent WHERE del_flag = 0")
    List<Map> selectListTalent(Map params);

    // 根据ID获取天赋
    @Select("SELECT * FROM zzf_talent WHERE del_flag = 0 AND id = #{id}")
    Map selectTalentById(Map params);

    /**
     * 增加天赋信息
     * @param params
     * @return
     */
    @Insert("INSERT INTO zzf_user_talent " +
            "(id, `order`, user_id, `name`, img_path, remark) VALUES " +
            "(#{id}, #{order}, #{user_id}, #{name}, #{img_path}, #{remark})")
    boolean edit(Map params);

    @Update("UPDATE zzf_user_talent SET del_flag=1 WHERE (user_id=#{user_id})")
    boolean delete(Map params);
}
