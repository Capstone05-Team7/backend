package com.capstone.team07.controller;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.capstone.team07.dto.Embedding.ProgressRequestDto;
import com.capstone.team07.dto.Embedding.ProgressResponseDto;
import com.capstone.team07.dto.Embedding.SimilarityResponseDto;
import com.capstone.team07.service.PresentationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class PresentationStompController {

	private final SimpMessagingTemplate messagingTemplate;
	private final PresentationService presentationService;

	/**
	 * STT 텍스트를 수신
	 * 경로: /pub/stt
	 */
	@MessageMapping("/stt")
	public void handleSttStream(String spokenText, @Header("presentationId") String presentationId) {

		// 현재 문장 추적
		SimilarityResponseDto responseDto = presentationService.calculateSimilarity(spokenText);

		log.info("-------여긴데 {}", responseDto);

		// 클라이언트에게 푸시
		// 구독 주소: /sub/current/{presentationId}
		if (responseDto != null){
			String destination = "/sub/current/" + presentationId;

			// presentationId를 구독하는 클라이언트에게 푸시
			messagingTemplate.convertAndSend(destination, responseDto);
		}
	}

	/**
	 * STT 텍스트를 수신(for 진행률 판단)
	 * 경로: /pub/stt/progress
	 */
	@MessageMapping("/stt/progress")
	public void handleSttStreamForProgress(ProgressRequestDto payload, @Header("presentationId") String presentationId) {
		// 진행률 계산
		ProgressResponseDto progress = presentationService.calculateProgress(payload.getTargetScriptId(), payload.getTargetScriptText(), payload.getQueryText());

		log.info("$$$$$$$$여긴데 {}", payload);
		log.info("++++++++여긴데 {}", progress);

		// 클라이언트에게 푸시
		// 구독 주소: /sub/progress/{presentationId}
		String destination = "/sub/progress/" + presentationId;

		// presentationId를 구독하는 클라이언트에게 푸시
		messagingTemplate.convertAndSend(destination, progress);
	}
}
