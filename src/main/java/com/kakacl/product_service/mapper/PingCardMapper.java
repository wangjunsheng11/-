package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author wangwei<br />
 * @Description: <br/>
 * @date 2019/2/4 11:31<br/>
 * ${TAGS}
 */
public interface PingCardMapper {

    /*
     * 获取用户最后一次打卡数据
     *
     * @author wangwei
     * @date 2019/2/10
      * @param params
     * @return java.util.Map
     */
    @Select("SELECT * FROM zzf_ping_card_history WHERE user_id = #{user_id} AND del_flag = 0 ORDER BY id DESC LIMIT 0, 1")
    Map slectLastPingType(Map params);

    /**
     * 用户打卡
     * @param params
     * @return
     */
    @Insert("INSERT INTO zzf_ping_card_history " +
            "(id, user_id, ping_time, longitude, latitude, company_id, company_name, create_time, ping_type, create_by) " +
            "VALUES (#{id}, #{user_id}, #{ping_time}, #{longitude}, #{latitude}, #{company_id}, #{company_name}, #{create_time}, #{ping_type}, #{create_by})")
    boolean insertPingCard(Map params);
}
