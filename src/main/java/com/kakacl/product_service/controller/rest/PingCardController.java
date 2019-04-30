package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constant;
import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.AccountService;
import com.kakacl.product_service.service.PingCardService;
import com.kakacl.product_service.service.WorkCheckService;
import com.kakacl.product_service.utils.*;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.*;

/**
 * @author wangwei<br />
 * @Description: 打卡控制器<br/>
 * @date 2019/2/4 11:06<br/>
 * ${TAGS}
 */
@RestController
@RequestMapping("/api/rest/{version}/pingcard")
public class PingCardController extends BaseController {

    @Autowired
    private PingCardService pingCardService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private WorkCheckService workCheckService;


    /**
     * showdoc
     * @catalog v1.0.1/用户打卡
     * @title 公司打卡规则设置,GCJ-02标准
     * @description 根据公司这只公司打卡规则，设置公司的坐标点;一个公司可以设置多个坐标点，可能公司有分公司，子公司，办事处等。
     * @method post
     * @url /api/rest/v1.0.1/pingcard/pingCardScopeRule
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param company_id 必选 string 公司主键
     * @param longitude 必选 string 经度
     * @param latitude 必选 string 纬度
     * @param effective_radius 可选 int 有效半，默认0
     * @param order 可选 int 顺序，默认99
     * @return
     * @return_param {"status":"200","message":"请求成功","data":{"id":"1549945941237165","company_id":"2","order":1,"longitude":"30.40628917093897","latitude":"118.51529848521174"},"page":null,"ext":null}
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "pingCardScopeRule", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp pingCardScopeRule(HttpServletRequest request,
                                  @RequestParam(value = "time", required = true)String time,
                                  String token,
                                  @RequestParam(value = "company_id", required = true)String company_id,
                                  @RequestParam(value = "longitude", required = true)String longitude,
                                  @RequestParam(value = "latitude", required = true)String latitude,
                                  @RequestParam(value = "effective_radius", required = true, defaultValue = "0")String effective_radius,
                                  @RequestParam(value = "order", required = true, defaultValue = "99")int order,
                                  Map params) {
        params.put("id", IDUtils.genHadId());
        params.put("company_id", company_id);
        params.put("order", order);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("effective_radius", effective_radius);
        boolean flag = pingCardService.insertPingCardScopeRule(params);
        if(flag) {
            return Resp.success(params);
        } else {
            return Resp.fail(params);
        }
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户打卡
     * @title 判断用户当前是否允许打卡,GCJ-02标准
     * @description 根据当前用户坐标和公司，判断用户是否允许打卡，打卡前需要先调用此方法，否则可能打卡不成功，此方法返回成功前，打卡按钮无效，返回公司允许打卡位置集合，客户端可以根据公司的位置和半径在地图上画一个打卡区域。
     * @method get
     * @url /api/rest/v1.0.1/pingcard/pingCardValidate
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param company_id 必选 string 公司主键
     * @param longitude 必选 string 经度120.534119
     * @param latitude 必选 string 纬度31.436429
     * @return {"status":"200","message":"请求成功","data":{"data":[{"del_flag":0,"company_id":"7","effective_radius":5000,"latitude":"31.436429","id":"5","order":1,"longitude":"120.534119"}]},"page":null,"ext":null}
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "pingCardValidate", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp pingCardValidate(HttpServletRequest request,
                                 @RequestParam(value = "time", required = true)String time,
                                 String token,
                                 @RequestParam(value = "company_id", required = true)String company_id,
                                 @RequestParam(value = "longitude", required = true)String longitude,
                                 @RequestParam(value = "latitude", required = true)String latitude,
                                 Map params) {
        Map result = new HashMap();
        params.put("company_id", company_id);
        List<Map> data = pingCardService.selectCompanyLocation(params);
        result.put("data", data);

        for (int i = Constants.CONSTANT_0; i < data.size(); i++) {
            double e_longitude = Double.valueOf(data.get(i).get("longitude").toString());
            double e_latitude = Double.valueOf(data.get(i).get("latitude").toString());
            double[] data_scope = LatLonUtil.GetAround(e_latitude, e_longitude, Integer.parseInt(data.get(i).get("effective_radius").toString()));
            double la_1 = data_scope[Constants.CONSTANT_0];
            double lo_1 = data_scope[Constants.CONSTANT_1];
            double la_2 = data_scope[Constants.CONSTANT_2];
            double lo_2 = data_scope[Constants.CONSTANT_3];
            if(lo_1 < Double.valueOf(longitude) && lo_2 > Double.valueOf(longitude)
                    && la_1 < Double.valueOf(latitude) && la_2 > Double.valueOf(latitude)) {
                return Resp.success(result);
            }
        }
        return Resp.fail(result);
    }

    /**
     * showdoc
     * @catalog v1.0.1/用户打卡31.4360324490
     * @title 获取当前用户最后的的打卡类型
     * @description 当前用户上一次的打卡类型-用户可能忘记打卡的情况，用户可能会手动切换打卡类型-返回值-ping_type始终存在，如果用户没有打过卡，这里ping_type默认返回下班。
     * @method get
     * @url /api/rest/v1.0.1/pingcard/findLastPingCardType
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @return {"status":"200","message":"请求成功","data":{"create_by":"1547006424247526","del_flag":0,"create_time":1549252601,"user_id":"1547006424247526","ping_type":"上班","id":"1549252601622393"},"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param ping_type string 上一次打卡类型
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "findLastPingCardType", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findLastPingCardType(HttpServletRequest request,
                             @RequestParam(value = "time", required = true)String time,
                             String token, Map params) {
        params.put("user_id", getUserid(request));
        String id1=null;
        Map param1 = new HashMap();
        param1.put("user_id",getUserid(request));
        Map result1 =accountService.selectById(param1);

        if(result1!=null){
            String cardId =(String)result1.get("id_card");
            param1.put("card_no",cardId);
            result1.clear();
            result1 = accountService.findStoreAccountInfoByCard(param1);

        }
        if(result1!=null){
            id1 =result1.get("account_id").toString();
            param1.clear();
            param1.put("id",id1);
            result1.clear();
            result1=accountService.findStoreAccountById(param1);
        }
        if(result1!=null){
            //获取该员工的考勤组信息，（打卡的次数，是白班还是夜班）192.168.4.155
            Integer wid =Integer.parseInt(result1.get("workCheck_id").toString());
            param1.clear();
            param1.put("id",wid);
            //根据wid查询该考勤组的相关信息
            result1.clear();
            result1 = workCheckService.findWorkCheckById(param1);
        }
        Integer gid =Integer.parseInt(result1.get("id").toString());
        String type =result1.get("title").toString();
        Map data  = pingCardService.slectLastPingType(params);
        if(data == null) {
            data= new HashMap();
                data.put("ping_type", Constant.PING_TYPE_DOWN);
        }else{
                if(type.equals("白班")){
                    params.put("time",DateUtil.getNowDate1());
                    data = pingCardService.slectLastPingType2(params);
                    if(data == null){
                        data= new HashMap();
                        data.put("ping_type", Constant.PING_TYPE_DOWN);
                    }
                }else if(type.equals("夜班")){
                   String str =DateUtil.AM_PM();
                    str=str.replaceAll("-","").replaceAll(" ","");
                    str=str.substring(0,10);
                    params.put("time",str);
                    params.put("time1",DateUtil.getNowDate4());
                    data = pingCardService.slectLastPingTypeOfNight(params);
                    if(data==null){
                        data= new HashMap();
                        data.put("ping_type", Constant.PING_TYPE_DOWN);
                    }
                }
        }
        return Resp.success(data);
    }







    /**
     * showdoc
     * @catalog v1.0.1/用户打卡
     * @title 用户打卡
     * @description 当前用户打卡
     * @method post
     * @url /api/rest/v1.0.1/pingcard/ping
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param user_id 必选 string 打卡用户主键
     * @param ping_time 必选 string 打卡时间戳
     * @param longitude 必选 string 经度
     * @param latitude 必选 string 纬度
     * @param company_id 必选 string 归属公司主键
     * @param company_name 必选 string 归属公司名
     * @param ping_type 必选 string 当前打卡类型，上班OR下班
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "ping", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp ping(HttpServletRequest request,
                     @RequestParam(value = "time", required = true)String time,
                     String token,
                     @RequestParam(value = "user_id", required = true)String user_id,
                     @RequestParam(value = "ping_time", required = true)String ping_time,
                     @RequestParam(value = "longitude", required = true)String longitude,
                     @RequestParam(value = "latitude", required = true)String latitude,
                     @RequestParam(value = "company_id", required = true)String company_id,
                     @RequestParam(value = "company_name", required = true)String company_name,
                     @RequestParam(value = "ping_type", required = true)String ping_type,
                     Map params
    ) {
        params.put("user_id", user_id);
        params.put("ping_time", ping_time);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("company_id", company_id);
        params.put("company_name", company_name);
        params.put("id", IDUtils.genHadId());
        params.put("create_time",System.currentTimeMillis()/1000L);
        params.put("create_by", getUserid(request));
        params.put("ping_type", ping_type);
        boolean flag=false;
        Map maps =new HashMap();
        maps.put("user_id", user_id);
        Map data = pingCardService.slectLastPingType(maps);
        if(data==null){
            flag = pingCardService.insertPingCard(params);
        }else{
            Long pingTime = Long.parseLong(data.get("ping_time").toString());
            pingTime=pingTime*1000L;
            Long crruteTime = System.currentTimeMillis();
            maps =DateUtil.getDatePoor1(crruteTime,pingTime);
            if(Integer.parseInt(maps.get("min").toString())<10){
                //如果打卡间隔小于10分钟，则打卡失败
                return  Resp.fail("更新打卡失败，打卡过于频繁");
            }else{
                //再根考勤组的中的每天打卡次数来限定，每天的打卡次数
                String id1=null;
                Map param1 = new HashMap();
                param1.put("user_id",user_id);
                Map result1 =accountService.selectById(param1);

                if(result1!=null){
                    String cardId =(String)result1.get("id_card");
                    param1.put("card_no",cardId);
                    result1.clear();
                    result1 = accountService.findStoreAccountInfoByCard(param1);

                }
                if(result1!=null){
                    id1 =result1.get("account_id").toString();
                    param1.clear();
                    param1.put("id",id1);
                    result1.clear();
                    result1=accountService.findStoreAccountById(param1);
                }
                if(result1!=null){
                    //获取该员工的考勤组信息，（打卡的次数，是白班还是夜班）192.168.4.155
                    Integer wid =Integer.parseInt(result1.get("workCheck_id").toString());
                    param1.clear();
                    param1.put("id",wid);
                    //根据wid查询该考勤组的相关信息
                    result1.clear();
                    result1 = workCheckService.findWorkCheckById(param1);
                }
                Integer gid =Integer.parseInt(result1.get("id").toString());
                String type =result1.get("title").toString();
                Integer pingCardNum =Integer.parseInt( result1.get("ping_card_num").toString());
                if(type.equals("白班")){
                    maps.clear();
                    maps.put("user_id", user_id);
                    maps.put("time",DateUtil.getNowDate1());
                    Integer count = pingCardService.selectCountPing(maps);
                    if(count<pingCardNum){
                        flag = pingCardService.insertPingCard(params);
                    }else{
                        return  Resp.fail("今天已经打了"+count+"次卡,无法再打卡");
                    }
                }else if(type.equals("夜班")){
                    //根据pingCardNum来确定该员工今天能打几次卡
                    //获取当前时间
                    Integer time1 =DateUtil.getNowDate4();
                    //根据当前时间来判断是否是大于今天的白天12点
                    Integer time2 =DateUtil.getNowDate2();
                    if(time1>time2){
                        maps.clear();
                        maps.put("user_id", user_id);
                        maps.put("time",time2);
                        maps.put("time1",time1);
                        System.out.println(maps);
                        Integer count = pingCardService.selectCountPingOfNight(maps);
                        //如果当前时间大于今天的12点，则证明文此人在这次打卡为第一次上班打卡
                        if(count>1){
                            return  Resp.fail("今天已经打了"+count+"次卡,无法再打卡");
                        }else{

                            flag = pingCardService.insertPingCard(params);
                        }
                    }else{
                        //如果当前时间小于今天的12点，则证明文此人在这次打卡不是第一次上班打卡
                        time2 =DateUtil.getNowDate3();
                        maps.clear();
                        maps.put("user_id", user_id);
                        maps.put("time",time2);
                        maps.put("time1",time1);
                        Integer count = pingCardService.selectCountPingOfNight(maps);
                        if(count<pingCardNum){
                            flag = pingCardService.insertPingCard(params);
                        }else{
                            return  Resp.fail("今天已经打了"+count+"次卡,无法再打卡");
                        }
                    }
                }
            }
        }
        if(flag) {
            Runnable runnable =  new Runnable() {
                @Override
                public void  run() {
                    Date createTime = new Date();
                    Map param = new HashMap();
                    param.put("user_id",user_id);
                    Map result =accountService.selectById(param);
                    if(result!=null){
                        String cardId =(String)result.get("id_card");
                        param.put("card_no",cardId);
                        result.clear();
                        result = accountService.findStoreAccountInfoByCard(param);
                    }
                    String id = null;
                    if(result!=null){
                        id =result.get("account_id").toString();
                        param.clear();
                        param.put("id",id);
                        result.clear();
                        result=accountService.findStoreAccountById(param);
                    }
                    if(result!=null){
                        //获取该员工的考勤组信息，（打卡的次数，是白班还是夜班）192.168.4.155
                        Integer wid =Integer.parseInt(result.get("workCheck_id").toString());
                        param.clear();
                        param.put("id",wid);
                        //根据wid查询该考勤组的相关信息
                        result.clear();
                        result = workCheckService.findWorkCheckById(param);
                    }

                    if(result!=null){
                        Integer gid =Integer.parseInt(result.get("id").toString());
                        //进行判断，查询最后一次打卡时间，拿打卡时间于当前日期进行对比，如果是白班，打卡时间超过24小时，则视为无效，统一为上班打卡时间
                        String type =(String)result.get("title");
                        Map results = new HashMap();
                        param.clear();
                        param.put("user_id",id);
                        param.put("gid",gid);
                        if(type.equals("白班")){
                            param.put("create_time",DateUtil.dateToStr(new Date()));
                            results = workCheckService.findWorkDayTime(param);
                        }else{
                            param.put("ping_time",DateUtil.AM_PM());
                            param.put("check_time",DateUtil.getStringDate());
                            results = workCheckService.findWorkNight(param);
                        }
                        if(results!=null){
                            Integer id1 = Integer.parseInt(""+results.get("id"));
                            param.clear();
                            param.put("id",id1);
                            if (results.get("end1_Time")==null){
                                Date eTime =DateUtil.strToDateLong(results.get("begin1_Time").toString());
                                Map maps = DateUtil.getDatePoor(createTime,eTime);
                                param.put("hrs_normal",Double.valueOf(maps.get("minAll").toString()));
                                param.put("end1_Time",DateUtil.dateToStrLong(createTime));

                                String str1 = result.get("first_ETime").toString();
                                String str2 = DateUtil.getStringDate().substring(0,11);
                                str2 =str2+str1;
                                Date eTime1 = DateUtil.strToDateLong(str2);
                                Map maps1 = DateUtil.getDatePoor(createTime,eTime1);
                                String remarks;
                                if(Double.valueOf(maps1.get("minAll").toString())>0){
                                    remarks =results.get("remarks").toString()+"0";

                                }else{
                                    remarks =results.get("remarks").toString()+"1";
                                }
                                param.put("remarks",remarks);

                            } else if (results.get("begin2_Time")==null){
                                param.put("begin2_Time",DateUtil.dateToStrLong(createTime));
                                String str1 = result.get("second_STime").toString();
                                String str2 = DateUtil.getStringDate().substring(0,11);
                                str2 =str2+str1;
                                Date bTime = DateUtil.strToDateLong(str2);
                                Map maps = DateUtil.getDatePoor(bTime,createTime);
                                String remarks;
                                if(Double.valueOf(maps.get("minAll").toString())>0){
                                    remarks =results.get("remarks").toString()+"0";

                                }else{
                                    remarks =results.get("remarks").toString()+"1";
                                }
                                param.put("remarks",remarks);
                            }else if (results.get("end2_Time")==null){
                                Date bTime = DateUtil.strToDateLong(results.get("begin2_Time").toString());

                                Map maps = DateUtil.getDatePoor(createTime,bTime);
                                Double hours = Double.valueOf(results.get("hrs_normal").toString());
                                param.put("hrs_normal",hours+Double.valueOf(maps.get("minAll").toString()));
                                param.put("end2_Time",DateUtil.dateToStrLong(createTime));


                                String str1 = result.get("second_ETime").toString();
                                String str2 = DateUtil.getStringDate().substring(0,11);
                                str2 =str2+str1;
                                Date eTime1 = DateUtil.strToDateLong(str2);
                                Map maps1 = DateUtil.getDatePoor(createTime,eTime1);
                                String remarks;
                                if(Double.valueOf(maps1.get("minAll").toString())>0){
                                    remarks =results.get("remarks").toString()+"0";

                                }else{
                                    remarks =results.get("remarks").toString()+"1";
                                }
                                param.put("remarks",remarks);

                            }else if (results.get("begin3_Time")==null){
                                param.put("begin3_Time",DateUtil.dateToStrLong(createTime));

                                String str1 = result.get("ext_begin").toString();
                                String str2 = DateUtil.getStringDate().substring(0,11);
                                str2 =str2+str1;
                                Date bTime = DateUtil.strToDateLong(str2);
                                Map maps = DateUtil.getDatePoor(bTime,createTime);
                                String remarks;
                                if(Double.valueOf(maps.get("minAll").toString())>0){
                                    remarks =results.get("remarks").toString()+"0";

                                }else{
                                    remarks =results.get("remarks").toString()+"1";
                                }
                                param.put("remarks",remarks);
                            }else if (results.get("end3_Time")==null){
                                Date bTime = DateUtil.strToDateLong(results.get("end3_Time").toString());
                                Map maps = DateUtil.getDatePoor(createTime,bTime);
                                Double hours = Double.valueOf(results.get("hrs_normal").toString());
                                param.put("hrs_normal",hours+Double.valueOf(maps.get("minAll").toString()));
                                param.put("end3_Time",DateUtil.dateToStrLong(createTime));

                                String str1 = result.get("ext_end").toString();
                                String str2 = DateUtil.getStringDate().substring(0,11);
                                str2 =str2+str1;
                                Date eTime1 = DateUtil.strToDateLong(str2);
                                Map maps1 = DateUtil.getDatePoor(createTime,eTime1);
                                String remarks;
                                if(Double.valueOf(maps1.get("minAll").toString())>0){
                                    remarks =results.get("remarks").toString()+"0";

                                }else{
                                    remarks =results.get("remarks").toString()+"1";
                                }
                                param.put("remarks",remarks);

                            }else{
                                return ;
                            }

                            param.put("update_time",DateUtil.dateToStrLong(createTime));
                            workCheckService.updateWorkTime(param);
                        }else{
                            param.put("user_id",id);
                            param.put("gid",gid);
                            param.put("expstate",1);
                            param.put("hrs_normal",0);
                            //将考勤组的打卡时间拿出来作对比
                            String str1 = result.get("first_STime").toString();
                            String str2 = DateUtil.getStringDate().substring(0,11);
                            str2 =str2+str1;
                            Date bTime = DateUtil.strToDateLong(str2);
                            Map maps = DateUtil.getDatePoor(bTime,createTime);
                            if(Double.valueOf(maps.get("minAll").toString())>0){
                                param.put("remarks","0");
                            }else{
                                param.put("remarks","1");
                            }
                            param.put("begin1_Time",DateUtil.dateToStrLong(createTime));
                            param.put("create_time",DateUtil.dateToStrLong(createTime));
                            workCheckService.insertWorkStatus(param);
                        }
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
        return flag ? Resp.success(params) : Resp.fail(params);
    }


























    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @GetMapping(value = "findLastPingCardType1", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp findLastPingCardType1(HttpServletRequest request,
                                      @RequestParam(value = "time", required = true)String time,
                                      String token, Map params) {
        params.put("user_id", getUserid(request));
        String id1=null;
        Map param1 = new HashMap();
        Map data =new HashMap();
        param1.put("user_id",getUserid(request));
        Map result1 =accountService.selectById(param1);
        if(result1!=null){
            String cardId =(String)result1.get("id_card");
            param1.put("card_no",cardId);
            result1.clear();
            result1 = accountService.findStoreAccountInfoByCard(param1);

        }
        if(result1!=null){
            id1 =result1.get("account_id").toString();
            param1.clear();
            param1.put("id",id1);
            result1.clear();
            result1=accountService.findStoreAccountById(param1);
        }
        if(result1!=null){
            //获取该员工的考勤组信息，（打卡的次数，是白班还是夜班）192.168.4.155
            Integer wid =Integer.parseInt(result1.get("workCheck_id").toString());
            param1.clear();
            param1.put("id",wid);
            //根据wid查询该考勤组的相关信息
            result1.clear();
            result1 = workCheckService.findWorkCheckById(param1);
        }
        Integer gid =Integer.parseInt(result1.get("id").toString());
        String type =result1.get("title").toString();
        //根据该人员的考勤组信息确定当前的打卡信息，给前端显示显示相应数据，根据上下班来分
        Date createTime = new Date();

        String dateStr = DateUtil.getStringDate().substring(0,11);

        String first_STime =String.valueOf( result1.get("first_STime"));
        first_STime=dateStr+first_STime;
        Date workCheckTime1 = DateUtil.strToDateLong(first_STime);
        Double min1 =Double.parseDouble(DateUtil.getDatePoor(createTime,workCheckTime1).get("minAll").toString());


        String first_ETime =String.valueOf(result1.get("first_ETime"));
        first_ETime = dateStr+first_ETime;
        Date workCheckTime2 = DateUtil.strToDateLong(first_ETime);
        Double min2 =Double.parseDouble(DateUtil.getDatePoor(createTime,workCheckTime2).get("minAll").toString());


        String second_STime;
        Double min3=null;
        String second_ETime;
        Double min4=null;
        String ext_begin;
        Double min5=null;
        String ext_end;
        Double min6=null;
        if(result1.get("second_STime")!=null&&!result1.get("second_STime").equals("")){
            second_STime =String.valueOf(result1.get("second_STime"));
            second_STime = dateStr+second_STime;
            Date workCheckTime3 = DateUtil.strToDateLong(second_STime);
            min3 =Double.parseDouble(DateUtil.getDatePoor(createTime,workCheckTime3).get("minAll").toString());
        }

        if(result1.get("second_ETime")!=null&&!result1.get("second_ETime").equals("")){
            second_ETime =String.valueOf(result1.get("second_ETime"));
            second_ETime=dateStr+second_ETime;
            Date workCheckTime4 = DateUtil.strToDateLong(second_ETime);
            min4 =Double.parseDouble(DateUtil.getDatePoor(createTime,workCheckTime4).get("minAll").toString());
        }

        if(result1.get("ext_begin")!=null&&!result1.get("ext_begin").equals("")){
            ext_begin =String.valueOf(result1.get("ext_begin"));
            ext_begin=dateStr+ext_begin;
            Date workCheckTime5 = DateUtil.strToDateLong(ext_begin);
            min5 =Double.parseDouble(DateUtil.getDatePoor(createTime,workCheckTime5).get("minAll").toString());
        }else{
            System.out.println("ext_begin");
        }

        if(result1.get("ext_end")!=null&&!result1.get("ext_end").equals("")){
            ext_end =String.valueOf(result1.get("ext_end"));
            ext_end=dateStr+ext_end;
            Date workCheckTime6 = DateUtil.strToDateLong(ext_end);
            min6 =Double.parseDouble(DateUtil.getDatePoor(createTime,workCheckTime6).get("minAll").toString());
        }else{
            System.out.println("ext_end");
        }

        if(min1<=30){
            if(min1<=0) {
                data.put("ping_type", Constant.PING_TYPE_UP_NORM);
                data.put("ping_count",1);
            }else{
                data.put("ping_type",Constant.PING_TYPE_LATER);
                data.put("ping_count",1);
            }
        }else{
            if(min2<0){
                data.put("ping_type",Constant.PING_TYPE_EARLY);
                data.put("ping_count",2);
            }else{
                if(min3!=null){
                    if(min2<=30){
                        data.put("ping_type",Constant.PING_TYPE_DOWN_NORM);
                        data.put("ping_count",2);
                    }else{
                        if(min3<=30){
                            if(min3<=0){
                                data.put("ping_type", Constant.PING_TYPE_UP_NORM);
                                data.put("ping_count",3);
                            }else{
                                data.put("ping_type",Constant.PING_TYPE_LATER);
                                data.put("ping_count",3);
                            }
                        }else{
                            if(min4!=null){
                                if(min4<0){
                                    data.put("ping_type",Constant.PING_TYPE_EARLY);
                                    data.put("ping_count",4);
                                }else{
                                    if(min5!=null){
                                        if(min4<=30){
                                            data.put("ping_type",Constant.PING_TYPE_DOWN_NORM);
                                            data.put("ping_count",4);
                                        }else{
                                            if(min5<=30){
                                                if(min5<=0){
                                                    data.put("ping_type", Constant.PING_TYPE_UP_NORM);
                                                    data.put("ping_count",5);
                                                }else{
                                                    data.put("ping_type",Constant.PING_TYPE_LATER);
                                                    data.put("ping_count",5);
                                                }
                                            }else{
                                                if(min6!=null){
                                                    if(min6<0){
                                                        data.put("ping_type",Constant.PING_TYPE_EARLY);
                                                        data.put("ping_count",6);
                                                    }else {
                                                        data.put("ping_type",Constant.PING_TYPE_DOWN_NORM);
                                                        data.put("ping_count",6);
                                                    }
                                                }
                                            }
                                        }
                                    }else{
                                        data.put("ping_type",Constant.PING_TYPE_DOWN_NORM);
                                        data.put("ping_count",4);
                                    }
                                }
                            }
                        }
                    }
                }else{
                    data.put("ping_type",Constant.PING_TYPE_DOWN_NORM);
                    data.put("ping_count",2);
                }

            }
        }
        return Resp.success(data);
    }






    /**
     * showdoc
     * @catalog v1.0.1/用户打卡
     * @title 用户打卡
     * @description 当前用户打卡
     * @method post
     * @url /api/rest/v1.0.1/pingcard/ping
     * @param time 必选 string 请求时间戳
     * @param token 必选 string token
     * @param user_id 必选 string 打卡用户主键
     * @param ping_time 必选 string 打卡时间戳
     * @param longitude 必选 string 经度
     * @param latitude 必选 string 纬度
     * @param company_id 必选 string 归属公司主键
     * @param company_name 必选 string 归属公司名
     * @param ping_type 必选 string 当前打卡类型，上班OR下班
     * @return {"status":"200","message":"请求成功","data":,"page":null,"ext":null}
     * @return_param message string 消息
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @PostMapping(value = "ping1", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp ping1(HttpServletRequest request,
                      @RequestParam(value = "time", required = true)String time,
                      String token,
                      @RequestParam(value = "user_id", required = true)String user_id,
                      @RequestParam(value = "ping_time", required = true)String ping_time,
                      @RequestParam(value = "longitude", required = true)String longitude,
                      @RequestParam(value = "latitude", required = true)String latitude,
                      @RequestParam(value = "company_id", required = true)String company_id,
                      @RequestParam(value = "company_name", required = true)String company_name,
                      @RequestParam(value = "ping_type", required = true)String ping_type,
                      @RequestParam(value="ping_count",required = true)Integer ping_count,
                      Map params
    ) {
        System.out.print("进入方法");
        params.put("user_id", user_id);
        params.put("ping_time", ping_time);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("company_id", company_id);
        params.put("company_name", company_name);
        params.put("id", IDUtils.genHadId());
        params.put("create_time",System.currentTimeMillis()/1000L);
        params.put("create_by", getUserid(request));
        params.put("ping_type", ping_type);
        boolean flag=false;
        Map maps =new HashMap();
        maps.put("user_id", user_id);
        Map data = pingCardService.slectLastPingType(maps);
        if(data==null){
            flag = pingCardService.insertPingCard(params);
        }else{
            Long pingTime = Long.parseLong(data.get("ping_time").toString());
            pingTime=pingTime*1000L;
            Long crruteTime = System.currentTimeMillis();
            maps =DateUtil.getDatePoor1(crruteTime,pingTime);
            if(Integer.parseInt(maps.get("min").toString())<10){
                //如果打卡间隔小于10分钟，则打卡失败
                return  Resp.fail("更新打卡失败，打卡过于频繁");
            }else{
                //再根考勤组的中的每天打卡次数来限定，每天的打卡次数
                String id1=null;
                Map param1 = new HashMap();
                param1.put("user_id",user_id);
                Map result1 =accountService.selectById(param1);

                if(result1!=null){
                    String cardId =(String)result1.get("id_card");
                    param1.put("card_no",cardId);
                    result1.clear();
                    result1 = accountService.findStoreAccountInfoByCard(param1);

                }
                if(result1!=null){
                    id1 =result1.get("account_id").toString();
                    param1.clear();
                    param1.put("id",id1);
                    result1.clear();
                    result1=accountService.findStoreAccountById(param1);
                }
                if(result1!=null){
                    //获取该员工的考勤组信息，（打卡的次数，是白班还是夜班）192.168.4.155
                    Integer wid =Integer.parseInt(result1.get("workCheck_id").toString());
                    param1.clear();
                    param1.put("id",wid);
                    //根据wid查询该考勤组的相关信息
                    result1.clear();
                    result1 = workCheckService.findWorkCheckById(param1);
                }
                Integer gid =Integer.parseInt(result1.get("id").toString());
                String type =result1.get("title").toString();
                Integer pingCardNum =Integer.parseInt( result1.get("ping_card_num").toString());
                if(type.equals("白班")){
                    maps.clear();
                    maps.put("user_id", user_id);
                    maps.put("time",DateUtil.getNowDate1());
                    Integer count = pingCardService.selectCountPing(maps);
                    if(count<pingCardNum){
                        flag = pingCardService.insertPingCard(params);
                    }else{
                        return  Resp.fail("今天已经打了"+count+"次卡,无法再打卡");
                    }
                }else if(type.equals("夜班")){
                    //根据pingCardNum来确定该员工今天能打几次卡
                    //获取当前时间
                    Integer time1 =DateUtil.getNowDate4();
                    //根据当前时间来判断是否是大于今天的白天12点
                    Integer time2 =DateUtil.getNowDate2();
                    if(time1>time2){
                        maps.clear();
                        maps.put("user_id", user_id);
                        maps.put("time",time2);
                        maps.put("time1",time1);
                        System.out.println(maps);
                        Integer count = pingCardService.selectCountPingOfNight(maps);
                        //如果当前时间大于今天的12点，则证明文此人在这次打卡为第一次上班打卡
                        if(count>=1){
                            return  Resp.fail("今天已经打了"+count+"次卡,无法再打卡");
                        }else{

                            flag = pingCardService.insertPingCard(params);
                        }
                    }else{
                        //如果当前时间小于今天的12点，则证明文此人在这次打卡不是第一次上班打卡
                        time2 =DateUtil.getNowDate3();
                        maps.clear();
                        maps.put("user_id", user_id);
                        maps.put("time",time2);
                        maps.put("time1",time1);
                        Integer count = pingCardService.selectCountPingOfNight(maps);
                        if(count<pingCardNum){
                            flag = pingCardService.insertPingCard(params);
                        }else{
                            return  Resp.fail("今天已经打了"+count+"次卡,无法再打卡");
                        }
                    }
                }
            }
        }
        if(flag) {
            Runnable runnable =  new Runnable() {
                @Override
                public void  run() {
                    Date createTime = new Date();
                    Map param = new HashMap();
                    param.put("user_id",user_id);
                    Map result =accountService.selectById(param);
                    if(result!=null){
                        String cardId =(String)result.get("id_card");
                        param.put("card_no",cardId);
                        result.clear();
                        result = accountService.findStoreAccountInfoByCard(param);
                    }
                    String id = null;
                    if(result!=null){
                        id =result.get("account_id").toString();
                        param.clear();
                        param.put("id",id);
                        result.clear();
                        result=accountService.findStoreAccountById(param);
                    }
                    if(result!=null){
                        //获取该员工的考勤组信息，（打卡的次数，是白班还是夜班）192.168.4.155
                        Integer wid =Integer.parseInt(result.get("workCheck_id").toString());
                        param.clear();
                        param.put("id",wid);
                        //根据wid查询该考勤组的相关信息
                        result.clear();
                        result = workCheckService.findWorkCheckById(param);
                    }

                    if(result!=null){
                        Integer gid =Integer.parseInt(result.get("id").toString());
                        //进行判断，查询最后一次打卡时间，拿打卡时间于当前日期进行对比，如果是白班，打卡时间超过24小时，则视为无效，统一为上班打卡时间
                        String type =(String)result.get("title");
                        Map results = new HashMap();
                        param.clear();
                        param.put("user_id",id);
                        param.put("gid",gid);
                        if(type.equals("白班")){
                            param.put("create_time",DateUtil.dateToStr(new Date()));
                            results = workCheckService.findWorkDayTime(param);
                        }else{
                            param.put("ping_time",DateUtil.AM_PM());
                            param.put("check_time",DateUtil.getStringDate());
                            results = workCheckService.findWorkNight(param);
                        }
                        if(results!=null){
                            Integer id1 = Integer.parseInt(""+results.get("id"));
                            param.clear();
                            param.put("id",id1);
                            param.put("update_time",DateUtil.dateToStrLong(createTime));
                            String str1;
                            String str2;
                            Map maps1;
                            String remarks;
                            Date eTime;
                            Map maps;
                            switch (ping_count){
                                case 2:
                                    eTime =DateUtil.strToDateLong(results.get("begin1_Time").toString());
                                    maps = DateUtil.getDatePoor(createTime,eTime);
                                    param.put("hrs_normal",Double.valueOf(maps.get("minAll").toString()));
                                    param.put("end1_Time",DateUtil.dateToStrLong(createTime));
                                    str1 = result.get("first_ETime").toString();
                                    str2 = DateUtil.getStringDate().substring(0,11);
                                    str2 =str2+str1;
                                    Date eTime1 = DateUtil.strToDateLong(str2);
                                    maps1 = DateUtil.getDatePoor(createTime,eTime1);

                                    if(Double.valueOf(maps1.get("minAll").toString())>0){
                                        remarks =results.get("remarks").toString()+"0";

                                    }else{
                                        remarks =results.get("remarks").toString()+"1";
                                    }
                                    param.put("remarks",remarks);
                                    workCheckService.updateWorkTime(param);
                                    break;
                                case 3:
                                    param.put("begin2_Time",DateUtil.dateToStrLong(createTime));
                                    str1 = result.get("second_STime").toString();
                                    str2 = DateUtil.getStringDate().substring(0,11);
                                    str2 =str2+str1;
                                    Date bTime = DateUtil.strToDateLong(str2);
                                    maps = DateUtil.getDatePoor(bTime,createTime);
                                    if(Double.valueOf(maps.get("minAll").toString())>0){
                                        remarks =results.get("remarks").toString()+"0";

                                    }else{
                                        remarks =results.get("remarks").toString()+"1";
                                    }
                                    param.put("remarks",remarks);
                                    workCheckService.updateWorkTime(param);
                                    break;
                                case 4:
                                    bTime = DateUtil.strToDateLong(results.get("begin2_Time").toString());
                                    maps = DateUtil.getDatePoor(createTime,bTime);
                                    Double hours = Double.valueOf(results.get("hrs_normal").toString());
                                    param.put("hrs_normal",hours+Double.valueOf(maps.get("minAll").toString()));
                                    param.put("end2_Time",DateUtil.dateToStrLong(createTime));
                                    str1 = result.get("second_ETime").toString();
                                    str2 = DateUtil.getStringDate().substring(0,11);
                                    str2 =str2+str1;
                                    eTime1 = DateUtil.strToDateLong(str2);
                                    maps1 = DateUtil.getDatePoor(createTime,eTime1);
                                    if(Double.valueOf(maps1.get("minAll").toString())>0){
                                        remarks =results.get("remarks").toString()+"0";

                                    }else{
                                        remarks =results.get("remarks").toString()+"1";
                                    }
                                    param.put("remarks",remarks);
                                    workCheckService.updateWorkTime(param);

                                    break;
                                case 5:
                                    param.put("begin3_Time",DateUtil.dateToStrLong(createTime));

                                     str1 = result.get("ext_begin").toString();
                                     str2 = DateUtil.getStringDate().substring(0,11);
                                    str2 =str2+str1;
                                     bTime = DateUtil.strToDateLong(str2);
                                     maps = DateUtil.getDatePoor(bTime,createTime);

                                    if(Double.valueOf(maps.get("minAll").toString())>0){
                                        remarks =results.get("remarks").toString()+"0";

                                    }else{
                                        remarks =results.get("remarks").toString()+"1";
                                    }
                                    param.put("remarks",remarks);
                                    workCheckService.updateWorkTime(param);
                                    break;
                                case 6:
                                     bTime = DateUtil.strToDateLong(results.get("end3_Time").toString());
                                     maps = DateUtil.getDatePoor(createTime,bTime);
                                     hours = Double.valueOf(results.get("hrs_normal").toString());
                                    param.put("hrs_normal",hours+Double.valueOf(maps.get("minAll").toString()));
                                    param.put("end3_Time",DateUtil.dateToStrLong(createTime));

                                     str1 = result.get("ext_end").toString();
                                     str2 = DateUtil.getStringDate().substring(0,11);
                                    str2 =str2+str1;
                                     eTime1 = DateUtil.strToDateLong(str2);
                                     maps1 = DateUtil.getDatePoor(createTime,eTime1);

                                    if(Double.valueOf(maps1.get("minAll").toString())>0){
                                        remarks =results.get("remarks").toString()+"0";

                                    }else{
                                        remarks =results.get("remarks").toString()+"1";
                                    }
                                    param.put("remarks",remarks);
                                    workCheckService.updateWorkTime(param);
                                    break;
                            };
                        }else{
                            param.put("user_id",id);
                            param.put("gid",gid);
                            param.put("expstate",1);
                            param.put("hrs_normal",0);
                            //将考勤组的打卡时间拿出来作对比
                            String str1 = result.get("first_STime").toString();
                            String str2 = DateUtil.getStringDate().substring(0,11);
                            str2 =str2+str1;
                            Date bTime = DateUtil.strToDateLong(str2);
                            Map maps = DateUtil.getDatePoor(bTime,createTime);
                            if(Double.valueOf(maps.get("minAll").toString())>0){
                                param.put("remarks","0");
                            }else{
                                param.put("remarks","1");
                            }
                            param.put("begin1_Time",DateUtil.dateToStrLong(createTime));
                            param.put("create_time",DateUtil.dateToStrLong(createTime));
                            workCheckService.insertWorkStatus(param);
                        }
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
        return flag ? Resp.success(params) : Resp.fail(params);
    }



    public void setShiftJudgment(String user_id , String shift,String ping_type,Long create_time){
        StringBuffer daytime = new StringBuffer(shift);
        if(ping_type.equals("上班")){
            daytime.append(user_id+"s_b");
        }else{
            daytime.append(user_id+"x_b");
        }
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        String count  = stringRedisTemplate.opsForValue().get(daytime.toString());
        if(count!=null&&count.equals("")){
            stringRedisTemplate.opsForValue().set(daytime.toString(),(Integer.valueOf(count)+1)+"");
        }else {
            stringRedisTemplate.opsForValue().set(daytime.toString(),"1");
        }
    }
    public String getShiftJudgment(String user_id , String shift,String ping_type){
        StringBuffer pingCardKey = new StringBuffer(shift);
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        return  stringRedisTemplate.opsForValue().get(pingCardKey.toString());
    }



    public static void main(String[] args) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2019-4-18");
        System.out.println("ss:"+System.currentTimeMillis());
    }

}
