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

    boolean insertPingCard(Map params);
}
