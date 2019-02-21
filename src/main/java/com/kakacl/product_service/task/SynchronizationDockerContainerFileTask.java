package com.kakacl.product_service.task;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 同步docker容器文件任务
 * @date 2019-02-20
 */

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wangwei
 * @version v1.0.0
 * @description 钱包创建任务
 * @date 2019-02-11
 */
@Component
@Configurable
@EnableScheduling
@RefreshScope
public class SynchronizationDockerContainerFileTask extends BaseTask{

    // docker-names
    @Value("${docker-names}")
    private String docker_names;

    // 一分钟同步一次
//    @Scheduled(cron = "0 */1 *  * * * ")
    public void task(){
        log.info("容器文件同步 {}", System.currentTimeMillis());
        try {
            Process process = null;
            // 运行容器的名称
            String container_names = docker_names;
            // /usr/local/fileData/zzf/ 容器内部的地址
            // /usr/local/fileData/ 宿主机的地址
            String command = "docker cp %s:/usr/local/fileData/zzf/ /usr/local/fileData/";
            command = String.format(command, container_names);
            log.info("command {}", command);
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (Exception e) {
            log.error(" SynchronizationDockerContainerFileTask: {} ", e);
        }
        // docker cp fileserver001:/usr/local/fileData/zzf/tmp/^Cusr/local/fileData/zzf/tmp
    }
}
