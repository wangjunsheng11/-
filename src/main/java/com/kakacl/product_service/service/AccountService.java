package com.kakacl.product_service.service;

import com.github.pagehelper.PageInfo;
import com.kakacl.product_service.domain.Account;

import java.util.List;
import java.util.Map;

public interface AccountService {

    Map selectById(java.util.Map params);

    Map selectByPhone(java.util.Map params);

    List<Map> selectByPhoneORKakaNum(java.util.Map params);

    PageInfo<Map> selectByPageAndSelections(int currentPage, int pageSize);
}
