package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.ChatMoodAnalysisMapper;
import com.kakacl.product_service.service.ChatMoodAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 消息情绪分析
 * @date 2019-02-12
 */
@Service
public class ChatMoodAnalysisServiceImpl implements ChatMoodAnalysisService {

    @Autowired
    private ChatMoodAnalysisMapper chatMoodAnalysisMapper;

    @Override
    public List<Map> selectListLimit10(Map params) {
        return chatMoodAnalysisMapper.selectListLimit10(params);
    }

    @Override
    public boolean insert(Map params) {
        return chatMoodAnalysisMapper.insert(params);
    }
}
