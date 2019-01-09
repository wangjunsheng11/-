package com.kakacl.product_service.utils;

import java.util.Random;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 唯一Code生成方法
 * @date 2019-01-09
 */
public class IDUtils {
    /*
     *
     * 当前时间戳加三位
     * @author wangwei
     * @date 2019/1/9
      * @param
     * @return java.lang.String
     */
    public static String genHadId() {
        long millis = System.currentTimeMillis();
        Random random = new Random();
        // 从99开始到999
        int end3 = random.nextInt(999) + 99;
        String str = millis + String.format("%03d", end3);
        return str;
    }

    /*
     *
     * 当前时间戳加二位
     * @author wangwei
     * @date 2019/1/9
     * @param
     * @return java.lang.String
     */
    public static long genTwoId() {
        long millis = System.currentTimeMillis();
        Random random = new Random();
        // 从10开始到99
        int end2 = random.nextInt(99) + 10;
        //不足两位前面补0
        String str = millis + String.format("%02d", end2);
        long id = new Long(str);
        return id;
    }
}
