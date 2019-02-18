package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.GradeRuleMapper;
import com.kakacl.product_service.service.GradeRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 等级规则实现类
 * @date 2019-01-09
 */
@Service
public class GradeRuleServiceImpl implements GradeRuleService {

    @Autowired
    private GradeRuleMapper gradeRuleMapper;

    @Override
    public List<Map> selectList(Map params) {
        return gradeRuleMapper.selectList(params);
    }

    @Override
    public Map selectMapByUpGrade(Map params) {
        return gradeRuleMapper.selectMapByUpGrade(params);
    }
}
