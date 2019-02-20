package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.AppVersionMapper;
import com.kakacl.product_service.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description
 * @date
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {

    @Autowired
    private AppVersionMapper appVersionMapper;


    @Override
    public List<Map> findNowVersion(Map params) {
        return appVersionMapper.findNowVersion(params);
    }
}
