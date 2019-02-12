package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

public interface ChatMoodAnalysisService {

    /*
     * 获取10条，没有分析情绪的用户消息, 已经分析过的情绪，逻辑删除，不做分析
     *
     * @author wangwei
     * @date 2019/2/12
     * @param params
     * @return java.util.List<java.util.Map>
     */
    List<Map> selectListLimit10(Map params);

    /*
     * 保存分析后的消息情绪
     *
     * @author wangwei
     * @date 2019/2/12
     * @param params
     * @return boolean
     */
    boolean insert(Map params);
}
