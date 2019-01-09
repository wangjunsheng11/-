package com.kakacl.product_service.service;

import java.util.*;

public interface AbilityService {

    List<Map> selectByUserid(java.util.Map params);

    boolean updateOne(Map params);
}
