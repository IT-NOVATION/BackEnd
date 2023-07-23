package com.ItsTime.ItNovation.controller.search;

import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.dto.UserSearchRequestDto;
import com.ItsTime.ItNovation.domain.user.dto.UserSearchResponseDto;
import com.ItsTime.ItNovation.domain.user.dto.UserSearchTotalResponseDto;
import com.ItsTime.ItNovation.service.user.UserSearchService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("search")
public class UserSearchController {

   private final UserSearchService userSearchService;



    @GetMapping("/user")
    public ResponseEntity <UserSearchTotalResponseDto> searchTargetUser(@RequestParam(name="userNm") String userName){
        return userSearchService.getTotalResponse(userName);
    }

}
