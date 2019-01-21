package com.kakacl.product_service.utils.redis;

/**
 * @author wangwei
 * @version v1.0.0
 * @description
 * @date
 */
public class CacheValidate {

    private long time;
    private int invokeNum;

    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public int getInvokeNum() {
        return invokeNum;
    }
    public void setInvokeNum(int invokeNum) {
        this.invokeNum = invokeNum;
    }

    /*
     *
     * 校验方法是否有效
     */
    public boolean isValidate(int limit){
        this.invokeNum = invokeNum + 1;
        if(System.currentTimeMillis() / 1000 <= time){
            System.err.println(System.currentTimeMillis() / 1000);
            if(invokeNum <= limit){
                return true;
            }
        }else{
            this.invokeNum = 1;
            this.time=System.currentTimeMillis() / 1000;
            return true;
        }
        return false;
    }
}
