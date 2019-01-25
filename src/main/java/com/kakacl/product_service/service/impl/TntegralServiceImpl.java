package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.TntegralMapper;
import com.kakacl.product_service.service.TntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 积分实现
 * @date 2019-01-09
 */
@Service
public class TntegralServiceImpl implements TntegralService {

    @Autowired
    private TntegralMapper tntegralMapper;

    @Override
    public Map selectOneByUserid(Map params) {
        return tntegralMapper.selectOneByUserid(params);
    }

    @Override
    public boolean insertOne(Map params) {
        return tntegralMapper.insertOne(params);
    }
}
