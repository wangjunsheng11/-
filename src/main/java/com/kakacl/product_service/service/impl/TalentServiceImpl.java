package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.mapper.TalentMapper;
import com.kakacl.product_service.service.TalentService;
import com.kakacl.product_service.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 天赋
 * @date 2019-01-09
 */
@Service
public class TalentServiceImpl implements TalentService {

    @Autowired
    private TalentMapper talentMapper;

    @Override
    public List<Map> selectList(Map params) {
        return talentMapper.selectList(params);
    }

    @Override
    public List<Map> selectListTalent(Map params) {
        return talentMapper.selectListTalent(params);
    }

    @Override
    public Map selectTalentById(Map params) {
        return talentMapper.selectTalentById(params);
    }

    @Override
    @Transient
    public boolean edit(Map params) {
        // 先删除 - 增加
        talentMapper.delete(params);
        String[] params_ids = (String[])params.get("param_ids");
        for (int i = 0; i < params_ids.length; i++) {
            String tmp = params_ids[i];
            params.put("id", tmp);
            Map data = talentMapper.selectTalentById(params);
            params.put("id", IDUtils.genHadId());
            params.put("order", i+ 1);
            params.put("user_id", params.get("user_id") + "");
            params.put("name", data.get("name")+ "");
            params.put("img_path", data.get("img_path")+ "");
            params.put("remark", data.get("remark")+ "");
            params.put("create_date", System.currentTimeMillis() / Constants.CONSTANT_1000);
            params.put("create_by", params.get("user_id") + "");
            boolean flag = talentMapper.edit(params);
        }
        return true;
    }
}
