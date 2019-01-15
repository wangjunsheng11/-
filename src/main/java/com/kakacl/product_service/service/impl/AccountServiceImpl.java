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
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Map selectById(Map params) {
        return accountMapper.selectByUserid(params);
    }

    @Override
    public PageInfo<Map> selectByPageAndSelections(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<Map> docs = accountMapper.selectByPageAndSelections(null);
        PageInfo<Map> pageInfo = new PageInfo<Map>(docs);
        return pageInfo;
    }
}
