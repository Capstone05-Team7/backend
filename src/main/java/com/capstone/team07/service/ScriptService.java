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

        Script savedScript = scriptRepository.save(Script.builder()
                .project(project)
                .sentence_content(dto.getScript())
                .build()
        );
        return ScriptResponseDto.ScriptRegisterDto.builder()
                .script(savedScript.getSentence_content())
                .build();
    }

    @Transactional
    public List<ScriptResponseDto.ScriptDto> getScripts() {
    List<Script> scripts = scriptRepository.findAll();

        List<ScriptResponseDto.ScriptDto> scriptDtoList = scripts.stream()
                .map(script -> ScriptResponseDto.ScriptDto.builder()
                        .scriptId(script.getId())         // Script 엔티티의 ID를 DTO의 scriptId에 매핑
                        .projectId(script.getProject().getId()) // Script 엔티티의 projectId를 DTO에 매핑
                        .script(script.getSentence_content())      // Script 엔티티의 내용(content)을 DTO의 script에 매핑
                        .build())
                .collect(Collectors.toList());

        return scriptDtoList;
    }
}
