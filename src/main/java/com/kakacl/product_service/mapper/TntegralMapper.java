package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 积分mapper
 * @date 2019-01-09
 */
public interface TntegralMapper {

    /*
     * 用户积分的记录表
     *
     * @author wangwei
     * @date 2019/1/24
     * @param params
     * @return boolean
     */
    @Insert("INSERT INTO zzf_user_integral_record (`id`, `user_id`, `fraction`, `create_time`, `create_by`, message) VALUES (#{id}, #{user_id}, #{fraction},#{create_time}, #{create_by}, #{message})")
    boolean insertOne(Map params);

    /*
     * 获取当前用户总积分
     *
     * @author wangwei
     * @date 2019/1/24
     * @param params
     * @return java.util.Map
     */
    // SELECT * FROM zzf_user_integral
    @Select("SELECT SUM(fraction) as fraction FROM zzf_user_integral_record WHERE user_id = #{user_id} AND del_flag = 0")
    Map selectOneByUserid(Map params);
}
