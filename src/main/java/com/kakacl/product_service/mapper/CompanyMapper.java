package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

public interface CompanyMapper {

    Map selectListEntity(Map<String, Object> params);

    @Select("select count(1) from store_company")
    int selectCountTotal();

    @Update("update store_company set province = #{province}, city = #{city} WHERE id=#{id}")
    boolean updateProvinceCityById(Map<String, Object> params);

    /*
     * 根据主键获取公司数据
     *
     * @author wangwei
     * @date 2019/2/10
     * @param params
     * @return java.util.Map
     */
    @Select("select * from store_company where id = #{id}")
    Map selectById(Map params);
}
