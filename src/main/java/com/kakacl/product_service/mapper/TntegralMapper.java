package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 积分mapper
 * @date 2019-01-09
 */
public interface TntegralMapper {

    @Select("SELECT * FROM zzf_user_integral WHERE user_id = #{user_id} AND del_flag = 0")
    Map selectOneByUserid(Map params);
}
