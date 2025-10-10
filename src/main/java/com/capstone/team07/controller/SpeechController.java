package com.capstone.team07.controller;

import com.capstone.team07.dto.speech.SpeechRequestDto;
import com.capstone.team07.dto.speech.SpeechResponseDto;
import com.capstone.team07.service.SpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/speech")
public class SpeechController {

    private final SpeechService speechService;

    @PostMapping
    public ResponseEntity<SpeechResponseDto> createProduct(@RequestBody SpeechRequestDto dto) {
        return ResponseEntity.ok(speechService.getresult(dto));
    }
}
