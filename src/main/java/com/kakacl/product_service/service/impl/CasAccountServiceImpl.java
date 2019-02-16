package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.AccountMapper;
import com.kakacl.product_service.mapper.CasAccountMapper;
import com.kakacl.product_service.service.CasAccountService;
import com.kakacl.product_service.service.CasMenuService;
import com.kakacl.product_service.service.GradeService;
import com.kakacl.product_service.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description CAS单点登录实现
 * @date 2018-01-09
 */
@Service
@RefreshScope
public class
CasAccountServiceImpl implements CasAccountService {

    @Autowired
    private CasAccountMapper casAccountMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CasMenuService casMenuService;

    @Autowired
    private GradeService gradeService;

    @Value("${sys-name}")
    private String sys_name;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(Map<String, Object> params) {
        boolean flag = false;
        casAccountMapper.insert(params);
        accountMapper.insert(params);
        params.put("user_id", params.get("id"));
        params.put("id", IDUtils.genHadId());
        flag = gradeService.insert(params);
        return flag;
    }

    @Override
    public Map selectOne(Map<String, Object> params) {
        Map result = new HashMap();
        Map cas_base = casAccountMapper.selectOne(params);
        if(cas_base == null) {
            return  cas_base;
        }
        params.put("user_id", cas_base.get("id"));
        params.put("sys_type", sys_name);
        List<Map> menu_base = casMenuService.selectListByUserid(params);
        result.put("cas_base", cas_base);
        result.put("menu_base", menu_base);
        return result;
    }

    @Override
    public Map selectOneByPhonenum(Map<String, Object> params) {
        return casAccountMapper.selectOneByPhonenum(params);
    }

    @Override
    public Map selectOneByKakanum(Map<String, Object> params) {
        return casAccountMapper.selectOneByKakanum(params);
    }

    @Override
    public int updateOnePassById(Map<String, Object> params) {
        return casAccountMapper.updateOnePassById(params);
    }

    @Override
    public int updateOnePassByPhonenum(Map<String, Object> params) {
        return casAccountMapper.updateOnePassByPhonenum(params);
    }

    @Override
    public boolean updateStatusById(Map params) {
        return casAccountMapper.updateStatusById(params);
    }
}
