package com.ItsTime.ItNovation.service.review;

import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.ReviewMovieInfoDto;
import com.ItsTime.ItNovation.domain.movie.dto.ReviewPostMovieInfoResponseDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;

import com.ItsTime.ItNovation.domain.review.dto.*;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLikeRepository;

import com.ItsTime.ItNovation.domain.star.Star;
import com.ItsTime.ItNovation.domain.star.StarRepository;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.domain.user.dto.ReviewLoginUserInfoDto;
import com.ItsTime.ItNovation.domain.user.dto.ReviewUserInfoDto;



import com.ItsTime.ItNovation.service.grade.GradeService;


import com.ItsTime.ItNovation.domain.user.dto.UserPushLikeDto;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReviewService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final StarRepository starRepository;
    private final GradeService gradeService;



    /**
     * @param userId
     * @return 유저의 리뷰 반환
     */
    @Transactional
    public List<Review> getReviewByUserId(Long userId) {
        return reviewRepository.findNewestReviewByUserIdWithNoPageable(userId);
    }

    /**
     *
     * @param reviewPostRequestDto
     * @param loginUserEmail
     * @return 리뷰 쓰기
     */
  
    @Transactional
    public ResponseEntity reviewWrite(ReviewPostRequestDto reviewPostRequestDto, String loginUserEmail) {
        try{
            User nowUser = userRepository.findByEmail(loginUserEmail).orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));
            Movie nowMovie = movieRepository.findById(reviewPostRequestDto.getMovieId()).orElseThrow(() -> new IllegalArgumentException("일치하는 영화가 없습니다."));

            Review writtenReview=saveReview(reviewPostRequestDto, nowUser, nowMovie);

            gradeService.validateGrade(nowUser);

            return ResponseEntity.status(201).body(writtenReview.getReviewId());
        }catch (IllegalArgumentException e) {
            //TODO: 에러 메시지  -> 관심영화 API에서 이 경우 에러 처리하면됨
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    private Review saveReview(ReviewPostRequestDto reviewPostRequestDto, User nowUser, Movie nowMovie) {


        Review review=Review.builder().star(saveStarAndGet(reviewPostRequestDto, nowMovie, nowUser))
                .movie(nowMovie)
                .user(nowUser)
                .reviewTitle(reviewPostRequestDto.getReviewTitle())
                .reviewMainText(reviewPostRequestDto.getReviewMainText())
                .hasGoodStory(reviewPostRequestDto.getHasGoodStory())
                .hasGoodProduction(reviewPostRequestDto.getHasGoodProduction())
                .hasGoodScenario(reviewPostRequestDto.getHasGoodScenario())
                .hasGoodDirecting(reviewPostRequestDto.getHasGoodDirecting())
                .hasGoodOst(reviewPostRequestDto.getHasGoodOst())
                .hasGoodVisual(reviewPostRequestDto.getHasGoodVisual())
                .hasGoodActing(reviewPostRequestDto.getHasGoodActing())
                .hasGoodCharterCharming(reviewPostRequestDto.getHasGoodCharterCharming())
                .hasGoodDiction(reviewPostRequestDto.getHasGoodDiction())
                .hasCheckDate(reviewPostRequestDto.getHasCheckDate())
                .hasSpoiler(reviewPostRequestDto.getHasSpoiler())
                .watchDate(reviewPostRequestDto.getWatchDate())
                .reviewLikes(null)
                .build();

        reviewRepository.save(review);
        return review;
    }

    private Float saveStarAndGet(ReviewPostRequestDto reviewPostRequestDto,Movie movie, User user) {
        Float starScore = reviewPostRequestDto.getStar();

        Optional<Star> existingStar = starRepository.findByUserAndMovie(user, movie);
        if (existingStar.isPresent()) {//존재하면, 스타 스코어만 업데이트
            Star star = existingStar.get();
            star.updateScore(starScore);
            starRepository.save(star);
        } else {//존재하지 않는다면, 새롭게 생성
            Star build = Star.builder()
                .user(user)
                .movie(movie)
                .score(starScore)
                .build();
            starRepository.save(build);
        }

        return starScore;
    }

    /**
     * @param reviewId
     * @param email
     * @return 리뷰 읽기
     */
    @Transactional
    public ResponseEntity reviewRead(Long reviewId, String email) {
        try {
            Optional<User> loginUser= Optional.empty();
            User loginedUser = validateLoginUser(email);
            Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없습니다."));
            Movie movie = review.getMovie();
            User user = review.getUser();

            ReviewReadResponseDto reviewReadResponseDto = madeResponseDto(review, movie, user, loginedUser);
            return ResponseEntity.status(200).body(reviewReadResponseDto);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private ReviewReadResponseDto madeResponseDto(Review review, Movie movie, User user, User loginUser) {
        ReviewInfoDto reviewInfoDto = madeReviewInfoDto(review);
        ReviewMovieInfoDto reviewMovieInfoDto = madeMovieInfoDto(movie);
        ReviewUserInfoDto reviewUserInfoDto = madeUserInfoDto(user,review);
        ReviewLoginUserInfoDto reviewLoginUserInfoDto = madeLoginUserInfoDto(loginUser, review.getUser(), review);

        return mergeInfoDto(reviewInfoDto, reviewMovieInfoDto, reviewUserInfoDto, reviewLoginUserInfoDto);
    }



    private ReviewReadResponseDto mergeInfoDto(ReviewInfoDto reviewInfoDto, ReviewMovieInfoDto reviewMovieInfoDto,
        ReviewUserInfoDto reviewUserInfoDto, ReviewLoginUserInfoDto reviewLoginUserInfoDto) {

        return ReviewReadResponseDto.builder()
            .review(reviewInfoDto)
            .movie(reviewMovieInfoDto)
            .user(reviewUserInfoDto)
            .loginUser(reviewLoginUserInfoDto)
            .build();

    }



    private ReviewLoginUserInfoDto madeLoginUserInfoDto(User loginUser, User reviewUser, Review review) {
        if(loginUser != null){
            log.info("user is Present");
            log.info("== loginUser id ===" + loginUser.getId().toString());
            boolean present = followRepository.findByPushUserAndFollowUser(
                loginUser.getId(), reviewUser.getId()).isPresent();
            return ReviewLoginUserInfoDto.builder()
                .pushedReviewLike(isUserLikeReview(review, loginUser))
                .pushedFollow(present)
                .build();
        }
        return ReviewLoginUserInfoDto.builder()   // 로그인이 아닌 경우
            .pushedReviewLike(false)
            .pushedFollow(false)
            .build();
    }

    private Boolean isUserLikeReview(Review review, User loginUser) {

        if(reviewLikeRepository.isUserLike(loginUser, review).isEmpty()){
            return false;
        };

        return reviewLikeRepository.isUserLike(loginUser, review).get();
    }

    private ReviewUserInfoDto madeUserInfoDto(User user, Review review) {
        return ReviewUserInfoDto.builder()
            .userId(user.getId())
            .bgImg(user.getBgImg())
            .nickname(user.getNickname())
            .grade(user.getGrade())
            .introduction(user.getIntroduction())
            .profileImg(user.getProfileImg())
            .followerNum(followRepository.countByFollowedUserId(user.getId()))
            .followingNum(followRepository.countByFollowingUserId(user.getId()))
            .hasReviewLike(isUserLikeReview(review, user))
            .build();
    }

    private ReviewMovieInfoDto madeMovieInfoDto(Movie movie) {

        return ReviewMovieInfoDto.builder()
            .movieId(movie.getId())
            .movieCountry(movie.getMovieCountry())
            .movieImg(movie.getMovieImg())
            .movieGenre(movie.getMovieGenre())
            .movieReleaseDate(movie.getMovieDate())
            .title(movie.getTitle())
            .build();
    }

    private ReviewInfoDto madeReviewInfoDto(Review review) {
        LocalDate createDate = review.getCreatedDate().toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedCreateDate = createDate.format(formatter);//localDate를 String 형식으로 주어진 날짜 패턴에 맞추어 변환
        return ReviewInfoDto.builder()
            .reviewId(review.getReviewId())
            .hasCheckDate(validateNull(review.getHasCheckDate()))
            .hasGoodActing(validateNull(review.getHasGoodActing()))
            .hasGoodDiction(validateNull(review.getHasGoodDiction()))
            .hasSpoiler(validateNull(review.getHasSpoiler()))
            .hasGoodDirecting(validateNull(review.getHasGoodDirecting()))
            .hasGoodOst(validateNull(review.getHasGoodOst()))
            .hasGoodScenario(validateNull(review.getHasGoodScenario()))
            .hasGoodProduction(validateNull(review.getHasGoodProduction()))
            .hasGoodVisual(validateNull(review.getHasGoodVisual()))
            .hasGoodStory(validateNull(review.getHasGoodStory()))
            .hasGoodCharterCharming(validateNull(review.getHasGoodCharterCharming()))
            .reviewTitle(review.getReviewTitle())
            .reviewMainText(review.getReviewMainText())
            .star(review.getStar())
            .reviewLikeNum(reviewLikeRepository.countReviewLikeByReviewId(review.getReviewId()))
            .watchDate(review.getWatchDate())
            .createdDate(formattedCreateDate)
            .build();
    }

    private Boolean validateNull(Boolean feature) {
        if(feature==null){
            return false;
        }
        return feature;
    }

    @Transactional
    public ResponseEntity getMovieInfo(Long movieId, Authentication authentication) {
        try {
            Movie findMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 존재하지 않습니다."));

            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

             if(reviewRepository.countByUserAndMovie(user, findMovie)>=1){
                 throw new IllegalArgumentException("이미 유저가 해당 영화의 리뷰를 작성했습니다.");
             }

            ReviewPostMovieInfoResponseDto responseDto = buildResponse(
                movieId, findMovie);
            return ResponseEntity.status(200).body(responseDto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    private ReviewPostMovieInfoResponseDto buildResponse(Long movieId,
        Movie findMovie) {
        return ReviewPostMovieInfoResponseDto.builder()
            .movieId(movieId)
            .movieImg(findMovie.getMovieImg())
            .title(findMovie.getTitle())
            .releaseDate(findMovie.getMovieDate())
            .build();
    }
    @Transactional
    public ResponseEntity reviewCount(Long movieId){
        try{
            log.info(String.valueOf(movieId));
            Long count = reviewRepository.countByMovieId(movieId);
            ReviewCountResponseDto responseDto = ReviewCountResponseDto.builder()
                    .reviewCount(count)
                    .build();
            return ResponseEntity.ok(responseDto);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @Transactional
    public ResponseEntity getLikeUsers(Long reviewId, String email) {
        try {
            return getListResponseEntity(reviewId, email);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private ResponseEntity<List<UserPushLikeDto>> getListResponseEntity(Long reviewId,
        String email) {
        User validateLoginUser = validateLoginUser(email);
        List<User> findByReviewId = reviewLikeRepository.findAllUserByReviwId(reviewId);
        List<UserPushLikeDto> likeUsers = new ArrayList<>();
        for (User user : findByReviewId) {
            boolean isLoginUserFollowed = false;
            boolean isMyProfile = false;
            if(validateLoginUser!=null){
                if(Objects.equals(validateLoginUser.getId(), user.getId())){
                    isMyProfile=true;
                }
                if(followRepository.findByPushUserAndFollowUser(validateLoginUser.getId(), user.getId()).isPresent()){
                    isLoginUserFollowed=true;
                }
            }
            UserPushLikeDto build = buildUserPushLikeDto(user,
                isLoginUserFollowed, isMyProfile);
            likeUsers.add(build);
        }
        return ResponseEntity.status(200).body(likeUsers);
    }

    private User validateLoginUser(String email) {
        if(email != null){
            Optional<User> byEmail = userRepository.findByEmail(email);
            return byEmail.get();
        }
        return null;
    }

    private static UserPushLikeDto buildUserPushLikeDto(User user, boolean isLoginUserFollowed,
        boolean isMyProfile) {
        UserPushLikeDto build = UserPushLikeDto.builder()
            .userId(user.getId())
            .isMyProfile(isMyProfile)
            .isLoginUserFollowed(isLoginUserFollowed)
            .profileImg(user.getProfileImg())
            .nickname(user.getNickname())
            .build();
        return build;
    }
}
