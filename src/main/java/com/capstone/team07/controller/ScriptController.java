package com.capstone.team07.controller;

import com.capstone.team07.apiPayload.ApiResponse;
import com.capstone.team07.dto.script.ScriptRequestDto;
import com.capstone.team07.dto.script.ScriptResponseDto;
import com.capstone.team07.service.ScriptService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scripts")
public class ScriptController {

    private final ScriptService scriptService;

    @PostMapping()
    @Operation(summary = "스크립트 등록")
    public ApiResponse<ScriptResponseDto.ScriptRegisterDto> registerScript(@RequestBody ScriptRequestDto.ScriptRegisterDto dto) {
        return ApiResponse.onSuccess(scriptService.registerScript(dto));
    }
}
