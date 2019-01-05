package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.ADMapper;
import com.kakacl.product_service.service.ADService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ADServiceImpl implements ADService {

    @Autowired
    private ADMapper adMapper;

    @Override
    public List<Map<String, String>> selectAD(Map params) {
        return adMapper.selectAD(params);
    }
}
