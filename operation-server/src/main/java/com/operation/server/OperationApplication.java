package com.operation.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by 李雷 on 2019/1/15.
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class OperationApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperationApplication.class,args);
    }

}
