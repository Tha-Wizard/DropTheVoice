package com.ssafy.a505.domain.controller;

import com.ssafy.a505.domain.entity.Voice;
import com.ssafy.a505.domain.repository.VoiceRepository;
import com.ssafy.a505.domain.service.VoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-storage")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class StorageController {

    private final VoiceService voiceService;
    private final VoiceRepository voiceRepository;

    @GetMapping("spread/{page}/{size}")
    public ResponseEntity<?> findAllWithPage(@RequestParam("memberId") Long memberId, @PathVariable("page") int page, @PathVariable("size") int size){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        log.info("findAllWithPage page={}", page - 1);
        List<Voice> result = voiceRepository.findByMemberWithSpread(memberId, pageRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("heart/{page}/{size}")
    public ResponseEntity<?> findLikeWithPage(@RequestParam("memberId") Long memberId, @PathVariable("page") int page, @PathVariable("size") int size) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        log.info("findAllWithPage page={}", page - 1);
        List<Voice> result = voiceRepository.findByMemberWithHeart(memberId, pageRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
