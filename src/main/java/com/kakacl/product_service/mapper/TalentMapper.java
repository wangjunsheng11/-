package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;

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
}
