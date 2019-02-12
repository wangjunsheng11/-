package com.kakacl.product_service.utils;

import java.io.File;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 文件工具类
 * @date
 */
public class FileUtils {

    public static boolean haveCreatePath = false;

    public static String getFileUploadPath(String fileUploadPath) {
        //判断有没有结尾符,没有得加上
        if (!fileUploadPath.endsWith(File.separator)) {
            fileUploadPath = fileUploadPath + File.separator;
        }
        //判断目录存不存在,不存在得加上
        if (!haveCreatePath) {
            File file = new File(fileUploadPath);
            file.mkdirs();
            haveCreatePath = true;
        }
        return fileUploadPath;
    }
}
