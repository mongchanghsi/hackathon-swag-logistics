package com.ericmonghackathonlogisticsswag.HackathonLogisticsSWAG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class HackathonLogisticsSwagApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackathonLogisticsSwagApplication.class, args);
		System.out.println("System has started!");
	}

}
