package net.xdclass.product_service.mapper;

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
}
