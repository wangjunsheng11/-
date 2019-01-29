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

    // 获取 提交别人希望申请我为好友的人和我忽略的好友
    List<Map> findAddFriends(Map params);

    boolean updateInfo(Map params);

    boolean addFriend(Map params);

    boolean updateFriend(Map params);

    // 同意申请者添加好友
    boolean agreeOne(Map params);

    List<Map> findMessages(Map params);

    List<Map> findGroup(Map params);
}
