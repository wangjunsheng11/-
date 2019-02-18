package com.kakacl.product_service.service;

import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface GradeService {

    Map selectById(java.util.Map params);

    // 增加用户的经验,只增加经验
    boolean updateGrade(Map params);

    boolean insert(java.util.Map params);
}
