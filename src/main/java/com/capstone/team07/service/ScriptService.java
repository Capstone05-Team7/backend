package com.capstone.team07.service;

import com.capstone.team07.domain.Project;
import com.capstone.team07.domain.Script;
import com.capstone.team07.dto.script.ScriptRequestDto;
import com.capstone.team07.dto.script.ScriptResponseDto;
import com.capstone.team07.repository.ProjectRepository;
import com.capstone.team07.repository.ScriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScriptService {
    private final ScriptRepository scriptRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public ScriptResponseDto.ScriptRegisterDto registerScript(ScriptRequestDto.ScriptRegisterDto dto) {
        Project project = projectRepository.findById(dto.getProjectId()).orElse(null);

        List<String> sentences = splitScript(dto.getScript());
        List<ScriptResponseDto.ScriptRegisterDto.SentenceInfo> sentenceInfos = new ArrayList<>();

        long sentenceIdCounter = 1;

        for (String sentence : sentences) {
            // 같은 프로젝트 & sentenceId 존재하면 삭제
            scriptRepository.deleteByProjectAndSentenceId(project, sentenceIdCounter);

            Script savedSentence = scriptRepository.save(Script.builder()
                    .project(project)
                    .sentenceId(sentenceIdCounter)
                    .sentenceContent(sentence)
                    .build()
            );

            sentenceInfos.add(ScriptResponseDto.ScriptRegisterDto.SentenceInfo.builder()
                    .sentenceId(savedSentence.getSentenceId())
                    .sentenceContent(savedSentence.getSentenceContent())
                    .build());

            sentenceIdCounter++;
        }

        return ScriptResponseDto.ScriptRegisterDto.builder()
                .scripts(sentenceInfos)
                .build();
    }

    @Transactional
    public List<ScriptResponseDto.ScriptGetResponseDto> getScript(ScriptRequestDto.ScriptGetRequestDto dto) {

        // 1. 프로젝트 ID로 문장 조회
        List<Script> sentences = scriptRepository.findByProjectIdOrderBySentenceIdAsc(dto.getProjectId());

        // 2. 문장들을 SentenceInfo로 변환
        List<ScriptResponseDto.ScriptGetResponseDto.SentenceInfo> sentenceInfos = new ArrayList<>();
        for (Script sentence : sentences) {
            ScriptResponseDto.ScriptGetResponseDto.SentenceInfo info =
                    ScriptResponseDto.ScriptGetResponseDto.SentenceInfo.builder()
                            .sentenceId(sentence.getSentenceId())
                            .sentenceContent(sentence.getSentenceContent())
                            .build();
            sentenceInfos.add(info);
        }

        // 3. 프로젝트 단위로 Response DTO 생성
        ScriptResponseDto.ScriptGetResponseDto responseDto =
                ScriptResponseDto.ScriptGetResponseDto.builder()
                        .projectId(dto.getProjectId())
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
