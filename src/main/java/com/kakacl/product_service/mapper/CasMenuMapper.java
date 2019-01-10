package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 单点登录菜单mapper
 * @date 2019-01-10
 */
public interface CasMenuMapper {

    /*
     *
     * 根据用户主键查询用户的菜单
     * @author wangwei
     * @date 2019/1/10
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM cas_menu WHERE sys_type = #{sys_type} AND id in (SELECT menuid FROM cas_relation WHERE roleid in (SELECT roleid from zzf_user_info WHERE id = #{user_id}))")
    List<Map> selectListByUserid(Map params);
}
