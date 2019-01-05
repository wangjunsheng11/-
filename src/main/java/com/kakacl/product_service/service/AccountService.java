package com.kakacl.product_service.service;

import com.github.pagehelper.PageInfo;
import com.kakacl.product_service.domain.Account;

import java.util.Map;

public interface AccountService {

    Map selectById(java.util.Map params);

    PageInfo<Account> selectByPageAndSelections(int currentPage, int pageSize);
}
