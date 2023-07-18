package com.ItsTime.ItNovation.controller.topfollower;

import com.ItsTime.ItNovation.domain.topFollower.TopFollowerResponseDto;
import com.ItsTime.ItNovation.service.topFollower.TopFollowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TopFollowerController {
    private final TopFollowerService topFollowerService;

    @GetMapping("/today/popularUser")
    public ResponseEntity<List<TopFollowerResponseDto>> getTopFollowers() {
        List<TopFollowerResponseDto> topFollowers = topFollowerService.getTopFollowers();
        return ResponseEntity.ok(topFollowers);
    }
}
