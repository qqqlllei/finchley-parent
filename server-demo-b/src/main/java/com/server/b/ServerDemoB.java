package com.server.b;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
//@EnableDiscoveryClient
//@EnableFeignClients(basePackages =  {"com.reliable.message.client.feign"})
@EnableKafka
@MapperScan({"com.reliable.message.client.dao","com.server.b.dao"})
public class ServerDemoB {

	public static void main(String[] args) {
		SpringApplication.run(ServerDemoB.class, args);
	}
}