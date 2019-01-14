package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.ProtocolMapper;
import com.kakacl.product_service.service.ProtocolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 协议实现
 * @date 2019-01-14
 */
@Service
public class ProtocolServiceImpl implements ProtocolService {

    @Autowired
    private ProtocolMapper protocolMapper;

    @Override
    public boolean addProtocol(Map params) {
        return protocolMapper.addProtocol(params);
    }

    @Override
    public List<Map> findGroup(Map params) {
        return protocolMapper.findGroup(params);
    }

    @Override
    public List<Map> findProtocol(Map params) {
        return protocolMapper.findProtocol(params);
    }
}
