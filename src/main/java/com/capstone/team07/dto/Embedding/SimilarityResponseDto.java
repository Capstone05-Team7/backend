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
public class SimilarityResponseDto {
	// 가장 유사한 스크립트 문장
	@JsonProperty("most_similar_id")
	private String mostSimilarId;

	// 유사도 점수 (float)
	@JsonProperty("similarity_score")
	private float similarityScore;

	// 쿼리 처리 시간 (float)
	@JsonProperty("query_process_time")
	private float queryProcessTime;
}
