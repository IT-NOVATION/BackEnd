package com.ItsTime.ItNovation.service.user;


import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.dto.SearchUserReviewDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.domain.user.dto.UserSearchResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSearchService {

    private final UserRepository userRepository;

    public List<UserSearchResponseDto> getResponse(String searchNickName) {
        List<User> findByNickName = userRepository.findByNickname(searchNickName);
        if(findByNickName.isEmpty()){
            log.error("유저가 없습니다.");
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
                .userImg(u.getProfileImg())
                .introduction(u.getIntroduction())
                .reviews(getSearchReviewResponse(u))
                .build();

            userSearchResponseDtos.add(userSearchResponseDto);
        }
        return userSearchResponseDtos;
    }

    private List<SearchUserReviewDto> getSearchReviewResponse(User u) {
        List<SearchUserReviewDto> searchUserReviewDtos = new ArrayList<>();
        List<Review> reviews = u.getReviews();
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
}
