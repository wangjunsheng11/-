package com.kakacl.product_service.controller.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.AccountService;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.io.*;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 文件上传控制器
 * @date 2019-02-10
 */
@RestController
@RequestMapping("/api/rest/{version}/fileupload")
@RefreshScope
public class FileUpLoadController extends BaseController {

    @Autowired
    private AccountService accountService;

    /*
     * 头像文件上传
     *
     * @author wangwei
     * @date 2019/2/10
      * @param request
     * @param token
     * @param time
     * @return com.kakacl.product_service.utils.Resp
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "headUpLoad", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp headUpLoad(HttpServletRequest request,
                           @RequestParam("file") CommonsMultipartFile file,
                           String token,
                           String time,
                           java.util.Map params) {
        String user_id = getUserid(request);
        String path = "D:"+ File.separator +"headimages" + File.separator + user_id + ".jpg";
        try {
            java.io.File newFile = new java.io.File(path);
            //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
            file.transferTo(newFile);
            params.put("id", user_id);
            params.put("head_path", path);
            accountService.updateHead(params);
        } catch (Exception e) {
            log.error("error: {}", e.getMessage());
            return Resp.fail();
        }
        return Resp.success();
    }

    @RequestMapping("springUpload")
    public String  springUpload(HttpServletRequest request) throws Exception
    {
        long  startTime=System.currentTimeMillis();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();

            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                if(file!=null)
                {
                    String path = "D:/springUpload"+file.getOriginalFilename();
                    //上传
                    file.transferTo(new java.io.File(path));
                }
            }
        }
        long  endTime=System.currentTimeMillis();
        System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");
        return "/success";
    }

}
