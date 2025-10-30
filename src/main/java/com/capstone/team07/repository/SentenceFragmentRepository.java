package com.capstone.team07.repository;

import com.capstone.team07.domain.SentenceFragment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SentenceFragmentRepository extends JpaRepository<SentenceFragment, Long> {
    List<SentenceFragment> findAllBySentence_Project_Id(Long projectId);
}
