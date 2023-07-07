package com.ItsTime.ItNovation.controller.topuser;


import com.ItsTime.ItNovation.service.best.TodayBestUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TopUserController {

    private final TodayBestUserService todayBestUserService;

    @GetMapping("/today/topUser")
    public ResponseEntity todayTopUser(){
        return todayBestUserService.getBestUserInfo();
    }


}
