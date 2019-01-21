package com.kakacl.product_service.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kakacl.product_service.mapper.UserDataMapper;
import com.kakacl.product_service.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 我的资料实现类
 * @date 2010-01-10
 */
@Service
public class UserDataServiceImpl implements UserDataService {

    @Autowired
    private UserDataMapper userDataMapper;

    @Override
    public List<Map> findListByUserid(Map params) {
        return userDataMapper.findListByUserid(params);
    }

    @Override
    public PageInfo<Map> findListByToken(Map params) {
        PageHelper.startPage((Integer)params.get("currentPage"), (Integer)params.get("pageSize"));
        List<Map> userPays = userDataMapper.findListByUserid(params);
        PageInfo<Map> pageInfo = new PageInfo<Map>(userPays);
        return pageInfo;
    }

    @Override
    public PageInfo<Map> findPaysByUserid(Map params) {
        PageHelper.startPage((Integer)params.get("currentPage"), (Integer)params.get("pageSize"));
        List<Map> userPays = userDataMapper.findPaysByUserid(params);
        PageInfo<Map> pageInfo = new PageInfo<Map>(userPays);
        return pageInfo;
    }

    @Override
    public List<Map> findPayDetail(Map params) {
        return userDataMapper.findPayDetail(params);
    }
}
