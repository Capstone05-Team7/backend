package com.capstone.team07.service;

import com.capstone.team07.domain.Sentence;
import com.capstone.team07.dto.start.ScriptDataDto;
import com.capstone.team07.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StartService {

    private final SentenceRepository sentenceRepository;
    private final WebClient webClient;

    @Transactional(readOnly = true)
    public void processAndSendSentences(Long projectId) {
        // 1. DB에서 Sentence 엔티티 리스트를 조회합니다.
        List<Sentence> sentences = sentenceRepository.findByProjectId(projectId);

        // 2. Sentence 엔티티 리스트를 ScriptDataDto 리스트로 변환합니다.
        List<ScriptDataDto> scriptDataDtos = sentences.stream()
                .map(sentence -> new ScriptDataDto(
                        String.valueOf(sentence.getSentenceId()),
                        sentence.getSentenceContent()
                ))
                .collect(Collectors.toList());

        // 3. 변환된 DTO 리스트를 파이썬 서버로 전송합니다.
        if (!scriptDataDtos.isEmpty()) {
            sendScriptToPythonServer(scriptDataDtos);
        }
    }

    private void sendScriptToPythonServer(List<ScriptDataDto> scriptDataDtos) {
        // 요청 본문을 {"script": [ ... ]} 구조로 만듭니다.
        Map<String, List<ScriptDataDto>> requestBody = Collections.singletonMap("script", scriptDataDtos);

        // WebClient를 사용하여 비동기 POST 요청을 보냅니다.
        webClient.post()
                .uri("/embed-sentences")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기식으로 결과를 기다립니다.
    }
}
