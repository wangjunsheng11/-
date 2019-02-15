package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.ChatMapper;
import com.kakacl.product_service.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 聊天服务实现
 * @date 2019-01-28
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMapper chatMapper;

    @Override
    public boolean insert(Map params) {
        return chatMapper.insert(params);
    }

    @Override
    public List<Map> findInfoBySendid(Map params) {
        return chatMapper.findInfoBySendid(params);
    }

    @Override
    public List<Map> findAddFriends(Map params) {
        return chatMapper.findAddFriends(params);
    }

    @Override
    public boolean updateInfo(Map params) {
        return chatMapper.updateInfo(params);
    }

    @Override
    public boolean addFriend(Map params) {
        return chatMapper.addFriend(params);
    }

    @Override
    public List<Map> findFriends(Map params) {
        return chatMapper.findFriends(params);
    }

    @Override
    public boolean updateFriend(Map params) {
        return chatMapper.updateFriend(params);
    }

    @Override
    public boolean agreeOne(Map params) {
        return chatMapper.agreeOne(params);
    }

    @Override
    @Transient
    public boolean agreeOneFriendAndMy(Map params) {
        boolean result = chatMapper.agreeOneFriendAndMy(params);
        return result;
    }

    @Override
    public List<Map> findMessages(Map params) {
        return chatMapper.findMessages(params);
    }

    @Override
    public List<Map> findMessageByKey(Map params) {
        return chatMapper.findMessageByKey(params);
    }

    @Override
    public List<Map> findGroup(Map params) {
        return chatMapper.findGroup(params);
    }
}
