package com.ItsTime.ItNovation.service.user;


import com.ItsTime.ItNovation.common.exception.UnauthorizedException;
import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.follow.FollowState;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.SearchUserReviewDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.domain.user.dto.UserSearchResponseDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ItsTime.ItNovation.domain.user.dto.UserSearchTotalResponseDto;
import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSearchService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;
    private String nowUserEmail = null;
    private final JwtService jwtService;


    @Transactional
    public List<UserSearchResponseDto> getResponse(String searchNickName) {
        List<User> findByNickName = userRepository.findByNickname(searchNickName);
        if(findByNickName.isEmpty()){
            log.error("유저가 없습니다.");
            return Collections.emptyList();
        }
        try {
            List<UserSearchResponseDto> userSearchResponseDtos = madeResponseDto(findByNickName);
            return userSearchResponseDtos;

        }catch (Exception e){
            throw new RuntimeException("오류가 발생했습니다.");
        }

    }

    private List<UserSearchResponseDto> madeResponseDto(List<User> findByNickName) {
        List<UserSearchResponseDto> userSearchResponseDtos = new ArrayList<>();

        for(User u : findByNickName){
            UserSearchResponseDto userSearchResponseDto = UserSearchResponseDto.builder()
                    .userId(u.getId())
                    .nickName(u.getNickname())
                    .isMyProfile(profileState(nowUserEmail, u.getId()))
                    .isNowUserFollowThisUser(followState(nowUserEmail, u.getId()))
                    .userImg(u.getProfileImg())
                    .introduction(u.getIntroduction())
                    .reviews(getSearchReviewResponse(u))
                    .build();

            userSearchResponseDtos.add(userSearchResponseDto);
        }
        return userSearchResponseDtos;
    }

    private Boolean followState(String nowUserEmail, Long userId) {
        Optional<User> user = userRepository.findByEmail(nowUserEmail);

        /**
         * 팔로우 하면 true
         * 아니면 false ( 같은 유저여도 false)
         */
        if (user.isPresent()) {
            Optional<FollowState> followState = followRepository.findByPushUserAndFollowUser(user.get().getId(), userId);
            return followState.isPresent();
        } else {
            return false;
        }
    }

    private Boolean profileState(String nowUserEmail, Long userId) {
        Optional<User> nowUser = userRepository.findByEmail(nowUserEmail);
        Optional<User> checkUser = userRepository.findById(userId);
        if (nowUser.isPresent()) {
            return nowUser.equals(checkUser);
        } else return false;
    }

    private List<SearchUserReviewDto> getSearchReviewResponse(User u) {
        List<SearchUserReviewDto> searchUserReviewDtos = new ArrayList<>();
        List<Review> reviews = reviewRepository.findNewestReviewByUserId(u.getId(), Pageable.ofSize(2));
        for(Review r : reviews){
            SearchUserReviewDto buildDto = SearchUserReviewDto.builder()
                .reviewId(r.getReviewId())
                .movieImg(r.getMovie().getMovieImg())
                .reviewTitle(r.getReviewTitle())
                .build();

            searchUserReviewDtos.add(buildDto);
        }
        return searchUserReviewDtos;
    }

    @Transactional
    public ResponseEntity<UserSearchTotalResponseDto> getTotalResponse(String userName, Optional<String> accessToken) {
        if (accessToken.isPresent()) {
            Optional<String> extractedEmail = jwtService.extractEmail(accessToken.get());
            try{
                extractedEmail.ifPresent(s -> nowUserEmail = s);
            }catch (UnauthorizedException e){

            }


        }
        List<UserSearchResponseDto> response = new ArrayList<>();
        response = getResponse(userName);
        int size = response.size();
        UserSearchTotalResponseDto userSearchTotalResponseDto = UserSearchTotalResponseDto.builder()
                .userSearchResponseDtoList(response)
                .totalSize(size)
                .build();
        try{
            return ResponseEntity.status(200).body(userSearchTotalResponseDto);
        }catch(Exception e){
            return ResponseEntity.status(400).body(userSearchTotalResponseDto);
        }
    }
}
