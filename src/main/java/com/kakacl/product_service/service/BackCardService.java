package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

public interface BackCardService {

    /*
     * 查询银行规则
     *
     * @author wangwei
     * @date 2019/2/10
     * @param params
     * @return java.util.Map
     */
    List<Map> selectBankRule(Map params);

    /*
     * 设置银行规则
     *
     * @author wangwei
     * @date 2019/2/10
     * @param params
     * @return java.util.Map
     */
    boolean insertBankRule(Map params);

    boolean addCard(Map params);

    List<Map> selectList(Map params);

    List<Map> selectBackCarcdExist(Map params);

    boolean updateById(Map params);

    boolean updateByUserIdAndBackcardNum(Map params);

    List<Map> selectUSerByIdcard(Map params);

    List<Map> selectIncomeByUserid(Map params);

    Map selectIncomeDetail(Map params);
}
