package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import  java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description CAS单点登录用
 * @date
 */
public interface CasAccountMapper {

    @Insert("INSERT INTO cas_account" +
            "(id, sys_type, kaka_num, phone_num, email, pass_word, status, del_flag, create_time, create_by) VALUES" +
            "(#{id}, #{sys_type}, #{kaka_num}, #{phone_num}, #{email}, #{pass_word}, #{status}, #{del_flag}, #{create_time}, #{create_by})")
    boolean insert(Map<String, Object> params);

    /*
     *
     * 根据咔咔号或者手机号码查寻用户是否存在
     * @author wangwei
     * @date 2019/1/8
      * @param params
     * @return java.util.Map
     */
    @Select("SELECT * FROM cas_account WHERE (kaka_num = #{kaka_num} OR phone_num = #{phone_num}) AND pass_word = #{pass_word} AND del_flag = 0 LIMIT 0, 1")
    java.util.Map selectOne(Map<String, Object> params);

    @Select("SELECT * FROM cas_account WHERE phone_num = #{phone_num} LIMIT 0, 1")
    java.util.Map selectOneByPhonenum(Map<String, Object> params);

    @Select("SELECT * FROM cas_account WHERE kaka_num = #{kaka_num} LIMIT 0, 1")
    java.util.Map selectOneByKakanum(Map<String, Object> params);

    @Update("UPDATE cas_account SET pass_word=#{pass_word} WHERE id=#{id}")
    int updateOnePassById(Map<String, Object> params);

}
