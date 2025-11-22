package com.capstone.team07.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentenceFragment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sentence_Id")
    private Sentence sentence;

    @Column(name = "fragment_sentence_id")
    private Long sentenceId;

    private Long sentenceOrder;
    private String sentenceFragmentContent;
    private String keyword;
    private String image;
}
