package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

public interface GradeRuleService {

    List<Map> selectList(Map params);

    // 根据等级获取下一等级数据
    Map selectMapByUpGrade(Map params);
}
