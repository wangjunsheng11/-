package com.kakacl.product_service.mapper;

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

    @Select("SELECT * FROM zzf_user_ability WHERE user_id = #{user_id}")
    List<Map> selectByUserid(java.util.Map params);

    @Update("UPDATE zzf_user_ability SET name=#{name}, remark=#{remark} WHERE id=#{id}")
    boolean updateOne(Map params);
}
