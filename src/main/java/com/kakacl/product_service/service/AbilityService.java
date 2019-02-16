package com.kakacl.product_service.service;

import java.util.*;

public interface AbilityService {

    // 获取能力规则
    List<Map> selectRuleList(Map params);

    // 查询没有能力的10条用户 已经赋予能力,然后删除的能力不查询
    List<Map> selectAblityListTop10(Map params);

    // 根据主键查询规则
    Map selectRuleById(Map params);

    List<Map> selectByUserid(java.util.Map params);

    boolean updateOne(Map params);

    boolean insertOne(Map params);
}
