package com.ItsTime.ItNovation.service.movielog;

import com.ItsTime.ItNovation.common.exception.ErrorCode;
import com.ItsTime.ItNovation.common.exception.NotFoundException;
import com.ItsTime.ItNovation.common.exception.UnauthorizedException;
import com.ItsTime.ItNovation.domain.comment.CommentRepository;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movielog.dto.*;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.review.ReviewService;
import com.ItsTime.ItNovation.service.star.StarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieLogService {
    private final FollowService followService;

    private final UserRepository userRepository;
    private final ReviewService reviewService;
    private final ReviewLikeService reviewLikeService;
    private final StarService starService;
    private final MovieLikeService movieLikeService;
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    private String nowUserEmail=null;


    @Transactional
    public ResponseEntity getMovieLogResponse(String email, Optional<String> accessToken) {

        try {
            User findUser = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

            MovieLogUserInfoDto movieLogUserInfoDto = getMovieLogUser(findUser);
            List<MovieLogfollowersInfoDto> movieLogfollowersInfoDtoList = getMovieLogFollowers(findUser);
            List<MovieLogfollowingInfoDto> movieLogfollowingInfoDtoList = getMovieLogFollowings(findUser);
            List<MovieLogReviewInfoDto> movieLogReviewInfoDtoList = getMovieLogReviews(findUser);
            List<MovieLogInterestedMovieInfoDto> movieLogInterestedMovieInfoDtoList = new ArrayList<>();
            Boolean isLoginedUserFollowsNowUser = false;

            if (accessToken.isPresent()) {
                Optional<String> extractedEmail = jwtService.extractEmail(accessToken.get());

                try {
                    extractedEmail.ifPresent(s -> nowUserEmail = s);
                } catch (UnauthorizedException e) {
                    throw new UnauthorizedException(e.getErrorCode());
                }

                User nowLoginedUser = null;
                if (userRepository.findByEmail(nowUserEmail).isPresent()) {
                    nowLoginedUser = userRepository.findByEmail(nowUserEmail).get();
                }

                isLoginedUserFollowsNowUser = isLoginedUserFollowsNowUser(findUser, nowLoginedUser);
                movieLogInterestedMovieInfoDtoList = getMovieLogInterestedMovies(findUser, nowLoginedUser);
            } else {
                movieLogInterestedMovieInfoDtoList = getMovieLogInterestedMovies(findUser, null);
            }
            MovieLogResponseDto movieLogResponseDto = MovieLogResponseDto.builder()
                    .movieLogUserInfoDto(movieLogUserInfoDto)
                    .movieLogfollowersInfoDtoList(movieLogfollowersInfoDtoList)
                    .movieLogfollowingInfoDtoList(movieLogfollowingInfoDtoList)
                    .movieLogReviewInfoDtoList(movieLogReviewInfoDtoList)
                    .movieLogInterestedMovieInfoDtoList(movieLogInterestedMovieInfoDtoList)
                    .isLoginedUserFollowsNowUser(isLoginedUserFollowsNowUser)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(movieLogResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    /**
     * @param findUser
     * @param nowLoginedUser
     * @return 현재 로그인 된 유저가 무비로그 주인을 팔로우 하고 있는지의 여부
     * 자신의 무비로그일 경우, 팔로우 안할 경우 -> false
     * 팔로우 중일 경우 -> true
     */
    private Boolean isLoginedUserFollowsNowUser(User findUser, User nowLoginedUser) {
        // FIXME: 로그인 한 유저와 무비로그 주인이 같을 경우 현재 프론트에서 처리중
        if (nowLoginedUser != null) {
            List<User> MyfollowingUser = followService.getFollowingsByUserId(nowLoginedUser.getId());
            if (MyfollowingUser.isEmpty()) {
                return false;
            } else {
                for (User user : MyfollowingUser) {
                    if (MyfollowingUser.contains(findUser)) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    /**
     * @param findUser
     * @return user 정보
     */

    private static MovieLogUserInfoDto getMovieLogUser(User findUser) {
        log.info("getMovieLogUser");
        log.info(findUser.getEmail());
        return MovieLogUserInfoDto.builder()
                .userId(findUser.getId())
                .bgImg(findUser.getBgImg())
                .nickname(findUser.getNickname())
                .introduction(findUser.getIntroduction())
                .grade(findUser.getGrade())
                .profileImg(findUser.getProfileImg())
                .build();
    }

    /**
     * @param findUser
     * @return 유저의 팔로잉 정보
     */
    private List<MovieLogfollowingInfoDto> getMovieLogFollowings(User findUser) {
        return getMovieLogfollowingInfoDtoList(findUser.getId());
    }

    private List<MovieLogfollowingInfoDto> getMovieLogfollowingInfoDtoList(Long id) {
        List<User> userList = followService.getFollowingsByUserId(id);
        int size = userList.size();

        log.info("팔로잉 수 " + String.valueOf(size));
        List<MovieLogfollowingInfoDto> movieLogfollowingInfoDtoList = new ArrayList<>();

        //팔로잉 없으면 빈 배열 반환
        MovieLogfollowingInfoDto movieLogfollowersInfoDto = null;
        for (int i = 0; i < size; i++) {
            movieLogfollowersInfoDto = MovieLogfollowingInfoDto.builder()
                    .userId(userList.get(i).getId())
                    .nickname(userList.get(i).getNickname())
                    .profileImg(userList.get(i).getProfileImg())
                    .build();
            movieLogfollowingInfoDtoList.add(movieLogfollowersInfoDto);

        }

        return movieLogfollowingInfoDtoList;
    }

    /**
     * @param findUser
     * @return 유저의 팔로워 정보
     */
    private List<MovieLogfollowersInfoDto> getMovieLogFollowers(User findUser) {
        return getMovieLogfollowersInfoDtoList(findUser.getId());
    }

    private List<MovieLogfollowersInfoDto> getMovieLogfollowersInfoDtoList(Long id) {
        List<User> userList = followService.getFollowersByUserId(id);
        int size = userList.size();
        List<MovieLogfollowersInfoDto> movieLogfollowersInfoDtoList = new ArrayList<>();

        MovieLogfollowersInfoDto movieLogfollowersInfoDto = null;
        for (int i = 0; i < size; i++) {
            movieLogfollowersInfoDto = MovieLogfollowersInfoDto.builder()
                    .userId(userList.get(i).getId())
                    .nickname(userList.get(i).getNickname())
                    .profileImg(userList.get(i).getProfileImg())
                    .build();
            movieLogfollowersInfoDtoList.add(movieLogfollowersInfoDto);


        }
        return movieLogfollowersInfoDtoList;
    }

    /**
     * @param findUser
     * @return 유저의 리뷰 최신순으로 가져옴
     */

    private List<MovieLogReviewInfoDto> getMovieLogReviews(User findUser) {
        List<Review> movieLogReviewList = getReviewList(findUser.getId());
        int size = movieLogReviewList.size();
        log.info("MovieLogService reviewsize " + size);
        List<MovieLogReviewInfoDto> movieLogReviewInfoDtoList = new ArrayList<>();
        MovieLogReviewInfoDto movieLogReviewInfoDto = null;
        for (int i = 0; i < size; i++) {
            movieLogReviewInfoDto = makeMovieLogReviewInfoDto(movieLogReviewList, i);
            movieLogReviewInfoDtoList.add(movieLogReviewInfoDto);

        }
        return movieLogReviewInfoDtoList;
    }

    private List<Review> getReviewList(Long id) {
        List<Review> reviewList = reviewService.getReviewByUserId(id);

        if (reviewList.isEmpty()) {
            //TODO: 프론트 에러 응답
//            throw new IllegalArgumentException(GeneralErrorCode.NO_REVIEW.getMessage());
            return new ArrayList<>();
        }
        return reviewList;
    }

    private MovieLogReviewInfoDto makeMovieLogReviewInfoDto(List<Review> movieLogReviewList, int i) {
        long targetReviewNum = movieLogReviewList.get(i).getReviewId();
        log.info("makemovielogreviewinfodto");
        log.info(String.valueOf(targetReviewNum));

        int reviewLikecount = reviewLikeService.countReviewLikebyReview(targetReviewNum);
        log.info(String.valueOf(reviewLikecount));

        MovieLogMovieofReviewsInfoDto movieLogMovieofReviewsInfoDto = MovieLogMovieofReviewsInfoDto.builder()
                .movieId(movieLogReviewList.get(i).getMovie().getId())
                .movieImg(movieLogReviewList.get(i).getMovie().getMovieImg()).build();

        String createdDate = String.valueOf(movieLogReviewList.get(i).getUser().getCreatedDate()).split("T")[0];
        return MovieLogReviewInfoDto.builder()
                .reviewId(movieLogReviewList.get(i).getReviewId())
                .reviewTitle(movieLogReviewList.get(i).getReviewTitle())
                .star(movieLogReviewList.get(i).getStar())
                .reviewMainText(movieLogReviewList.get(i).getReviewMainText())
                //TODO: BaseTimeEntity 넘겨줄때 string 으로 할지 localdatetime 으로 할지 상의해보고 수정하기
                .createdDate(createdDate)
                .likeCount(reviewLikecount)
                .hasSpoiler(movieLogReviewList.get(i).getHasSpoiler())
                //TODO: 댓글 서비스 구현 후 수정하기
                .comments(commentRepository.findAllByReviewId(movieLogReviewList.get(i).getReviewId()).size())
                .movieLogMovieofReviewsInfoDtoList(movieLogMovieofReviewsInfoDto)
                .build();
    }


    /**
     * @param findUser
     * @return 유저의 관심영화 가져오기
     */
    private List<MovieLogInterestedMovieInfoDto> getMovieLogInterestedMovies(User findUser, User nowLoginedUser) {

        List<Movie> movieList = movieLikeService.UserInterestedMovieList(findUser.getId());

        log.info("getMovieLogInterestedMovies " + movieList.size());
        List<MovieLogInterestedMovieInfoDto> movieLogInterestedMovieInfoDtoList = new ArrayList<>();
        MovieLogInterestedMovieInfoDto movieLogInterestedMovieInfoDto = null;
        for (int i = 0; i < movieList.size(); i++) {
            movieLogInterestedMovieInfoDto = getMovieLogInterestedMovieInfoDto(findUser, movieList, i, nowLoginedUser);
            movieLogInterestedMovieInfoDtoList.add(movieLogInterestedMovieInfoDto);
        }
        return movieLogInterestedMovieInfoDtoList;
    }

    private MovieLogInterestedMovieInfoDto getMovieLogInterestedMovieInfoDto(User findUser, List<Movie> movieList, int i, User nowLoginedUser) {
        long movieId = movieList.get(i).getId();
        float avgMovieScore = starService.getMovieAvgScore(movieId);

        Map<Boolean, Long> returnValue = getHasReviewedOfInterestedMovieByLoginUser(nowLoginedUser, movieId);
        if (returnValue.containsKey(true)) {
            return MovieLogInterestedMovieInfoDto.builder()
                    .movieId(movieId)
                    .movieImg(movieList.get(i).getMovieImg())
                    .star(avgMovieScore)
                    .title(movieList.get(i).getTitle())
                    .hasReviewdByLoginedUser(true)
                    .reviewId(returnValue.get(true))
                    .build();
        } else {
            return MovieLogInterestedMovieInfoDto.builder()
                    .movieId(movieId)
                    .movieImg(movieList.get(i).getMovieImg())
                    .star(avgMovieScore)
                    .title(movieList.get(i).getTitle())
                    .hasReviewdByLoginedUser(false)
                    .reviewId(null)
                    .build();
        }

    }

    private Map<Boolean, Long> getHasReviewedOfInterestedMovieByLoginUser(User nowLoginedUser, long movieId) {
//        log.info(String.valueOf(nowLoginedUser.getId()));
        Map<Boolean, Long> returnValue = new HashMap<>();
        returnValue.put(false, null);
        if (nowLoginedUser == null) {
            return returnValue;
        } else {
            List<Review> reviewsOfLoginUser = reviewService.getReviewByUserId(nowLoginedUser.getId());
            if (reviewsOfLoginUser.isEmpty()) {
                return returnValue;
            } else {
                for (Review review : reviewsOfLoginUser) {
                    if (review.getMovie().getId().equals(movieId)) {
//                        hasReviewedOfInterestedMovieByLoginUser = true;
                        returnValue.put(true, review.getReviewId());
                        return returnValue;

                    }
                }
                return returnValue;
            }
        }


    }


}
