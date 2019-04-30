package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mappers.TestMapper;
import com.kakacl.product_service.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 多数据源测试类
 * @date
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;
    @Override
    public List<Map> getList() {
        System.out.print("王俊生=service");
        return testMapper.getList();
    }
}
