package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.AbilityMapper;
import com.kakacl.product_service.service.AbilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 账户能力impl
 * @date 2019-01-09
 */
@Service
public class AbilityServiceImpl implements AbilityService {

    @Autowired
    private AbilityMapper abilityMapper;

    @Override
    public List<Map> selectRuleList(Map params) {
        return abilityMapper.selectRuleList(params);
    }

    @Override
    public Map selectRuleById(Map params) {
        return abilityMapper.selectRuleById(params);
    }

    @Override
    public List<Map> selectByUserid(Map params) {
        return abilityMapper.selectByUserid(params);
    }

    @Override
    public boolean updateOne(Map params) {
        return abilityMapper.updateOne(params);
    }

    @Override
    public boolean insertOne(Map params) {
        return abilityMapper.insertOne(params);
    }
}
