package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

public interface AppVersionService {

    // 根据参数获取版本
    List<Map> findNowVersion(Map params);

}
