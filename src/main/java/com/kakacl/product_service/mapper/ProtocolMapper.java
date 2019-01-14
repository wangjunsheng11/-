package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ProtocolMapper {

    /*
     *
     * 添加协议
     * @author wangwei
     * @date 2019/1/14
     * @param params
     * @return boolean
     */
    @Insert("INSERT INTO zzf_protocol (id, title, content, group_name, status, order, remake, create_time, create_by) VALUES (${id}, ${title}, ${content}, ${group_name}, ${status}, ${order}, ${remake}, ${create_time}, ${create_by})")
    boolean  addProtocol(Map params);

    /*
     *
     * 获取协议组
     * @author wangwei
     * @date 2019/1/14
     * @param patams
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_protocol WHERE group_name = ${group_name}")
    List<Map> findGroup(Map patams);

    /*
     *
     * 获取协议
     * @author wangwei
     * @date 2019/1/14
     * @param patams
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_protocol")
    List<Map> findProtocol(Map patams);
}
