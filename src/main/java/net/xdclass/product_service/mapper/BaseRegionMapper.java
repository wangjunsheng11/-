package net.xdclass.product_service.mapper;

import org.apache.ibatis.annotations.Select;

public interface BaseRegionMapper {

    @Select("select * from store_base_region where id = #{parentId}")
    java.util.Map selectOne(java.util.Map<String, Object> params);

}
