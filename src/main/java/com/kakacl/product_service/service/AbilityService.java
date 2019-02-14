package com.kakacl.product_service.service;

import java.util.*;

public interface AbilityService {

    // 获取能力规则
    List<Map> selectRuleList(Map params);

    // 根据主键查询规则
    Map selectRuleById(Map params);

    List<Map> selectByUserid(java.util.Map params);

    boolean updateOne(Map params);

    boolean insertOne(Map params);
}
