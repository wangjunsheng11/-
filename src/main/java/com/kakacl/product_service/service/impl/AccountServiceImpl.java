package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.AccountMapper;
import com.kakacl.product_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;


    @Override
    public Map selectById(Map params) {
        return accountMapper.selectById(params);
    }
}
