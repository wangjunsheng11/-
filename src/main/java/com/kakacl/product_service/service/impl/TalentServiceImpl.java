package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.TalentMapper;
import com.kakacl.product_service.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 天赋
 * @date 2019-01-09
 */
@Service
public class TalentServiceImpl implements TalentService {

    @Autowired
    private TalentMapper talentMapper;

    @Override
    public List<Map> selectList(Map params) {
        return talentMapper.selectList(params);
    }
}
