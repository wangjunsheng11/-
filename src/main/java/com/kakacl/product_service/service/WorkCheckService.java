package com.kakacl.product_service.service;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface WorkCheckService {
    Map findWorkCheckById(Map params);

    Map findWorkDayTime(Map params);

    Map findWorkNight(Map params);

    boolean updateWorkTime(Map params);

    boolean insertWorkStatus(Map params);
}
