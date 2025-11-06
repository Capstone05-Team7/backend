package com.capstone.team07.service;

import com.capstone.team07.apiPayload.ApiResponse;
import com.capstone.team07.domain.Project;
import com.capstone.team07.domain.Sentence;
import com.capstone.team07.domain.SentenceFragment;
import com.capstone.team07.dto.sentence.SentenceRequestDto;
import com.capstone.team07.dto.sentence.SentenceResponseDto;
import com.capstone.team07.repository.ProjectRepository;
import com.capstone.team07.repository.SentenceRepository;
import com.capstone.team07.repository.SentenceFragmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SentenceService {
    private final ProjectRepository projectRepository;
    private final SentenceRepository sentenceRepository;
    private final SentenceFragmentRepository sentenceFragmentRepository;

    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @Transactional
    public ApiResponse<Void> registerScript(SentenceRequestDto.ScriptRegisterDto dto) {

        // 1번과정 : 스크립트를 입력받으면 스크립트를 문장별로 추출해서 저장해놓기. (to ScriptRepository (예비용))
        Project project = projectRepository.findById(dto.getProjectId()).orElse(null);

        List<String> sentences = splitScript(dto.getScript());

        long sentenceIdCounter = 1;

        for (String sentence : sentences) {
            // 같은 프로젝트 & sentenceId 존재하면 삭제
            sentenceRepository.deleteByProjectAndSentenceId(project, sentenceIdCounter);

            Sentence savedSentence = sentenceRepository.save(Sentence.builder()
                    .project(project)
                    .sentenceId(sentenceIdCounter)
                    .sentenceContent(sentence)
                    .build()
            );
            sentenceIdCounter++;
        }

        // 2번과정 : 추출된 문장들을 ChatGPT AI를 통해 의미 단위를 기준으로 키워드 받아오기.
        List<Sentence> contents = sentenceRepository.findByProjectIdOrderBySentenceIdAsc(dto.getProjectId());

        // 문장번호 붙이기
        StringBuilder numberedContents = new StringBuilder();
        for (int i = 0; i < contents.size(); i++) {
            numberedContents.append(i + 1) // 문장번호 1부터 시작
                    .append(" : ")
                    .append(contents.get(i).getSentenceContent())
                    .append("\n");
        }

        String inputPrompt = """
                스크립트:
                %s
                
                이게 스크립트인데 이걸 중요한 의미단위로 나누고 의미단위별로 키워드를 추출해줄래
                내용을 요약하지 말고 <문장번호> <문장에서의 의미단위 번호> <스크립트 내용> <내용에 대한 키워드> 
                이 포맷으로 해서 예를 들면 
                1 / 1 / 하지만 스마트폰을 너무 오래 사용하면 / 오래 사용 
                1 / 2 / 집중력이 떨어지고 피로해질 수 있습니다. / 집중력 저하 
                이런식으로 해서 답변만줘
                """.formatted(numberedContents);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", inputPrompt
        );
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(message)
        );

        Map<String, Object> responseBody = webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();  // 최종 응답 대기

        System.out.println(responseBody);

        // 3번과정 : 받아온 키워드 저장하기. (to SentenceFrg.repo)
        if (responseBody != null && responseBody.containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> messageMap = (Map<String, Object>) choices.get(0).get("message");
                if (messageMap != null && messageMap.containsKey("content")) {
                    String content = messageMap.get("content").toString();

                    // content를 줄 단위로 나누고 바로 DB 저장
                    String[] lines = content.split("\\n");
                    for (String line : lines) {
                        String[] parts = line.split(" / ");
                        if (parts.length == 4) {
                            Long sentenceId = Long.parseLong(parts[0].trim());
                            Long sentenceOrder = Long.parseLong(parts[1].trim());
                            String sentenceFragmentContent = parts[2].trim();
                            String keyword = parts[3].trim();

                            Sentence sentence = sentenceRepository
                                    .findByProjectIdAndSentenceId(dto.getProjectId(), sentenceId)
                                    .orElseThrow(() -> new RuntimeException("해당 문장을 찾을 수 없습니다."));

                            // 바로 저장
                            SentenceFragment fragment = sentenceFragmentRepository.save(SentenceFragment.builder()
                                    .sentenceId(sentenceId)
                                    .sentence(sentence)
                                    .sentenceOrder(sentenceOrder)
                                    .sentenceFragmentContent(sentenceFragmentContent)
                                    .keyword(keyword).build());
                            sentenceFragmentRepository.save(fragment);
                        }
                    }
                }
            }
        }

        return ApiResponse.onSuccess(null);
    }

    @Transactional
    public List<SentenceResponseDto.ScriptGetResponseDto> getScript(Long projectId) {

        // 1. 프로젝트 ID로 문장 조각 조회
        List<SentenceFragment> fragments = sentenceFragmentRepository.findAllBySentence_Project_Id(projectId);

        // 2. 문장들을 SentenceInfo로 변환
        List<SentenceResponseDto.ScriptGetResponseDto.SentenceInfo> sentenceInfos = new ArrayList<>();
        for (SentenceFragment sentenceFragment : fragments) {
            SentenceResponseDto.ScriptGetResponseDto.SentenceInfo info =
                    SentenceResponseDto.ScriptGetResponseDto.SentenceInfo.builder()
                            .sentenceId(sentenceFragment.getSentenceId())
                            .sentenceOrder(sentenceFragment.getSentenceOrder())
                            .sentenceFragmentContent(sentenceFragment.getSentenceFragmentContent())
                            .keyword(sentenceFragment.getKeyword())
                            .build();
            sentenceInfos.add(info);
        }

        // 3. 프로젝트 단위로 Response DTO 생성
        SentenceResponseDto.ScriptGetResponseDto responseDto =
                SentenceResponseDto.ScriptGetResponseDto.builder()
                        .projectId(projectId)
                        .scripts(sentenceInfos)
                        .build();

        // 4. 리스트로 감싸서 반환 (프로젝트 하나 기준이니까 하나만 들어있음)
        return List.of(responseDto);
    }

    private List<String> splitScript(String script) {
        // 단순한 구분 기준 (.?!)
        return Arrays.stream(script.split("(?<=[.!?])\\s+"))
                .filter(s -> !s.isBlank())
                .toList();
    }
}
