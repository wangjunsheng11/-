package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.JobTrajectoryAutoMapper;
import com.kakacl.product_service.service.JobTrajectoryAutoService;
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
public class JobTrajectoryAutoServiceImpl implements JobTrajectoryAutoService {

    @Autowired
    private JobTrajectoryAutoMapper jobTrajectoryAutoMapper;


    @Override
    public List<Map> findListo0_50(Map params) {
        return jobTrajectoryAutoMapper.findListo0_50(params);
    }

    @Override
    public Map findUserInfoByPhone(Map params) {
        return jobTrajectoryAutoMapper.findUserInfoByPhone(params);
    }

    @Override
    public Map findUserBaseInfo(Map params) {
        return jobTrajectoryAutoMapper.findUserBaseInfo(params);
    }

    @Override
    public Map findPhoneBuUserInfo(Map params) {
        return jobTrajectoryAutoMapper.findPhoneBuUserInfo(params);
    }

    @Override
    public boolean insertOne(Map params) {
        return jobTrajectoryAutoMapper.insertOne(params);
    }
}
