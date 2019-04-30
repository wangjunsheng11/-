package com.kakacl.product_service.controller.open.rest;


import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wangjunsheng
 * @version v1.0.0
 * @description 能力控制器
 * @date 2019-01-09
 */
@RestController
@RequestMapping("/api/open/rest/{version}/mytest")
public class MytestController extends BaseController {

    @Autowired
    private TestService testService;

    @RequestMapping("/wang")
      public List<Map> getList(){
        System.out.print("王俊生");
        return testService.getList();
}


}
