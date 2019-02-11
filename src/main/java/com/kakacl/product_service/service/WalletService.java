package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

public interface WalletService {

    /*
     * 查询10条数据没有钱包的用户数据
     *
     * @author wangwei
     * @date 2019/2/11
     * @param params
     * @return java.util.List<java.util.Map>
     */
    List<Map> selectListLimit10(Map params);

    /*
     * 插入一条钱包数据
     *
     * @author wangwei
     * @date 2019/2/11
     * @param params
     * @return boolean
     */
    boolean insertOne(Map params);
}
