package com.kakacl.product_service.service;

import java.util.Map;

public interface TntegralService {

    /*
     * 获取当前用户总积分
     *
     * @author wangwei
     * @date 2019/1/24
      * @param params
     * @return java.util.Map
     */
    Map selectOneByUserid(Map params);

    /*
     * 用户积分的记录表
     *
     * @author wangwei
     * @date 2019/1/24
      * @param params
     * @return boolean
     */
    boolean insertOne(Map params);
}
