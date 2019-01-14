package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.StartImageMapper;
import com.kakacl.product_service.service.StartImageService;
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
public class StartImageServiceImpl implements StartImageService {

    @Autowired
    private StartImageMapper startImageMapper;

    @Override
    public List<Map> list(Map params) {
        return startImageMapper.selectList(params);
    }
}
