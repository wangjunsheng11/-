package com.kakacl.product_service.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 我的资料
 * @date 2010-01-10
 */
public interface UserDataMapper {

    /*
     *
     * 根据用户主键查询用户职业历史轨迹集合数据
     * @author wangwei
     * @date 2019/1/10
      * @param params map集合
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT sc.id as company_id, sc.company_name, sc.image, zueh.orbit_id, zueh.entry_time, zueh.resignation_time, zueh.work_status FROM zzf_user_employee_hitory zueh " +
            "LEFT JOIN store_company sc ON zueh.company_id = sc.id " +
            "WHERE zueh.user_id = #{user_id} ORDER BY zueh.entry_time DESC")
    List<Map> findListByUserid(Map params);

    /*
     *
     * 根据用户主键查询用户薪资列表信息
     * @author wangwei
     * @date 2019/1/10
      * @param
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_user_pay WHERE user_id = #{user_id} AND company_id = #{company_id} AND del_flag = 0 ORDER BY produce_time DESC")
    List<Map> findPaysByUserid(Map params);

    /*
     *
     * 根据pay_id查询支付详情
     * @author wangwei
     * @date 2019/1/10
      * @param params
     * @return java.util.List<java.util.Map>
     */
    @Select("SELECT * FROM zzf_user_pay_details WHERE del_flag = 0 AND pay_id = #{pay_id}")
    List<Map> findPayDetail(Map params);
}
