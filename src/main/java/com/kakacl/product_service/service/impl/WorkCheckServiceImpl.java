package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mappers.WorkCheckMapper;
import com.kakacl.product_service.service.WorkCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class WorkCheckServiceImpl implements WorkCheckService {

    @Autowired
    private WorkCheckMapper workCheckMapper;
    @Override
    public Map findWorkCheckById(Map params) {
        return workCheckMapper.findWorkCheckById(params);
    }

    @Override
    public Map findWorkDayTime(Map params) {
        return workCheckMapper.findWorkDayTime(params);
    }

    @Override
    public Map findWorkNight(Map params) {
        return workCheckMapper.findWorkNight(params);
    }

    @Override
    public boolean updateWorkTime(Map params) {
        return workCheckMapper.updateWorkTime(params);
    }

    @Override
    public boolean insertWorkStatus(Map params) {
        return workCheckMapper.insertWorkStatus(params);
    }
}
