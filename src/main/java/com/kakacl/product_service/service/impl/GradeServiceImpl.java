package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.GradeMapper;
import com.kakacl.product_service.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeMapper gradeMapper;

    @Override
    public Map selectById(Map params) {
        return gradeMapper.selectById(params);
    }

    @Override
    public boolean insert(Map params) {
        return gradeMapper.insert(params);
    }
}
