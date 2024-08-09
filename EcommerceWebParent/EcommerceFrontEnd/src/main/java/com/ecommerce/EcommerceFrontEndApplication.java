package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages="com.ecommerce")
public class EcommerceFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceFrontEndApplication.class, args);
	}

}
