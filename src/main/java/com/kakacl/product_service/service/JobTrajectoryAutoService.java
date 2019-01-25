package com.kakacl.product_service.service;


import java.util.List;
import java.util.Map;

public interface JobTrajectoryAutoService {

    /*
     * 查询 0-50条昨天入职的人员信息
     *
     * @author wangwei
     * @date 2019/1/25
     * @param params
     * @return java.util.List<java.util.Map>
     */
    List<Map> findListo0_50(Map params);

    /*
     * 根据求职者手机号码查询求职者信息
     *
     * @author wangwei
     * @date 2019/1/25
     * @param params
     * @return java.util.Map
     */
    Map findUserInfoByPhone(Map params);

    /*
     * 根据用户信息用户状态
     *
     * params user_id phone  company_id
     * @author wangwei
     * @date 2019/1/25
     * @param params
     * @return java.util.Map
     */
    Map findUserBaseInfo(Map params);

    /*
     * 根据求职者的手机号码查询用于的基础信息
     *
     * @author wangwei
     * @date 2019/1/25
      * @param params
     * @return java.util.Map
     */
    Map findPhoneBuUserInfo(Map params);

    /*
     * 添加用户轨迹到周周发系统
     *
     * @author wangwei
     * @date 2019/1/25
     * @param params
     * @return boolean
     */
    boolean insertOne(Map params);
}
