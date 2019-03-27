package com.demo.c;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages =  {"com.reliable.message.client.feign"})
@EnableKafka
@MapperScan({"com.reliable.message.client.dao","com.demo.c.dao"})
public class ServerDemoC {

	public static void main(String[] args) {
		SpringApplication.run(ServerDemoC.class, args);
	}
}