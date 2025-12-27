package com.capstone.team07.dto.Embedding;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ProgressResponseDto {
	@JsonProperty("next_script_id")
	private String nextScriptId;

	@JsonProperty("similarity_score")
	private Float similarityScore;
}

