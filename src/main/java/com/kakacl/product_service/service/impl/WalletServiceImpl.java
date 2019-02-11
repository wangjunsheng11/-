package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.WalletMapper;
import com.kakacl.product_service.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 钱包服务
 * @date 2019-02-11
 */
@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletMapper walletMapper;

    @Override
    public List<Map> selectListLimit10(Map params) {
        return walletMapper.selectListLimit10(params);
    }

    @Override
    public boolean insertOne(Map params) {
        return walletMapper.insertOne(params);
    }
}
