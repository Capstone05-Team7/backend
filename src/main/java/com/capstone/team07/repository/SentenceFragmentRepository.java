package com.capstone.team07.repository;

import com.capstone.team07.domain.SentenceFragment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentenceFragmentRepository extends JpaRepository<SentenceFragment, Long> {
    List<SentenceFragment> findAllBySentence_Project_Id(Long projectId);


    @Query("SELECT sf FROM SentenceFragment sf JOIN sf.sentence s WHERE s.project.id = :projectId")
    List<SentenceFragment> findByProjectId(@Param("projectId") Long projectId);
}
