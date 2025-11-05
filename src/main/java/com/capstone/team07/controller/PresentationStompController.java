package com.capstone.team07.controller;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.capstone.team07.service.PresentationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PresentationStompController {

	private final SimpMessagingTemplate messagingTemplate;
	private final PresentationService presentationService;

	/**
	 * STT 텍스트를 수신
	 * 경로: /pub/stt
	 */
	@MessageMapping("/stt")
	public void handleSttStream(String spokenText, @Header("presentationId") String presentationId) {

		// 진행 상황 추적 및 중단 판단
		String hintText = presentationService.processSttTextAndCheckInterruption(presentationId, spokenText);

		// 클라이언트에게 푸시
		if (hintText != null && !hintText.isEmpty()) {
			// 구독 주소: /sub/hint/{presentationId}
			String destination = "/sub/hint/" + presentationId;

			// presentationId를 구독하는 클라이언트에게 푸시
			messagingTemplate.convertAndSend(destination, hintText);
		}
	}
}
