package com.kakacl.product_service.controller.open.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author wangwei
 * @version v1.0.0
 * @description
 * @date
 */
@RestController
@RequestMapping("/api/open/rest/{version}/fileDownLoadOpen")
@RefreshScope
public class FileDownLoadOpenController extends BaseController {

    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "downloadLocal")
    public void downloadLocal(HttpServletResponse response, String path) throws FileNotFoundException  {
        // 下载本地文件
        String fileName = "app.apk".toString(); // 文件的默认保存名
        // 读到流中
        InputStream inStream = new FileInputStream(path);// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
