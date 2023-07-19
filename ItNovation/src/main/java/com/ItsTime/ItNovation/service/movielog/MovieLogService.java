package com.ItsTime.ItNovation.service.movielog;

import com.ItsTime.ItNovation.common.GeneralErrorCode;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movieLike.MovieLikeRepository;
import com.ItsTime.ItNovation.domain.movielog.dto.*;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.follow.FollowService;
import com.ItsTime.ItNovation.service.movieLike.MovieLikeService;
import com.ItsTime.ItNovation.service.review.ReviewService;
import com.ItsTime.ItNovation.service.reviewlike.ReviewLikeService;
import com.ItsTime.ItNovation.service.star.StarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final JwtService jwtService;

    @Transactional
    public ResponseEntity getMovieLogResponse(String email) {

        try {
            User findUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(GeneralErrorCode.UNKNOWN_USER.getMessage()));
            MovieLogUserInfoDto movieLogUserInfoDto=getMovieLogUser(findUser);
            List<MovieLogfollowersInfoDto> movieLogfollowersInfoDtoList=getMovieLogFollowers(findUser);
            List<MovieLogfollowingInfoDto> movieLogfollowingInfoDtoList = getMovieLogFollowings(findUser);
            List<MovieLogReviewInfoDto> movieLogReviewInfoDtoList=getMovieLogReviews(findUser);
            List<MovieLogInterestedMovieInfoDto> movieLogInterestedMovieInfoDtoList = getMovieLogInterestedMovies(findUser);

            MovieLogResponseDto movieLogResponseDto=MovieLogResponseDto.builder()
                    .movieLogUserInfoDto(movieLogUserInfoDto)
                    .movieLogfollowersInfoDtoList(movieLogfollowersInfoDtoList)
                    .movieLogfollowingInfoDtoList(movieLogfollowingInfoDtoList)
                    .movieLogReviewInfoDtoList(movieLogReviewInfoDtoList)
                    .movieLogInterestedMovieInfoDtoList(movieLogInterestedMovieInfoDtoList)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(movieLogResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    /**
     *
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
     *
     * @param findUser
     * @return 유저의 팔로잉 정보
     */
    private List<MovieLogfollowingInfoDto> getMovieLogFollowings(User findUser) {
        return getMovieLogfollowingInfoDtoList(findUser.getId());
    }
    private List<MovieLogfollowingInfoDto> getMovieLogfollowingInfoDtoList(Long id) {
        List<User> userList = followService.getFollowingsByUserId(id);
        int size = userList.size();

        log.info("팔로잉 수 "+String.valueOf(size));
        List<MovieLogfollowingInfoDto> movieLogfollowingInfoDtoList = new ArrayList<>();

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
     *
     * @param findUser
     * @return 유저의 팔로워 정보
     */
    private List<MovieLogfollowersInfoDto> getMovieLogFollowers(User findUser) {
        return getMovieLogfollowersInfoDtoList(findUser.getId());
    }

    private List<MovieLogfollowersInfoDto> getMovieLogfollowersInfoDtoList(Long id) {
        List<User> userList = followService.getFollowersByUserId(id);
        int size = userList.size();
        log.info("팔로워 수 "+String.valueOf(size));

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
     *
     * @param findUser
     * @return 유저의 리뷰 최신순으로 가져옴
     */

    private List<MovieLogReviewInfoDto> getMovieLogReviews(User findUser) {
        List<Review> movieLogReviewList=getReviewList(findUser.getId());
        int size = movieLogReviewList.size();
        log.info("MovieLogService reviewsize " + size);
        List<MovieLogReviewInfoDto> movieLogReviewInfoDtoList = new ArrayList<>();
        MovieLogReviewInfoDto movieLogReviewInfoDto = null;
        for (int i = 0; i <size ; i++) {
            movieLogReviewInfoDto=makeMovieLogReviewInfoDto(movieLogReviewList, i);
            movieLogReviewInfoDtoList.add(movieLogReviewInfoDto);

        }
        return movieLogReviewInfoDtoList;
    }
    private List<Review> getReviewList(Long id) {
        List<Review> reviewList = reviewService.getReviewByUserId(id);
        if (reviewList == null) {
            //TODO: 프론트 에러 응답
            throw new IllegalArgumentException(GeneralErrorCode.NO_REVIEW.getMessage());
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
                .comments(0)
                .movieLogMovieofReviewsInfoDtoList(movieLogMovieofReviewsInfoDto)
                .build();
    }


    /**
     *
     * @param findUser
     * @return 유저의 관심영화 가져오기
     */
    private List<MovieLogInterestedMovieInfoDto> getMovieLogInterestedMovies(User findUser) {
        List<Movie> movieList = movieLikeService.UserInterestedMovieList(findUser.getId());
        log.info("getMovieLogInterestedMovies " + movieList.size());
        List<MovieLogInterestedMovieInfoDto> movieLogInterestedMovieInfoDtoList = new ArrayList<>();
        MovieLogInterestedMovieInfoDto movieLogInterestedMovieInfoDto=null;
        for (int i = 0; i < movieList.size(); i++) {
            movieLogInterestedMovieInfoDto = getMovieLogInterestedMovieInfoDto(findUser, movieList, i);
            movieLogInterestedMovieInfoDtoList.add(movieLogInterestedMovieInfoDto);
        }
        return movieLogInterestedMovieInfoDtoList;
    }

    private MovieLogInterestedMovieInfoDto getMovieLogInterestedMovieInfoDto(User findUser, List<Movie> movieList, int i) {
        MovieLogInterestedMovieInfoDto movieLogInterestedMovieInfoDto;
        long movieId = movieList.get(i).getId();
        long userId = findUser.getId();
        float avgMovieScore = starService.getMovieAvgScore(movieId);
        int count = movieLikeService.MovieLikeCountByUserIdAndMovieId(userId, movieId);
        Boolean isReviewed = false;
        if (count != 0) {
            isReviewed = true;
        }
        movieLogInterestedMovieInfoDto = MovieLogInterestedMovieInfoDto.builder()
                .movieId(movieId)
                .movieImg(movieList.get(i).getMovieImg())
                .star(avgMovieScore)
                .title(movieList.get(i).getTitle())
                .hasReviewed(isReviewed)
                .build();
        return movieLogInterestedMovieInfoDto;
    }





}
