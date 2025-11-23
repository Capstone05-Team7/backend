package com.capstone.team07.repository;

import com.capstone.team07.domain.Project;
import com.capstone.team07.domain.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    void deleteByProjectAndSentenceId(Project project, Long sentenceId);
    List<Sentence> findByProjectIdOrderBySentenceIdAsc(Long projectId);

    @Query("SELECT s.sentenceContent FROM Sentence s WHERE s.project.id = :projectId")
    List<String> findSentenceContentsByProjectId(@Param("projectId") Long projectId);

    Optional<Sentence> findByProjectIdAndSentenceId(Long projectId, Long sentenceId);

    Boolean existsByProjectId(Long projectId);

    List<Sentence> findByProjectId(Long projectId);
}
