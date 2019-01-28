package com.kakacl.product_service.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakacl.product_service.config.HttpURLConfig;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author wangwei<br/>
 * @Description: <br/>
 * @date 2018/12/14 11:59<br/>
 * ${TAGS}
 */
public class BackUtils {
    /**
     *
     * @param cardNo 银行卡卡号
     * @return {"bank":"CMB","validated":true,"cardType":"DC","key":"(卡号)","messages":[],"stat":"ok"}
     * 2018年12月14日 12:35:23
     */
    public static String getCardDetail(String cardNo) throws Exception {
        // 创建HttpClient实例
        String url = HttpURLConfig.VALIDATE_AND_CACHE_CARD_INFO_URL;
        url+=cardNo;
        url+="&cardBinCheck=true";
        StringBuilder sb = new StringBuilder();
        URL urlObject = new URL(url);
        URLConnection uc = urlObject.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String inputLine = null;
        while ( (inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String cardNum = "6217002000024612766"; // {"cardType":"DC","bank":"CCB","key":"6217002000024612766","messages":[],"validated":true,"stat":"ok"}
//        cardNum = "6317002000024612766";
//        cardNum = "5254980012170051"; // {"cardType":"CC","bank":"ICBC","key":"1544450272622-6535-11.234.127.162-1933341719","messages":[],"validated":true,"stat":"ok"}
//        cardNum = "5200830004619869"; // {"cardType":"CC","bank":"ABC","key":"1544449796416-6698-11.235.164.50-519718469","messages":[],"validated":true,"stat":"ok"}

        String backResult = BackUtils.getCardDetail(cardNum);
        JSONObject obj = JSON.parseObject(backResult);
        if(obj != null && obj.get("validated").toString().equals("true")) {
            if(obj.get("cardType").toString().toLowerCase().contains("dc")) {
                System.out.println(backResult);
                String bank = obj.get("bank").toString(); // CCB
                String cardType = obj.get("cardType").toString(); // DC 储蓄卡； CC 信用卡 ；
                String validated = obj.get("validated").toString(); // true
                String stat = obj.get("stat").toString(); // ok
                String.format(" bank: %s ,  ", bank);
                System.out.println(String.format(" bank: %s , cardType %s ,  validated %s , stat %s ", bank, cardType, validated, stat));
            } else {
                System.out.println("可能是贷记卡，信用卡");
            }
        } else {
            System.out.println(backResult);
        }
    }
}
