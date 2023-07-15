package com.ItsTime.ItNovation.controller.top;


import com.ItsTime.ItNovation.service.top.TopKeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TopKeywordController {

    private final TopKeywordService topKeywordService;

    @GetMapping("/top/moviekeyword/{movieId}")
    public ResponseEntity topKeyword(@PathVariable Long movieId){
        return topKeywordService.findTop3Feature(movieId);
    }



}
