package com.kakacl.product_service.service;

import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;

public interface UserDataService {

    List<Map> findListByUserid(Map params);

    PageInfo<Map> findPaysByUserid(Map params);

    List<Map> findPayDetail(Map params);
}
