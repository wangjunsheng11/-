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

    // 根据群组查询自己的好友
    List<Map> findFriends(Map params);

    boolean updateFriend(Map params);

    // 同意申请者添加我为好友；我添加一个好友，设置好友同意
    boolean agreeOne(Map params);

    // 同意好友添加我为好友
    boolean agreeOneFriendAndMy(Map params);

    List<Map> findMessages(Map params);

    // 输入对方姓名、咔咔号、电话号码、聊天记录关键词可进行查找相关信息
    List<Map> findMessageByKey(Map params);

    List<Map> findGroup(Map params);
}
