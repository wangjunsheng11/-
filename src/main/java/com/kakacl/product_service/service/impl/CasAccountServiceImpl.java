package com.kakacl.product_service.service.impl;

import com.kakacl.product_service.mapper.AccountMapper;
import com.kakacl.product_service.mapper.CasAccountMapper;
import com.kakacl.product_service.service.CasAccountService;
import com.kakacl.product_service.service.GradeService;
import com.kakacl.product_service.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

/**
 * @author wangwei
 * @version v1.0.0
 * @description CAS单点登录实现
 * @date 2018-01-09
 */
@Service
public class CasAccountServiceImpl implements CasAccountService {

    @Autowired
    private CasAccountMapper casAccountMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private GradeService gradeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(Map<String, Object> params) {
        boolean flag = false;
        casAccountMapper.insert(params);
        accountMapper.insert(params);
        params.put("user_id", params.get("id"));
        params.put("id", IDUtils.genHadId());
        flag = gradeService.insert(params);
        // TODO 能力，天赋
        return flag;
    }

    @Override
    public Map selectOne(Map<String, Object> params) {
        return casAccountMapper.selectOne(params);
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
}
