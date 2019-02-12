package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.BankCardMapper;
import com.kakacl.product_service.service.BankCardService;
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
public class BankCardServiceImpl implements BankCardService {

    @Autowired
    private BankCardMapper bankCardMapper;

    @Override
    public List<Map> selectBankRule(Map params) {
        return bankCardMapper.selectBankRule(params);
    }

    @Override
    public boolean setBankCardMain(Map params) {
        return bankCardMapper.setBankCardMain(params);
    }

    @Override
    public boolean insertBankRule(Map params) {
        return bankCardMapper.insertBankRule(params);
    }

    @Override
    public boolean addCard(Map params) {
        return bankCardMapper.addCard(params);
    }

    @Override
    public List<Map> selectList(Map params) {
        return bankCardMapper.selectList(params);
    }

    @Override
    public List<Map> selectBackCarcdExist(Map params) {
        return bankCardMapper.selectBackCarcdExist(params);
    }

    @Override
    public boolean updateById(Map params) {
        return bankCardMapper.updateById(params);
    }

    @Override
    public boolean updateByUserIdAndBackcardNum(Map params) {
        return bankCardMapper.updateByUserIdAndBackcardNum(params);
    }

    @Override
    public List<Map> selectUSerByIdcard(Map params) {
        return bankCardMapper.selectUSerByIdcard(params);
    }

    @Override
    public List<Map> selectIncomeByUserid(Map params) {
        return bankCardMapper.selectIncomeByUserid(params);
    }

    @Override
    public Map selectIncomeDetail(Map params) {
        return bankCardMapper.selectIncomeDetail(params);
    }
}
