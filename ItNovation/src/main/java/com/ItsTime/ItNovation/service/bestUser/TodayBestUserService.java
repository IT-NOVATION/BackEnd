package com.ItsTime.ItNovation.service.bestUser;

import com.ItsTime.ItNovation.domain.bestUser.TopUserResponseDto;
import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.follow.FollowState;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.dto.TopUserMovieDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.TopUserReviewDto;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLikeRepository;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodayBestUserService {


    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    private final LocalDate yesterday = LocalDate.now().minusDays(1);

    /**
     * Todo -> 전날 기준 집계해서 top User 뽑는 방식으로 고려
     * @return
     */
    @Transactional
    public ResponseEntity getBestUserInfo(String email) {
        Pageable pageable = PageRequest.of(0, 5);
        log.info("hello");
        List<User> top5UsersWithTodayDate = reviewLikeRepository.findTopUsersWithYesterdayDate(yesterday,
            pageable); // -> 이거 전날 기준으로 고쳐야 함!
        System.out.println("top5UsersWithTodayDate = " + top5UsersWithTodayDate);

        List<TopUserResponseDto> top5UserResponseDtos = new ArrayList<>();
        User loginUser = null;
        Optional<User> findUser = userRepository.findByEmail(email);

        if(findUser.isPresent()){
            loginUser=findUser.get();
        }

        for (int index = 0; index < top5UsersWithTodayDate.size(); index++) {
            List<TopUserReviewDto> topUserReviewDtos = new ArrayList<>();
            User user = top5UsersWithTodayDate.get(index);
            madeInternalResponseDto(topUserReviewDtos, top5UserResponseDtos, user, loginUser);
        }


        if(top5UserResponseDtos.isEmpty()){
            return ResponseEntity.status(403).body("topUser 가 존재하지 않습니다!");
        }


        return ResponseEntity.status(200).body(top5UserResponseDtos);
    }

    private void madeInternalResponseDto(List<TopUserReviewDto> topUserReviewDtos,
        List<TopUserResponseDto> top5UserResponseDtos, User user, User loginUser) {
        Long following = (long) user.getFollowStates().size();
        log.info(yesterday.toString());
        Long followers = followRepository.countByFollowedUserId(user.getId());
        List<Review> reviews = reviewLikeRepository.bestReviewsByUserId(yesterday, user.getId());
        addBestReview(topUserReviewDtos, reviews);
        Pageable remainPageable = PageRequest.of(0, 2);
        addNewestReview(topUserReviewDtos, user, remainPageable);
        TopUserResponseDto topUserResponseDto = buildTopUserResponseDto(
            topUserReviewDtos, user, following, followers, loginUser);
        top5UserResponseDtos.add(topUserResponseDto);
    }

    private void addNewestReview(List<TopUserReviewDto> topUserReviewDtos, User user,
        Pageable remainPageable) {
        List<Review> newestReviewByUserId = reviewRepository.findNewestReviewByUserId(
            user.getId(), remainPageable);
        addTopUserNewestReviewDto(topUserReviewDtos, newestReviewByUserId);
    }

    private void addBestReview(List<TopUserReviewDto> topUserReviewDtos, List<Review> reviews) {
        log.info(reviews.toString());
        TopUserReviewDto topReviewDto = getTopUserReviewDto(reviews.get(0));
        topUserReviewDtos.add(topReviewDto);
    }

    private void addTopUserNewestReviewDto(List<TopUserReviewDto> topUserReviewDtos,
        List<Review> newestReviewByUserId) {
        for (Review review : newestReviewByUserId) {
            TopUserReviewDto remainDto = getTopUserReviewDto(review);
            if(isPresentReview(remainDto, topUserReviewDtos)){
                continue;
            }
            topUserReviewDtos.add(remainDto);
        }
    }

    private Boolean isPresentReview(TopUserReviewDto review, List<TopUserReviewDto> topUserReviewDtos) {
        for (TopUserReviewDto topUserReviewDto : topUserReviewDtos) {
            if(topUserReviewDto.getReviewId() == review.getReviewId()){
                return true;
            }
        }
        return false;
    }

    private TopUserResponseDto buildTopUserResponseDto(
        List<TopUserReviewDto> topUserReviewDtos, User user, Long following, Long followers, User loginUser) {
        TopUserResponseDto topUserResponseDto = TopUserResponseDto.builder()
            .userId(user.getId())
            .profileImg(user.getProfileImg())
            .nickName(user.getNickname())
            .introduction(user.getIntroduction())
            .grade(user.getGrade())
            .followers(followers)
            .followings(following)
            .reviews(topUserReviewDtos)
            .isLoginedUserFollowsNowUser(getIsLoginUserPushFollow(loginUser, user))
            .build();
        return topUserResponseDto;
    }

    private Boolean getIsLoginUserPushFollow(User loginUser, User user) {
        if(loginUser == null){
            return false;
        }
        Optional<FollowState> byPushUserAndFollowUser = followRepository.findByPushUserAndFollowUser(
            loginUser.getId(), user.getId());
        if(byPushUserAndFollowUser.isEmpty()){
            return false;
        }
        return true;
    }

    private TopUserReviewDto getTopUserReviewDto(Review topReview) {
        int reviewLikeCount = reviewLikeRepository.countReviewLikeByReviewId(
            topReview.getReviewId());
        TopUserReviewDto topReviewDto = TopUserReviewDto.builder()
            .star(topReview.getStar())
            .reviewTitle(topReview.getReviewTitle())
            .createdDate(topReview.getCreatedDate().toLocalDate().toString())
            .reviewId(topReview.getReviewId())
            .reviewLikeCount(reviewLikeCount)
            .reviewMainText(topReview.getReviewMainText())
            .movie(getMovieDto(topReview.getMovie()))
            .hasSpoiler(topReview.getHasSpoiler())
            .build();
        return topReviewDto;
    }

    private TopUserMovieDto getMovieDto(Movie movie) {
        return TopUserMovieDto.builder()
            .movieId(movie.getId())
            .movieImg(movie.getMovieImg())
            .build();
    }
}