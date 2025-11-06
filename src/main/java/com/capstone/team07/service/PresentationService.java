package com.capstone.team07.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import com.capstone.team07.dto.speech.SpeechRequestDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PresentationService {
	private final SpeechService speechService;

	// (실제 환경에서는 Redis 등으로 관리 필요)
	// 발표 ID별 현재 진행 상태를 저장
	private final Map<String, PresentationState> presentationStateMap = new ConcurrentHashMap<>();

	/**
	 * 발표가 시작되거나 (최초 STT 메시지 수신 등) 초기화가 필요할 때 상태를 준비합니다.
	 * @param presentationId 발표 자료의 고유 ID
	 */
	public void ensurePresentationStateExists(String presentationId) {
		// 해당 presentationId의 상태가 없으면 새로운 상태 객체를 생성하여 저장
		presentationStateMap.putIfAbsent(presentationId, new PresentationState());
	}

	/**
	 * 실시간으로 들어오는 STT 텍스트를 처리하고, 중단 감지 시 힌트를 반환합니다.
	 * @param presentationId 발표 ID
	 * @param spokenText 현재 시점까지 발언한 텍스트 (또는 스트리밍 조각)
	 * @return 힌트 텍스트 (중단 시), null (정상 진행 시)
	 */
	public String processSttTextAndCheckInterruption(String presentationId, String spokenText) {

		// 발표 상태 준비 확인
		ensurePresentationStateExists(presentationId);

		// 현재 발표 상태 로딩
		PresentationState state = presentationStateMap.get(presentationId);

		// 진행 상황 추적, 중단 여부 판단
		boolean isInterrupted = checkInterruption(state);

		if (isInterrupted) {
			// 발표가 중단되면 힌트 로딩
			return callLlmForHint(state, spokenText, presentationId);
		}

		// 마지막 메시지 수신 시간 갱신
		state.updateLastActivityTime();

		return null; // 정상 진행 중
	}

	// 발표 상태 저장
	private static class PresentationState {
		private long lastMessageTime; // 마지막 STT 메시지 수신 시간

		public PresentationState() {
			this.lastMessageTime = System.currentTimeMillis();
		}

		public void updateLastActivityTime() {
			this.lastMessageTime = System.currentTimeMillis();
		}

		// TODO: 현재 문장 인덱스 등 추가
	}

	// 발표 중단 감지
	private boolean checkInterruption(PresentationState state) {
		final long MAX_SILENCE_MS = 5000L; // 중단 판단 기준 TODO: 의도한 시간보다는 짧게 설정하여 미리 로딩되게 해놓고 캐싱하는 과정 필요

		if ((System.currentTimeMillis() - state.lastMessageTime) > MAX_SILENCE_MS) {
			// 일정 시간 동안 메시지가 들어오지 않으면 중단으로 간주
			return true;
		}

		return false;
	}

	// LLM 호출
	private String callLlmForHint(PresentationState state, String spokenText, String presentationId) {

		SpeechRequestDto dto = SpeechRequestDto.builder().
			projectNumber(Long.parseLong(presentationId))
			.input(spokenText)
			.build();

		return speechService.getresult(dto).getNext();
	}
}
