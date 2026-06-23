package com.animanga.ms_library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsLibraryApplication.class, args);
	}
}
