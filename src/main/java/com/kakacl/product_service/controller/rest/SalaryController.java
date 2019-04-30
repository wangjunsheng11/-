package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author wangwei
 * @version v1.0.0
 * @description 员工的工资条访问层
 * @date 2019-4-3
 */
@RestController
@RequestMapping("/api/rest/{version}/salary")
public class SalaryController {


  /*  @GetMapping(value="getSalary",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map getSalary(){

    }*/



public static void main(String[] args){
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");

    Object object=null;
    Object object1=null;
    String str =String.valueOf(object);
    System.out.println(str.length()+str);


    Calendar calendar = Calendar.getInstance();

    calendar.set(calendar.HOUR_OF_DAY,00);
    calendar.set(calendar.MINUTE,00);
    calendar.set(calendar.SECOND,00);
    int data =calendar.get(calendar.DATE);
    System.out.println("当前日期:"+calendar.getTime().getTime());

    System.out.println("当前日期:"+simpleDateFormat.format(calendar.getTime()));
    Long time=1555286798L;
    Long time1=1554888797L;
    calendar.setTimeInMillis(time1 * 1000);
    Date date1=new Date(time );
    Date date = calendar.getTime();
    System.out.println( "打卡时间："+simpleDateFormat.format(date1));
    System.out.println( "打卡时间2："+simpleDateFormat1.format(date1).replaceAll(":",""));
    System.out.println( simpleDateFormat.format(date));
    System.out.println(System.currentTimeMillis());
    System.out.println(System.currentTimeMillis()/1000);





 }

}
