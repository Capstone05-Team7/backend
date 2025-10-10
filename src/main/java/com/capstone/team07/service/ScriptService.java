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
}
