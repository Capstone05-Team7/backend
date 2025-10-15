package com.capstone.team07.repository;

import com.capstone.team07.domain.Project;
import com.capstone.team07.domain.Script;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScriptRepository extends JpaRepository<Script, Long> {
    void deleteByProjectAndSentenceId(Project project, Long sentenceId);
    List<Script> findByProjectIdOrderBySentenceIdAsc(Long projectId);
}
