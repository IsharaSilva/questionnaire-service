package com.xitricon.questionnaireservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.xitricon.questionnaireservice.audit.AuditorAwareImpl;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
	@Bean
	AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}
}
