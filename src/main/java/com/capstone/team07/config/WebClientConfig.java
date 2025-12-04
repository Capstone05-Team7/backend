package com.capstone.team07.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	// Python FastAPI 서버의 기본 URL
	private static final String FASTAPI_BASE_URL = "http://3.34.163.79:8000";

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
			.baseUrl(FASTAPI_BASE_URL)
			.build();
	}
}
