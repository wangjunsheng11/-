package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.*;

public interface BackCardMapper {

    @Insert("INSERT INTO zzf_user_back_card (id, user_id, back_type, phone_num, card_num, id_card, create_time, create_by) VALUES (#{id}, #{user_id}, #{back_type}, #{phone_num}, #{card_num}, #{id_card}, #{create_time}, #{create_by})")
    boolean addCard(Map params);

    @Select("SELECT * FROM zzf_user_back_card WHERE user_id = #{user_id} AND del_flag = 0")
    List<Map> selectList(Map params);

    @Select("SELECT * FROM zzf_user_back_card WHERE card_num = #{card_num} AND del_flag = 0")
    List<Map> selectBackCarcdExist(Map params);

    @Update("UPDATE zzf_user_back_card SET del_flag=#{del_flag} WHERE (id = #{id})")
    boolean updateById(Map params);

    /*
     *
     * 根据身份证号码查询用户信息
     * @author wangwei
     * @date 2019/1/11
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_user_info WHERE id_card = #{id_card}")
    List<Map> selectUSerByIdcard(Map params);

    @Select("SELECT * FROM zzf_user_income WHERE user_id = #{user_id} AND del_flag = 0")
    List<Map> selectIncomeByUserid(Map params);
}
