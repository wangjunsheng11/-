package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.GradeRuleMapper;
import com.kakacl.product_service.mapper.TntegralRuleMapper;
import com.kakacl.product_service.service.TntegralRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 积分规则实现
 * @date 2019-01-09
 */
@Service
public class TntegralRuleServiceImpl implements TntegralRuleService {

    @Autowired
    private TntegralRuleMapper tntegralRuleMapper;

    @Override
    public List<Map> selectList(Map params) {
        return tntegralRuleMapper.selectList(params);
    }
}
