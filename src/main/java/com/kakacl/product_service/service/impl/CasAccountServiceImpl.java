package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.CasAccountMapper;
import com.kakacl.product_service.service.CasAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description CAS单点登录实现
 * @date 2018-01-09
 */
@Service
public class CasAccountServiceImpl implements CasAccountService {

    @Autowired
    private CasAccountMapper casAccountMapper;

    @Override
    public boolean insert(Map<String, Object> params) {
        return casAccountMapper.insert(params);
    }

    @Override
    public Map selectOne(Map<String, Object> params) {
        return casAccountMapper.selectOne(params);
    }

    @Override
    public Map selectOneByPhonenum(Map<String, Object> params) {
        return casAccountMapper.selectOneByPhonenum(params);
    }

    @Override
    public Map selectOneByKakanum(Map<String, Object> params) {
        return casAccountMapper.selectOneByKakanum(params);
    }
}
