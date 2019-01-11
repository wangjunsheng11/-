package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

public interface BackCardService {

    boolean addCard(Map params);

    List<Map> selectList(Map params);

    List<Map> selectBackCarcdExist(Map params);

    boolean updateById(Map params);

    /*
     *
     * 根据身份证号码查询用户信息
     * @author wangwei
     * @date 2019/1/11
     * @param params
     * @return java.util.List<java.util.Map>
     */
    List<Map> selectUSerByIdcard(Map params);

    List<Map> selectIncomeByUserid(Map params);
}
