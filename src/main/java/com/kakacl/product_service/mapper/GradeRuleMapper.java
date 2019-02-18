package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 等级规则mapper
 * @date 2019-01-09
 */
public interface GradeRuleMapper {

    @Select("SELECT * FROM zzf_grade_rule WHERE del_flag = 0")
    List<Map> selectList(Map params);

    // 根据等级获取下一等级数据
    @Select("SELECT * FROM zzf_user_grade_rule WHERE del_flag = 0 AND grade = #{grade} + 1 LIMIT 0,1")
    Map selectMapByUpGrade(Map params);
}
