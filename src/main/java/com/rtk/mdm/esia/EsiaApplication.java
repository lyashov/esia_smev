package com.rtk.mdm.esia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class EsiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsiaApplication.class, args);
	}

}
