package com.ItsTime.ItNovation.controller.top;


import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.top.TopKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/top")
@RequiredArgsConstructor
public class TopKeywordController {
    private final TopKeywordService topKeywordService;

    @GetMapping("/moviekeyword/{movieId}")
    public ResponseEntity topKeyword(@PathVariable Long movieId){
        return topKeywordService.findTop3Feature(movieId);
    }
}
