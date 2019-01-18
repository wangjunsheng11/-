package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.service.UserDataService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 我的主页
 * @date 2010-01-10
 */
@RestController
@RequestMapping("/api/rest/{version}/mypage")
public class MyPageController extends BaseController {

    @Autowired
    private UserDataService userDataService;

    /**
     * showdoc
     * @catalog v1.0.1/我的资料
     * @title 我的资料-获取我的职业轨迹
     * @description 根据token获取我的职业轨迹
     * 我入职的企业列表 企业名称 企业图片 当前状态 入职日期 离职日期
     * @method get
     * @url /api/rest/v1.0.1/mypage/findEmployeeHistory
     * @param token 必选 string token
     * @param time 必选 int 请求时间戳
     * @return {"status":"200","message":"请求成功","data":[{"image":"http://192.168.4.170:8081/1545188000331-762166088940527181.png","orbit_id":"8","entry_time":1547006424,"company_name":"苏州富通精密机械有限公司","resignation_time":1547006424,"work_status":"52110"},{"image":"http://192.168.4.170:8081/1545188000331-762166088940527181.png","orbit_id":"9","entry_time":1547006424,"company_name":"航天信息","resignation_time":1547006424,"work_status":"52100"}],"page":null,"ext":null}
     * @return_param data Object 职业轨迹数据
     * @return_param status string 状态
     * @return_param image String 图片地址
     * @return_param orbit_id String 当前这一求职纪录主键
     * @return_param entry_time String 日志时间-精确到秒
     * @return_param company_name String 企业名称
     * @return_param resignation_time String 离职日期
     * @return_param work_status String 当前这一职位状态
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(value = "findEmployeeHistory", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findEmployeeHistory(HttpServletRequest request, String token, String time) {
        Map params = new HashMap<>();
        params.put("user_id", getUserid(request));
        List<Map> data = userDataService.findListByUserid(params);
        return Resp.success(data);
    }

    /**
     * showdoc
     * @catalog v1.0.1/我的资料
     * @title 获取我的薪资列表
     * @description 根据token我的薪资列表
     * 企业图片  入职日期 当前状态 工作天数 嘉奖次数 违纪次数 个人所得税 社保 总收入  企业地图坐标
     * @method get
     * @url /api/rest/v1.0.1/mypage/findPys
     * @param token 必选 string token
     * @param currentPage 可选 int 当前页-默认1
     * @param pageSize 可选 int 每页数量-默认3
     * @param time 必选 int 请求时间戳
     * @param company_id 必选 String 公司主键
     * @return {"status":"200","message":"请求成功","data":{"pageNum":1,"pageSize":3,"size":2,"startRow":1,"endRow":2,"total":2,"pages":1,"list":[{"insurance":1.0,"income":1.0,"del_flag":0,"company_id":"5","create_time":1547006424,"tax":1.0,"detail_id":"1","produce_time":1547006424,"create_by":"1547006424247526","orbit_id":"8","award":0,"user_id":"1547006424247526","work_day":1,"punish":0,"pay_type":50701,"id":"1"},{"insurance":1.0,"income":1.0,"del_flag":0,"company_id":"5","create_time":1547006424,"tax":1.0,"detail_id":"1","produce_time":1547006424,"create_by":"1547006424247526","orbit_id":"9","award":0,"user_id":"1547006424247526","work_day":1,"punish":0,"pay_type":50701,"id":"2"}],"prePage":0,"nextPage":0,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1],"navigateFirstPage":1,"navigateLastPage":1,"firstPage":1,"lastPage":1},"page":null,"ext":null}
     * @return_param data Object 职业轨迹数据
     * @return_param status string 状态
     * @return_param insurance String 社保
     * @return_param income String 收入
     * @return_param tax String 税费
     * @return_param produce_time String 收入所得时间
     * @return_param orbit_id String 职位轨迹主键
     * @return_param award String 奖励次数
     * @return_param punish String 惩戒次数
     * @return_param work_day String 工作天数
     * @return_param pay_type String 收入类型
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(value = "findPys", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findPys(HttpServletRequest request,
                        @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
                        @RequestParam(value = "pageSize", defaultValue = "3")int pageSize,
                        String time, String token,
                        @RequestParam(value = "company_id", required = true)int company_id) {
        Map params = new HashMap<>();
        params.put("user_id", getUserid(request));
        params.put("currentPage", currentPage);
        params.put("pageSize", pageSize);
        params.put("company_id", company_id);
        return Resp.success(userDataService.findPaysByUserid(params));
    }

    /**
     * showdoc
     * @catalog v1.0.1/我的资料
     * @title 获取我的薪资详情
     * @description 根据token我的薪资详情
     * 工号 线号 组别 排班天数 实际出勤 基本工资 平时加班数 平时加班费 周末加班数 周末加班费 旷工罚款 迟到罚款 餐费 预支工资 其他
     * @method get
     * @url /api/rest/v1.0.1/mypage/findPayDetail
     * @param token 必选 string token
     * @param time 必选 int 请求时间戳
     * @param pay_id 必选 String 薪资主键
     * @return {"status":"200","message":"请求成功","data":[{"create_by":"1547006424","del_flag":0,"create_time":1547006424,"price":1.0,"id":"1","remake":"基本工资","pay_id":"1","order":1},{"create_by":"1547006424","del_flag":0,"create_time":1547006424,"price":50.0,"id":"2","remake":"平时加班","pay_id":"1","order":2},{"create_by":"1547006424","del_flag":0,"create_time":1547006424,"price":200.0,"id":"3","remake":"绩效","pay_id":"1","order":3}],"page":null,"ext":null}
     * @return_param data Object 职业轨迹数据
     * @return_param status string 状态
     * @return_param remake String 描述
     * @return_param order String 展示顺序
     * @return_param price String 金额
     * @remark 这里是备注信息
     * @number 99
     */
    @RequestMapping(value = "findPayDetail", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findPayDetail(HttpServletRequest request, String token, String time,
                              @RequestParam(value = "pay_id", required = true)String pay_id) {
        Map params = new HashMap<>();
        params.put("pay_id", pay_id);
        return Resp.success(userDataService.findPayDetail(params));
    }

    // 异常申诉- 对单独的个人发送消息

}
