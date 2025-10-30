package com.capstone.team07.controller;

import com.capstone.team07.apiPayload.ApiResponse;
import com.capstone.team07.dto.sentence.SentenceRequestDto;
import com.capstone.team07.dto.sentence.SentenceResponseDto;
import com.capstone.team07.service.SentenceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scripts")
public class SentenceController {

    private final SentenceService sentenceService;

    @PostMapping()
    @Operation(summary = "스크립트 등록")
    public ApiResponse<SentenceResponseDto.ScriptRegisterDto> registerScript(@RequestBody SentenceRequestDto.ScriptRegisterDto dto) {
        return ApiResponse.onSuccess(sentenceService.registerScript(dto));
    }

    @GetMapping()
    @Operation(summary = "스크립트 리스트 반환")
    public ApiResponse<List<SentenceResponseDto.ScriptGetResponseDto>> getScript(@RequestBody SentenceRequestDto.ScriptGetRequestDto dto) {
        return ApiResponse.onSuccess(sentenceService.getScript(dto));
    }
}
