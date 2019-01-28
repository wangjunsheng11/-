package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.ChatMapper;
import com.kakacl.product_service.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean updateInfo(Map params) {
        return chatMapper.updateInfo(params);
    }
}
