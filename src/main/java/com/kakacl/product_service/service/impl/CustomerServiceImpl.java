package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.CustomerServiceMapper;
import com.kakacl.product_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 客服服务类
 * @date 2019-02-11
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerServiceMapper customerServiceMapper;

    @Override
    public List<Map> selectListLimit10(Map params) {
        return customerServiceMapper.selectListLimit10(params);
    }

    @Override
    public boolean insertOne(Map params) {
        return customerServiceMapper.insertOne(params);
    }

    @Override
    public List<Map> findCustomerList(Map params) {
        return customerServiceMapper.findCustomerList(params);
    }

    @Override
    public boolean findMessageExist(Map params) {
        return customerServiceMapper.findMessageExist(params);
    }
}
