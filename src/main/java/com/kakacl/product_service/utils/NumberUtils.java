package com.kakacl.product_service.utils;

import com.kakacl.product_service.config.Constants;

import java.util.Random;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 数字工具类
 * @date 2019-01-10
 */
public class NumberUtils {

    /*
     *
     * 获取指定数值内的随机数，包含传入的开始值和结束值
     * @author wangwei
     * @date 2019/1/10
      * @param startWith 开始值
     * @param entWith 结束值
     * @return int 返回一个随机数
     */
    public static int getRandomNumber(int startWith, int entWith) {
        return new Random().nextInt(entWith - startWith + Constants.CONSTANT_1) + startWith;
    }

    /*
     *
     * 获取当前的时间秒
     * @author wangwei
     * @date 2019/1/10
      * @param
     * @return int
     */
    public static int getCurrentTimes () {
        return Integer.parseInt(System.currentTimeMillis() / Constants.CONSTANT_1000 + "");
    }

}
