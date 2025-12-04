package com.capstone.team07.controller;

import com.capstone.team07.apiPayload.ApiResponse;
import com.capstone.team07.dto.start.StartRequestDto;
import com.capstone.team07.service.StartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/start")
public class StartController {

    private final StartService startService;

    @PostMapping
    public ApiResponse<String> startPresentation(@RequestBody StartRequestDto requestDto) {
        startService.processAndSendSentences(requestDto.getProjectId());
        return ApiResponse.onSuccess("Sentence processing has been initiated.");
    }
}
