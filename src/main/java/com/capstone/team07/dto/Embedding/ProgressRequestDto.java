package com.capstone.team07.dto.Embedding;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressRequestDto {
	@JsonProperty("target_script_id")
	private String targetScriptId;

	@JsonProperty("target_script_text")
	private String targetScriptText;

	@JsonProperty("query_text")
	private String queryText;
}


