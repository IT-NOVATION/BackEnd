package com.ItsTime.ItNovation.controller.user;

import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.dto.UserSearchResponseDto;
import com.ItsTime.ItNovation.service.user.UserSearchService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserSearchController {

   private final UserSearchService userSearchService;



    @PostMapping("/test/search/user")
    public ResponseEntity<List<UserSearchResponseDto>> searchUser(@RequestBody String searchNickName){
        List<UserSearchResponseDto> response = new ArrayList<>();
        try {
            response = userSearchService.getResponse(searchNickName);
            return ResponseEntity.status(200).body(response);

        }catch (Exception e){
            return ResponseEntity.status(400).body(response);
        }
    }

}
