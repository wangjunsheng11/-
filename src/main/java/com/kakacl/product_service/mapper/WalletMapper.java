package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.*;

public interface WalletMapper {

    /*
     * 查询10条数据没有钱包的用户数据
     *
     * @author wangwei
     * @date 2019/2/11
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT id, user_name FROM zzf_user_info WHERE zzf_user_info.id NOT IN (SELECT user_id FROM zzf_user_wallet) ORDER BY id ASC LIMIT 0, 10")
    List<Map> selectListLimit10(Map params);

    /*
     * 插入一条钱包数据
     *
     * @author wangwei
     * @date 2019/2/11
      * @param params
     * @return boolean
     */
    @Insert("INSERT INTO zzf_user_wallet (id, user_id, create_time, create_by) VALUES (#{id}, #{user_id}, #{create_time}, #{create_by})")
    boolean insertOne(Map params);

}
