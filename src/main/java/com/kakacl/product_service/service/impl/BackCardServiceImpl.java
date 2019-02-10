package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.BackCardMapper;
import com.kakacl.product_service.service.BackCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 银行卡实现类
 * @date 2019-01-11
 */
@Service
public class BackCardServiceImpl implements BackCardService {

    @Autowired
    private BackCardMapper backCardMapper;

    @Override
    public List<Map> selectBankRule(Map params) {
        return backCardMapper.selectBankRule(params);
    }

    @Override
    public boolean insertBankRule(Map params) {
        return backCardMapper.insertBankRule(params);
    }

    @Override
    public boolean addCard(Map params) {
        return backCardMapper.addCard(params);
    }

    @Override
    public List<Map> selectList(Map params) {
        return backCardMapper.selectList(params);
    }

    @Override
    public List<Map> selectBackCarcdExist(Map params) {
        return backCardMapper.selectBackCarcdExist(params);
    }

    @Override
    public boolean updateById(Map params) {
        return backCardMapper.updateById(params);
    }

    @Override
    public boolean updateByUserIdAndBackcardNum(Map params) {
        return backCardMapper.updateByUserIdAndBackcardNum(params);
    }

    @Override
    public List<Map> selectUSerByIdcard(Map params) {
        return backCardMapper.selectUSerByIdcard(params);
    }

    @Override
    public List<Map> selectIncomeByUserid(Map params) {
        return backCardMapper.selectIncomeByUserid(params);
    }

    @Override
    public Map selectIncomeDetail(Map params) {
        return backCardMapper.selectIncomeDetail(params);
    }
}
