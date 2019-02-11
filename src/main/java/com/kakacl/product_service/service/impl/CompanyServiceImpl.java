package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.CompanyMapper;
import com.kakacl.product_service.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 公司服务类
 * @date 2019-02-10
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public Map selectById(Map params) {
        return companyMapper.selectById(params);
    }
}
