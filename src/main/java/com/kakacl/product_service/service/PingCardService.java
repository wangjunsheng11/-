package com.kakacl.product_service.service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei<br />
 * @Description: <br/>
 * @date 2019/2/4 11:45<br/>
 * ${TAGS}
 */
public interface PingCardService {

    /*
     * 设置公司打卡位置
     *
     * @author wangwei
     * @date 2019/2/12
     * @param params
     * @return boolean
     */
    boolean insertPingCardScopeRule(Map params);

    /*
     * 根据公司获取打卡位置
     *
     * @author wangwei
     * @date 2019/2/11
     * @param params
     * @return java.util.List<java.util.Map>
     */
    List<Map> selectCompanyLocation(Map params);

    /*
     * 获取用户最后一次打卡数据
     *
     * @author wangwei
     * @date 2019/2/10
     * @param params
     * @return java.util.Map
     */
    Map slectLastPingType(Map params);

    Map slectLastPingType2(Map params);

    Map slectLastPingTypeOfNight(Map params);

    //查询该员工当天的打卡次数(白班)
    Integer selectCountPing(Map params);

    //查询当天打卡的记录条数（夜班）
    Integer selectCountPingOfNight(Map params);

    boolean insertPingCard(Map params);
}
