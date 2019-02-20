package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.*;

public interface AppVersionMapper {

    // 根据参数获取版本
   /* @Select("({" +
            "<script>" +
            "select" +
            " * " +
            "from zzf_app_version " +
            "where 1 = 1 " +
            "<if test=type != null>" +
            "AND type = #{type}" +
            "</if> AND del_flag = 0 ORDER BY create_time DESC LIMIT 0, 1" +
            "</script>" +
            "})")*/
   @Select("select * FROM zzf_app_version WHERE 1 = 1 AND type = #{type} AND del_flag = 0 ORDER BY create_time DESC LIMIT 0, 1")
    List<Map> findNowVersion(Map params);

}
