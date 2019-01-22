package com.kakacl.product_service.test;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 签名测试
 * @date 2019-01-22
 */
public class SignTest {

    /** 加密密钥 */
    public String appkey = "myprovitekey";

    // 间隔时间
    public int timeout = 1 * 30 * 1000;

    /*
     *
     * 测试sign签名的的测试方法
     *
     * 参数 time sign 不在签名参数中。
     * 修改参数会修改sign参数才会成功。
     *
     * @author zzf@kakacl.com
     * @date 2019/1/22
      * @param  sign 签名
     * @return void
     */
    @Test
    public void testSign() {
        Map params = new HashMap();
        params.put("sign", "f34440e6a59729220aadd6fa843e9ba6");
        params.put("time", System.currentTimeMillis() + "");
        params.put("secretKey", "mysecret123456");
        System.out.println(verify(params));;
    }

    public Map verify(Map<String, String> params) {
        Map result = new HashMap();
        String sign = "";
        if (params.get("sign") != null) {
            sign = params.get("sign");
        }else {
            result.put("sign", "sign is null");
            return result;
        }
        String timestamp = "";
        if (params.get("time") != null) {
            timestamp = params.get("time");
        }else {
            result.put("time", "time is null");
            return result;
        }
        //过滤空值、sign
        Map<String, String> sParaNew = paraFilter(params);
        //获取待签名字符串
        String preSignStr = createLinkString(sParaNew);
        //获得签名验证结果
        String mysign = DigestUtils.md5Hex(getContentBytes(preSignStr + appkey, "UTF-8"));
        if (mysign.equals(sign)) {
            //是否超时
            long curr = System.currentTimeMillis();
            if ((curr - Long.valueOf(timestamp)) > timeout){
                result.put("time", "api is time out " + curr);
                result.put("sysTime", curr);
                result.put("sysSign", mysign);
                return result;
            }
            result.put("time", "sign success .");
            result.put("sysSign", mysign);
            return result;
        } else {
            result.put("sysSign", mysign);
            result.put("sysSign_tips", "sign error .");
            return result;
        }
    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public String createLinkString(Map<String, String> params) {
        return createLinkString(params, false);
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @param encode 是否需要UrlEncode
     * @return 拼接后字符串
     */
    public String createLinkString(Map<String, String> params, boolean encode) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode)
                // value = urlEncode(value, INPUT_CHARSET);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 编码转换
     * @param content
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}
