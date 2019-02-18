package com.kakacl.product_service.controller.open.rest;

import com.kakacl.product_service.config.Constants;
import com.kakacl.product_service.controller.base.BaseController;
import com.kakacl.product_service.limiting.AccessLimit;
import com.kakacl.product_service.service.AccountService;
import com.kakacl.product_service.utils.FileUtils;
import com.kakacl.product_service.utils.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 文件上传控制器
 * @date 2019-02-10
 */
@RestController
@RequestMapping("/api/open/rest/{version}/fileUpLoadOpen")
@RefreshScope
//@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
public class FileUpLoadOpenController extends BaseController {

    @Value("${up-load-file-path}")
    private String upLoadFilePath;

    @Value("${file-upload-ip-and-port}")
    private String fileUploadIpAndPort;


    @Autowired
    private AccountService accountService;

    /**
     * showdoc
     * @catalog v1.0.1/用户相关
     * @title 用户头像上传 - 备用
     * @description 用户头像上传,类型指定multipart/form-data
     * @method post
     * @url /api/open/rest/v1.0.1/fileUpLoadOpen/headUpLoad
     * @param token 必选 string token
     * @param time 必选 string time
     * @param file 必选 CommonsMultipartFile file，文件对象
     * @return {"status":"200","message":"请求成功","data":{"id":"1547006424247526","head_path":"http://211.149.226.29:8081/fileData\\headFiles\\1547006424247526.jpg"},"page":null,"ext":null}
     * @return_param code int 验证码
     * @return_param status string 状态
     * @remark 这里是备注信息
     * @number 99
     */
    @AccessLimit(limit = Constants.CONSTANT_10,sec = Constants.CONSTANT_10)
    @RequestMapping(value = "headUpLoad")
    public Resp headUpLoad(HttpServletRequest request,
                           @RequestParam("file") MultipartFile file,
                           String time,
                           @RequestParam("token")String token,
                           java.util.Map params) {
        String user_id = getUserid(request);
        String file_dir = "headFiles";
        String image_name = user_id + ".jpg";
        String path = FileUtils.getFileUploadPath(upLoadFilePath + File.separator + file_dir) + image_name;
        String head_path = fileUploadIpAndPort + File.separator + FileUtils.getFileUploadPath(file_dir) + image_name;
        log.info("path: {}", path);
        log.info("head_path: {}", head_path);
        try {
            File newFile = new File(path);
            file.transferTo(newFile);
            params.put("id", user_id);
            params.put("head_path", head_path);
            accountService.updateHead(params);
        } catch (Exception e) {
            log.error("error: {}", e.getMessage());
            return Resp.fail(params);
        }
        return Resp.success(params);
    }

    @RequestMapping("springUpload")
    public String  springUpload(HttpServletRequest request) throws Exception {
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
                    file.transferTo(new File(path));
                }
            }
        }
        long  endTime=System.currentTimeMillis();
        System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");
        return "/success";
    }

}
