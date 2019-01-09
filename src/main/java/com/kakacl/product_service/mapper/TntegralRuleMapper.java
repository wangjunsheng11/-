package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/*
 *
 * 积分规则mapper
 * @author wangwei
 * @date 2019/1/9
  * @param null
 * @return
 */
public interface TntegralRuleMapper {

    @Select("SELECT * FROM zzf_integral_rule WHERE del_flag = 0")
    List<Map> selectList(Map params);
}
