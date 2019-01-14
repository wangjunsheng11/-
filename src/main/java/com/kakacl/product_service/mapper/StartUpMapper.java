package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface StartUpMapper {

    @Select("SELECT * FROM zzf_starting_images_ad WHERE del_flag = 0")
    List<Map> selectList(Map params);
}
