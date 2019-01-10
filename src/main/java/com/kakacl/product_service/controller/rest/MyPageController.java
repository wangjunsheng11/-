package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 我的主页
 * @date 2010-01-10
 */
@RestController
@RequestMapping("/api/{version}/mypage")
public class MyPageController extends BaseController {

    /*
     * 1
     * 用户名 身份证号码
     * 我入职的企业列表 企业名称 企业图片 当前状态 入职日期 离职日期
     * @author wangwei
     * @date 2019/1/10
      * @param null
     * @return
     */


    /*
     *
     * 2
     * 点击某一个信息后 查询某一个我的入职企业轨迹详情 在职的企业
     * 企业图片  入职日期 当前状态 工作天数 嘉奖次数 违纪次数 个人所得税 社保 总收入  企业地图坐标
     * 工资列表信息
     * @author wangwei
     * @date 2019/1/10
      * @param null
     * @return
     */

    /*
     *2
     * 功能描述
     * 点击某一个工资 进入工资详情
     * 工号 线号 组别 排班天数 实际出勤 基本工资 平时加班数 平时加班费 周末加班数 周末加班费 旷工罚款 迟到罚款 餐费 预支工资 其他
     * @author wangwei
     * @date 2019/1/10
      * @param null
     * @return
     */

    // 异常申诉- 对单独的个人发送消息

}
