package com.kakacl.product_service.service;

import java.util.*;

public interface CompanyService {

    /*
     * 根据主键获取公司数据
     *
     * @author wangwei
     * @date 2019/2/10
      * @param params
     * @return java.util.Map
     */
    Map selectById(Map params);
}
