package com.animanga.ms_social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsSocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSocialApplication.class, args);
	}
}
