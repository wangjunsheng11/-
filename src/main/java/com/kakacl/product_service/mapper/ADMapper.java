package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ADMapper {

    @Select("SELECT 'order', path, target_path, del_flag FROM zzk_images_ad WHERE del_flag = 0")
    List<Map<String, String>> selectAD(Map params);
}
