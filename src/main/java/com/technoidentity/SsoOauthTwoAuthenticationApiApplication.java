package com.technoidentity;

import com.technoidentity.config.AppProperties;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SsoOauthTwoAuthenticationApiApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SsoOauthTwoAuthenticationApiApplication.class, args);
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SsoOauthTwoAuthenticationApiApplication.class);
	}
	@Bean
	public Mapper mapper() {
		return new DozerBeanMapper();
	}

}
