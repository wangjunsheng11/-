package com.kakacl.product_service.service;

import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description CAS单点登录
 * @date 2018-01-09
 */
public interface CasAccountService {

    boolean insert(Map<String, Object> params);

    java.util.Map selectOne(java.util.Map<String, Object> params);

    java.util.Map selectOneByPhonenum(Map<String, Object> params);

    java.util.Map selectOneByKakanum(Map<String, Object> params);

    int updateOnePassById(Map<String, Object> params);
}
