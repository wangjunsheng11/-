package com.kakacl.product_service.service;

import java.util.*;

public interface ProtocolService {

    /*
     *
     * 获取协议组
     * @author wangwei
     * @date 2019/1/14
      * @param patams
     * @return java.util.List<java.util.Map>
     */
    List<Map> findGroup(Map params);

    /*
     *
     * 获取协议
     * @author wangwei
     * @date 2019/1/14
      * @param patams
     * @return java.util.List<java.util.Map>
     */
    List<Map> findProtocol(Map params);
}
