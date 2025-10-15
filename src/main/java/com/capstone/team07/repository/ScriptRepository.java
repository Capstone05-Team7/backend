package com.capstone.team07.repository;

import com.capstone.team07.domain.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScriptRepository extends JpaRepository<Script, Long> {
    @Query("SELECT s.sentence_content FROM Script s WHERE s.project.id = :projectId")
    List<String> findSentenceContentsByProjectId(@Param("projectId") Long projectId);
}
