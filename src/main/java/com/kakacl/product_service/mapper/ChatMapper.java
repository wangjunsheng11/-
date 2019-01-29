package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
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

    // 获取 提交别人希望申请我为好友的人和我忽略的好友
    /*({
            "<script>",
            "select",
            "*",
            "from zzf_user_friends",
            "where del_flag = 0 AND friend_id = #{user_id} AND status in",
            "<foreach collection='stu' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })*/
    @Select("SELECT * FROM zzf_user_friends WHERE del_flag = 0 AND (`status` = #{status_01} OR `status` = #{status_02}) AND friend_id = #{user_id}")
    List<Map> findAddFriends(Map params);

    @Update("UPDATE zzf_user_chat_history SET update_by=#{update_by}, update_time=#{update_time}, read_status = #{read_status} WHERE (id=#{id})")
    boolean updateInfo(Map params);

    @Insert("INSERT INTO zzf_user_friends (id, my_id, friend_id, group_name, create_time, create_by) VALUES (#{id}, #{my_id}, #{friend_id}, #{group_name}, #{create_time}, #{create_by})")
    boolean addFriend(Map params);

    @Update("update zzf_user_friends set del_flag = #{del_flag} where friend_id = #{friend_id} and my_id = #{my_id}")
    boolean updateFriend(Map params);

    // 同意申请者添加好友
    @Update("UPDATE zzf_user_friends SET `status` = #{status_01}  WHERE del_flag = 0 AND my_id = #{friend_id} AND friend_id = #{user_id}")
    boolean agreeOne(Map params);

    @Select("SELECT * FROM zzf_user_chat_history WHERE content like '%${search_key}%' and (to_id = #{user_id} or send_id = #{user_id}) AND del_flag = 0")
    List<Map> findMessages(Map params);

    @Select("SELECT DISTINCT group_name FROM zzf_user_friends WHERE my_id = #{user_id} AND del_flag = 0")
    List<Map> findGroup(Map params);
}
