package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import java.util.*;

public interface CustomerServiceMapper {

    /*
     * 获取10条没有客服的用户
     *
     * @author wangwei
     * @date 2019/2/11
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_user_info WHERE zzf_user_info.id NOT IN (SELECT my_id FROM zzf_user_friends WHERE group_name = #{group_name}) ORDER BY id ASC LIMIT 0, 10")
    List<Map> selectListLimit10(Map params);

    /*
     * 给用户添加一个客服
     *
     * @author wangwei
     * @date 2019/2/11
      * @param params
     * @return boolean
     */
    @Insert("INSERT INTO zzf_user_friends (id, my_id, friend_id, group_name, `status`, create_time, create_by) VALUES (#{id}, #{my_id}, #{friend_id}, #{group_name}, #{status}, #{create_time}, #{create_by})")
    boolean insertOne(Map params);

}
