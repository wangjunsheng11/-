package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.StartUpMapper;
import com.kakacl.product_service.service.StartUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 启动控制器
 * @date 2010-01-14
 */
@Service
public class StartUpServiceImpl implements StartUpService {

    @Autowired
    private StartUpMapper startUpMapper;

    @Override
    public List<Map> list(Map params) {
        return startUpMapper.selectList(params);
    }
}
