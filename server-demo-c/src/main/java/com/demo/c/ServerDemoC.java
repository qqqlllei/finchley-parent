package com.demo.c;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableKafka
@MapperScan({"com.demo.c.dao"})
public class ServerDemoC {

	public static void main(String[] args) {
		SpringApplication.run(ServerDemoC.class, args);
	}
}