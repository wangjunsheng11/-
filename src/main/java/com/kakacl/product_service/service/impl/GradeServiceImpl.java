package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.GradeMapper;
import com.kakacl.product_service.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public boolean updateGrade(Map params) {
        return gradeMapper.updateGrade(params);
    }

    @Override
    public boolean insert(Map params) {
        return gradeMapper.insert(params);
    }

    @Override
    public boolean updateGradegrade(Map params) {
        return gradeMapper.updateGradegrade(params);
    }

    @Override
    public List<Map> selectList() {
        return gradeMapper.selectList();
    }
}
