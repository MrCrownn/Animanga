package com.animanga.ms_helpdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsHelpdeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsHelpdeskApplication.class, args);
	}
}
