package com.ItsTime.ItNovation.controller.single;

import com.ItsTime.ItNovation.domain.star.StarRepository;
import com.ItsTime.ItNovation.domain.star.dto.SingleStarEvaluateRequestDto;
import com.ItsTime.ItNovation.service.star.StarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test/single")
public class SingleController {

    private final StarService starService;

    @PostMapping("/starEvaluate")
    public ResponseEntity singleStarEvaluate(@RequestBody SingleStarEvaluateRequestDto singleStarEvaluateRequestDto){
        Long userId = singleStarEvaluateRequestDto.getUserId();

        log.info(String.valueOf(userId));
        return starService.singleStarEvaluate(singleStarEvaluateRequestDto);
    }
}
