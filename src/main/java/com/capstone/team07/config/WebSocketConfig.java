package com.capstone.team07.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker	// STOMP를 사용하는 웹소켓 메시지 브로커 기능 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	/**
	 * 클라이언트가 웹소켓 연결을 시작할 엔드포인트 등록
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 웹소켓 연결을 위한 엔드포인트 설정 e.g. ws://localhost:8080/ws/presentation
		// 임시로 모든 도메인에서의 접근을 허용
		// TODO: 관련 도메인에서만 접근 가능하게 제한
		registry.addEndpoint("/ws/presentation").setAllowedOriginPatterns("*").withSockJS();
	}

	/**
	 * 메시지 브로커 설정
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {

		// 구독 대상 설정 (서버 -> 클라이언트 push 시 사용 경로)
		config.enableSimpleBroker("/sub");

		// 발행 대상 설정 (클라이언트 -> 서버 전송 시 사용 경로)
		// 서버의 @Controller로 라우팅
		config.setApplicationDestinationPrefixes("/pub");
	}
}
