package com.ItsTime.ItNovation.service.push;


import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.follow.Follower;
import com.ItsTime.ItNovation.domain.follow.dto.FollowStateResponseDto;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movieLike.MovieLike;
import com.ItsTime.ItNovation.domain.movieLike.MovieLikeRepository;
import com.ItsTime.ItNovation.domain.movieLike.dto.MovieLikeStateResponseDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.PushReviewLikeResponseDto;
import com.ItsTime.ItNovation.domain.review.dto.PushReviewLikeResponseDto.PushReviewLikeResponseDtoBuilder;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLike;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLikeRepository;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final FollowRepository followRepository;
    private final MovieRepository movieRepository;
    private final MovieLikeRepository movieLikeRepository;

    @Transactional
    public ResponseEntity pushReviewLike(Long reviewId, Long pushUserId) {
        try {
            Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없어요!"));
            User user = userRepository.findById(pushUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어요!"));
            Optional<ReviewLike> reviewLike = reviewLikeRepository.findReviewLikeByReviewIdAndUserId(
                pushUserId, reviewId);
            return getPushReviewLikeResponseDtoResponseEntity(review, user, reviewLike);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }



    private ResponseEntity<PushReviewLikeResponseDto> getPushReviewLikeResponseDtoResponseEntity(
        Review review, User user, Optional<ReviewLike> reviewLike) {
        if(reviewLike.isEmpty()){
            ReviewLike build = buildReviewLike(review, user);
            reviewLikeRepository.save(build);
            PushReviewLikeResponseDto reviewLikeDto = buildPushReviewLikeResponseDto(
                build);
            return ResponseEntity.status(200).body(reviewLikeDto);
        }
        else{
            ReviewLike presentReviewLike = reviewLike.get();
            presentReviewLike.updateReviewLike();

            //reviewLikeRepository.save(presentReviewLike); //transactional 작성 꼭 해야함 안 하면 더티 체킹 안함.

            PushReviewLikeResponseDto reviewLikeDto = buildPushReviewLikeResponseDto(
                presentReviewLike);
            return ResponseEntity.status(200).body(reviewLikeDto);
        }
    }

    private static PushReviewLikeResponseDto buildPushReviewLikeResponseDto(ReviewLike build) {
        PushReviewLikeResponseDto reviewLikeDto = PushReviewLikeResponseDto.builder()
            .isReviewLike(build.getReviewLike())
            .build();
        return reviewLikeDto;
    }

    private static ReviewLike buildReviewLike(Review review, User user) {
        ReviewLike build = ReviewLike.builder()
            .user(user)
            .review(review)
            .reviewLike(true)
            .build();
        return build;
    }

    public ResponseEntity pushFollow(Long pushUserId, Long targetId) {
        try {
            User pushUser = userRepository.findById(pushUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어요!"));
            User targetUser = userRepository.findById(targetId).orElseThrow(() -> new IllegalArgumentException("해당 팔로우 유저가 없어요!"));
            Optional<Follower> findByPushUserAndFollowUser = followRepository.findByPushUserAndFollowUser(
                pushUser.getId(), targetUser.getId());
            isSelfFollow(pushUser, targetUser);
            return getFollowStateResponseDtoResponseEntity(pushUser, targetUser,
                findByPushUserAndFollowUser);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }




    }

    private ResponseEntity<FollowStateResponseDto> getFollowStateResponseDtoResponseEntity(
        User pushUser, User targetUser, Optional<Follower> findByPushUserAndFollowUser) {
        if(findByPushUserAndFollowUser.isEmpty()){
            Follower build = getFollower(pushUser, targetUser);
            followRepository.save(build);
            FollowStateResponseDto followStateResponseDto = FollowStateResponseDto.builder()
                .isFollow(true).build();
            return ResponseEntity.status(200).body(followStateResponseDto);
        }
        else{
            Follower find = findByPushUserAndFollowUser.get();
            followRepository.delete(find);
            FollowStateResponseDto followStateResponseDto = FollowStateResponseDto.builder()
                .isFollow(false).build();
            return ResponseEntity.status(200).body(followStateResponseDto);
        }
    }

    private static Follower getFollower(User pushUser, User targetUser) {
        Follower build = Follower.builder()
            .pushUser(pushUser)
            .follower(targetUser)
            .build();
        return build;
    }

    private static void isSelfFollow(User pushUser, User targetUser) {
        if(pushUser.getId().equals(targetUser.getId())){
            throw new IllegalArgumentException("자기가 자기 자신을 팔로우 할 순 없습니다!");
        }
    }

    @Transactional
    public ResponseEntity pushMovieLike(Long userId, Long movieId){
        try{
            User movieLikeUser = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어요!"));
            Movie movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 영화가 없어요"));
            Optional<MovieLike> findByUserAndMovie = movieLikeRepository.findByUserIdAndMovieId(
                    movieLikeUser.getId(), movie.getId());
            return getMovieLikeStateResponseDtoResponseEntity(movieLikeUser, movie,
                    findByUserAndMovie);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    private ResponseEntity getMovieLikeStateResponseDtoResponseEntity(User movieLikeUser, Movie movie, Optional<MovieLike> findByUserAndMovie) {
        if(findByUserAndMovie.isEmpty()){
            MovieLike build = getMovieLike(movieLikeUser, movie);
            movieLikeRepository.save(build);
            MovieLikeStateResponseDto movieLikeStateResponseDto = MovieLikeStateResponseDto.builder()
                    .isMovieLike(true).build();
            return ResponseEntity.status(200).body(movieLikeStateResponseDto);
        }else{
            MovieLike movieLike = findByUserAndMovie.get();
            movieLikeRepository.delete(movieLike);
            MovieLikeStateResponseDto movieLikeStateResponseDto = MovieLikeStateResponseDto.builder()
                    .isMovieLike(false).build();
            return ResponseEntity.status(200).body(movieLikeStateResponseDto);
        }

    }
    private static MovieLike getMovieLike(User userId, Movie movieId) {
        MovieLike build = MovieLike.builder()
                .user(userId)
                .movie(movieId)
                .build();
        return build;
    }
}
