package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;

import java.util.Map;

/**
 * @author wangwei<br />
 * @Description: <br/>
 * @date 2019/2/4 11:31<br/>
 * ${TAGS}
 */
public interface PingCardMapper {

    /**
     * 用户打卡
     * @param params
     * @return
     */
    @Insert("INSERT INTO zzf_ping_card_history " +
            "(id, user_id, ping_time, longitude, latitude, company_id, company_name, create_time, create_by) " +
            "VALUES (#{id}, #{user_id}, #{ping_time}, #{longitude}, #{latitude}, #{company_id}, #{company_name}, #{create_time}, #{create_by})")
    boolean insertPingCard(Map params);
}
