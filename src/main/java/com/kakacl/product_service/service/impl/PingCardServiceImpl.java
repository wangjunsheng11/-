package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.PingCardMapper;
import com.kakacl.product_service.service.PingCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangwei<br />
 * @Description: <br/>
 * @date 2019/2/4 11:46<br/>
 * ${TAGS}
 */
@Service
public class PingCardServiceImpl implements PingCardService {

    @Autowired
    private PingCardMapper pingCardMapper;

    @Override
    public boolean insertPingCardScopeRule(Map params) {
        return pingCardMapper.insertPingCardScopeRule(params);
    }

    @Override
    public List<Map> selectCompanyLocation(Map params) {
        return pingCardMapper.selectCompanyLocation(params);
    }

    @Override
    public Map slectLastPingType(Map params) {
        return pingCardMapper.slectLastPingType(params);
    }

    @Override
    public Map slectLastPingType2(Map params) {
        return pingCardMapper.slectLastPingType2(params);

    }

    @Override
    public Map slectLastPingTypeOfNight(Map params) {
        return pingCardMapper.slectLastPingTypeOfNight(params);
    }

    @Override
    public Integer selectCountPing(Map params) {
        return pingCardMapper.selectCountPing(params);
    }

    @Override
    public Integer selectCountPingOfNight(Map params) {
        return pingCardMapper.selectCountPingOfNight(params);
    }

    @Override
    public boolean insertPingCard(Map params) {
        return pingCardMapper.insertPingCard(params);
    }
}
