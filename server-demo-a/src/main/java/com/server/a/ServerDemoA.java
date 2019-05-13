package com.server.a;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@MapperScan({"com.server.a.dao"})
public class ServerDemoA {
	public static void main(String[] args) {
		SpringApplication.run(ServerDemoA.class, args);
	}

}