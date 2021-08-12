package com.reports;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("com")
@SpringBootApplication
public class DynamicReportsAppApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(DynamicReportsAppApplication.class);

		builder.headless(false);

		ConfigurableApplicationContext context = builder.run(args);
	}
}
