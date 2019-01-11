package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

public interface BackCardService {

    boolean addCard(Map params);

    List<Map> selectList(Map params);

    List<Map> selectBackCarcdExist(Map params);

    boolean updateById(Map params);

    boolean updateByUserIdAndBackcardNum(Map params);

    List<Map> selectUSerByIdcard(Map params);

    List<Map> selectIncomeByUserid(Map params);

    Map selectIncomeDetail(Map params);
}
