package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

/*
 * 聊天服务service
 *
 * @author wangwei
 * @date 2019/1/28
 */
public interface ChatService {

    boolean insert(Map params);

    List<Map> findInfoBySendid(Map params);

    boolean updateInfo(Map params);

    boolean addFriend(Map params);

    boolean updateFriend(Map params);

    List<Map> findMessages(Map params);

    List<Map> findGroup(Map params);
}
