package com.capstone.team07.dto.Embedding;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimilarityRequestDto {
	@JsonProperty("query_text")
	private String queryText;
}
