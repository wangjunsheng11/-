package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.*;

/*
 * 聊天mapper
 *
 * @author wangwei
 * @date 2019/1/28
  * @param null
 * @return
 */
public interface ChatMapper {

    @Insert("INSERT INTO zzf_user_chat_history (id, send_id, to_id, content, title, create_by, create_time) VALUES (#{id}, #{send_id}, #{to_id}, #{content}, #{title}, #{create_by}, #{create_time})")
    boolean insert(Map params);

    @Select("select * from zzf_user_chat_history where del_flag = 0 and to_id = #{to_id} and send_id = #{send_id} and del_flag = #{del_flag}")
    List<Map> findInfoBySendid(Map params);

    @Update("UPDATE zzf_user_chat_history SET update_by=#{update_by}, update_time=#{update_time}, read_status = #{read_status} WHERE (id=#{id})")
    boolean updateInfo(Map params);

}
