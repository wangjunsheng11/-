package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.*;

/*
 * 用户消息情绪分析
 *
 * @author wangwei
 * @date 2019/2/12
  * @param null
 * @return
 */
public interface ChatMoodAnalysisMapper {

    /*
     * 获取10条，没有分析情绪的用户消息, 已经分析过的情绪，逻辑删除，不做分析
     *
     * @author wangwei
     * @date 2019/2/12
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_user_chat_history WHERE id NOT IN (select chat_id from zzf_chat_mood_analysis) ORDER BY id ASC LIMIT 0, 10")
    List<Map> selectListLimit10(Map params);

    /*
     * 保存分析后的消息情绪
     *
     * @author wangwei
     * @date 2019/2/12
      * @param params
     * @return boolean
     */
    @Insert("INSERT INTO zzf_chat_mood_analysis (id, chat_id, mood) VALUES (#{id}, #{chat_id}, #{mood})")
    boolean insert(Map params);

}
