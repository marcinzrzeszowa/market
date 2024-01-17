package com.mj.market.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;



@ComponentScan
@EnableJpaRepositories
@EntityScan //szuka w innych pakietach
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.mj.market.app")
public class MarketApplication {
	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}

}
