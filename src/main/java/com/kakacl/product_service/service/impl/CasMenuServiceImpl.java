package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.CasMenuMapper;
import com.kakacl.product_service.service.CasMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 单点登录菜单实现
 * @date 2019-01-10
 */
@Service
public class CasMenuServiceImpl implements CasMenuService {

    @Autowired
    private CasMenuMapper casMenuMapper;


    @Override
    public List<Map> selectListByUserid(Map params) {
        return casMenuMapper.selectListByUserid(params);
    }
}
