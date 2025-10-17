package com.capstone.team07.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Table(
        name = "script",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "sentenceId"})
)
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private Long sentenceId;
    private String sentenceContent;

    @ElementCollection
    private Set<String> keyword = new HashSet<>();
}
