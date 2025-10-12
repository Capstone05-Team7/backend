package com.capstone.team07.service;

import com.capstone.team07.apiPayload.ApiResponse;
import com.capstone.team07.dto.speech.SpeechRequestDto;
import com.capstone.team07.dto.speech.SpeechResponseDto;
import com.capstone.team07.repository.ScriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpeechService {

    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    private final ScriptRepository scriptRepository;

    public SpeechResponseDto getresult(SpeechRequestDto dto) {
        long start = System.currentTimeMillis();

        List<String> contents = scriptRepository.findSentenceContentsByProjectId(dto.getProjectNumber());

        String inputPrompt = """
                스크립트:
                %s

                입력:
                %s
                현재까지의 입력이야. 입력을 토대로 스크립트를 보고 다음 내용이 무엇인지 문장만을 알려줘.
                """.formatted(contents, dto.getInput());

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", inputPrompt
        );
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(message)
        );

        Map responseBody = webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();  // 여기서만 블로킹 (최종 응답 대기)

        System.out.println(responseBody);

        String next = null;

        if (responseBody != null && responseBody.containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> assistantMessage = (Map<String, Object>) choices.get(0).get("message");
                if (assistantMessage != null && assistantMessage.containsKey("content")) {
                    next = assistantMessage.get("content").toString();
                }
            }
        }

        long time = System.currentTimeMillis() - start;
        return SpeechResponseDto.fromentity(next, time);
    }
}
