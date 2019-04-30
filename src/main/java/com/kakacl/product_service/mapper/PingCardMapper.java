package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author wangwei<br />
 * @Description: <br/>
 * @date 2019/2/4 11:31<br/>
 * ${TAGS}
 */
public interface PingCardMapper {

    /*
     * 设置公司打卡位置
     *
     * @author wangwei
     * @date 2019/2/12
      * @param params
     * @return boolean
     */
    @Insert("INSERT INTO zzf_pingcard_scope_rule (id, company_id, `order`, longitude, latitude, effective_radius) VALUES (#{id}, #{company_id}, #{order}, #{longitude}, #{latitude}, #{effective_radius})")
    boolean insertPingCardScopeRule(Map params);

    /*
     * 根据公司获取打卡位置
     *
     * @author wangwei
     * @date 2019/2/11
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_pingcard_scope_rule WHERE company_id = #{company_id} AND del_flag = 0")
    List<Map> selectCompanyLocation(Map params);

    /*
     * 获取用户最后一次打卡数据
     *
     * @author wangwei
     * @date 2019/2/10
      * @param params
     * @return java.util.Map
     */
    @Select({"SELECT * FROM zzf_ping_card_history WHERE user_id = #{user_id} AND del_flag = 0 ORDER BY id DESC LIMIT 0, 1"})
    Map slectLastPingType(Map params);

    @Select({"SELECT * FROM zzf_ping_card_history WHERE user_id = #{user_id} AND FROM_UNIXTIME(ping_time, '%Y%m%d') >= #{time} AND del_flag = 0 ORDER BY id DESC LIMIT 0, 1"})
    Map slectLastPingType2(Map params);

    @Select({"SELECT * FROM zzf_ping_card_history WHERE user_id = #{user_id} AND  FROM_UNIXTIME(ping_time, '%Y%m%d%H') >= #{time} AND FROM_UNIXTIME(ping_time, '%Y%m%d%h') < #{time1} AND del_flag = 0 ORDER BY id DESC LIMIT 0, 1"})
    Map slectLastPingTypeOfNight(Map params);

    //查询当天打卡的记录条数（白班）
    @Select("SELECT COUNT(id) FROM zzf_ping_card_history WHERE user_id = #{user_id} AND FROM_UNIXTIME(ping_time, '%Y%m%d') >= #{time}")
    Integer selectCountPing(Map params);

    //查询当天打卡的记录条数（夜班）
    @Select("SELECT COUNT(id) FROM zzf_ping_card_history WHERE user_id = #{user_id} AND FROM_UNIXTIME(ping_time, '%Y%m%d%H') BETWEEN #{time} AND  #{time1}")
    Integer selectCountPingOfNight(Map params);

    /**
     * 用户打卡
     * @param params
     * @return
     */
    @Insert("INSERT INTO zzf_ping_card_history " +
            "(id, user_id, ping_time, longitude, latitude, company_id, company_name, create_time, ping_type, create_by) " +
            "VALUES (#{id}, #{user_id}, #{ping_time}, #{longitude}, #{latitude}, #{company_id}, #{company_name}, #{create_time}, #{ping_type}, #{create_by})")
    boolean insertPingCard(Map params);

    @Update("UPDATE zzf_ping_card_history SET ping_time=#{ping_time} WHERE id=#{id}")
    boolean updatePingCard(Map params);


}
