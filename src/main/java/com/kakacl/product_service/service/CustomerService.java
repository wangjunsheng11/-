package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    /*
     * 获取10条没有客服的用户
     *
     * @author wangwei
     * @date 2019/2/11
     * @param params
     * @return java.util.List<java.util.Map>
     */
    List<Map> selectListLimit10(Map params);

    /*
     * 给用户添加一个客服
     *
     * @author wangwei
     * @date 2019/2/11
     * @param params
     * @return boolean
     */
    boolean insertOne(Map params);
}
