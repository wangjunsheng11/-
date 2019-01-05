package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

public interface ADService {
    public List<Map<String, String>> selectAD(Map params);
}
