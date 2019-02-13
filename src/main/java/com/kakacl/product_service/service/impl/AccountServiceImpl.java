package com.kakacl.product_service.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kakacl.product_service.domain.Account;
import com.kakacl.product_service.mapper.AccountMapper;
import com.kakacl.product_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl
        implements
        AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<Map> findDetailtList(Map params) {
        return accountMapper.findDetailtList(params);
    }

    @Override
    public Map selectHistoryByYserId(Map params) {
        return accountMapper.selectHistoryByYserId(params);
    }

    @Override
    public Map selectById(Map params) {
        return accountMapper.selectByUserid(params);
    }

    @Override
    public Map selectByPhone(Map params) {
        return accountMapper.selectByPhone(params);
    }

    @Override
    public List<Map> selectByPhoneORKakaNum(Map params) {
        return accountMapper.selectByPhoneORKakaNum(params);
    }

    @Override
    public PageInfo<Map> selectByPageAndSelections(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<Map> docs = accountMapper.selectByPageAndSelections(null);
        PageInfo<Map> pageInfo = new PageInfo<Map>(docs);
        return pageInfo;
    }

    @Override
    public boolean updateInfo(Map params) {
        return accountMapper.updateInfo(params);
    }

    @Override
    public boolean updateHead(Map params) {
        return accountMapper.updateHead(params);
    }
}
