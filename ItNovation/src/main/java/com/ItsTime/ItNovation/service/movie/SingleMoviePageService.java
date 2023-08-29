package com.ItsTime.ItNovation.service.movie;


import com.ItsTime.ItNovation.domain.actor.Actor;
import com.ItsTime.ItNovation.domain.actor.ActorRepository;
import com.ItsTime.ItNovation.domain.comment.CommentRepository;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.MovieFeatureDto;
import com.ItsTime.ItNovation.domain.movie.dto.SingleMoviePageMovieInfoDto;
import com.ItsTime.ItNovation.domain.movieLike.MovieLike;
import com.ItsTime.ItNovation.domain.movieLike.MovieLikeRepository;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.SingleMoviePageReviewInfoDto;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLikeRepository;
import com.ItsTime.ItNovation.domain.singleMoviePage.SingleMoviePageResponseDto;
import com.ItsTime.ItNovation.domain.singleMoviePage.SingleMoviePageReviewAndUserDto;
import com.ItsTime.ItNovation.domain.star.Star;
import com.ItsTime.ItNovation.domain.star.StarRepository;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.domain.user.dto.SingleMoviePageLoginUserInfoDto;
import com.ItsTime.ItNovation.domain.user.dto.SingleMoviePageUserInfoDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SingleMoviePageService {


    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final MovieLikeRepository movieLikeRepository;
    private final StarRepository starRepository;
    private final UserRepository userRepository;
    private final ActorRepository actorRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public ResponseEntity getReviewInformationAboutMovie(Long movieId, String email) {

        try {
            Movie findMovie = movieRepository.findByMovieId(movieId)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 존재하지 않습니다."));

            List<Review> reviewList =
                reviewRepository.findAllByMovie(findMovie);
            List<SingleMoviePageReviewAndUserDto> singleMoviePageReviewAndUserDtos = new LinkedList<>();


            for (Review review : reviewList) {
                SingleMoviePageReviewAndUserDto reviewAndUserInfoDto = SingleMoviePageReviewAndUserDto.builder()
                    .user(madeSignUserInfoDto(review))
                    .review(madeSingleReviewInfoDto(review))
                    .build();
                singleMoviePageReviewAndUserDtos.add(reviewAndUserInfoDto);
            }
            log.info("마지막 과정 직전 로직");
            SingleMoviePageResponseDto movieAndReviewAndUserInfoDto = SingleMoviePageResponseDto.builder()
                .movie(madeSingleMovieDto(findMovie))
                .reviewAndUserInfoList(singleMoviePageReviewAndUserDtos)
                .loginUserInfoDto(madeLoginUserInfoDto(email, findMovie))
                .build();

            return ResponseEntity.status(200).body(movieAndReviewAndUserInfoDto);
        }catch (IllegalArgumentException e){
         return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(400).body("서버에서 오류가 발생했습니다.");
        }
    }

    private SingleMoviePageLoginUserInfoDto madeLoginUserInfoDto(String email, Movie movie) {
        Boolean pushMovieLike = false;
        Float reviewStar = 0.0f;
        Float movieStar = 0.0f;
        log.info("madeLoginUserInfoDto 입장");
        if(email == null){
            log.info("email 값은" + email);
            return madeDto(reviewStar,
                movieStar, pushMovieLike);
        }
        log.info("email 값은" + email);
        User user = userRepository.findByEmail(email).get();
        log.info(user.getEmail());
        if(starRepository.findByUserAndMovie(user,movie).isPresent()){
            movieStar=starRepository.findByUserAndMovie(user,movie).get().getScore();
        }
        log.info("round1 clear");
        if(reviewRepository.findByUserAndMovie(user,movie).isPresent()){
            reviewStar = reviewRepository.findByUserAndMovie(user,movie).get().getStar();
        }
        log.info("round 2 clear");

        if(movieLikeRepository.findByUserIdAndMovieId(user.getId(), movie.getId()).isPresent()){
            pushMovieLike= true;
        }



        log.info("madeLoginUserInfoDto 끝");

        return madeDto(reviewStar,movieStar,pushMovieLike);
    }

    private SingleMoviePageLoginUserInfoDto madeDto(Float reviewStar, Float movieStar, Boolean pushMovieLike) {
        return SingleMoviePageLoginUserInfoDto.builder()
            .pushedMovieLike(pushMovieLike)
            .reviewStar(reviewStar)
            .movieStar(movieStar)
            .build();
    }

    private SingleMoviePageMovieInfoDto madeSingleMovieDto(Movie movie) {
        SingleMoviePageMovieInfoDto singleMoviePageMovieInfoDto = SingleMoviePageMovieInfoDto.builder()
            .movieImg(movie.getMovieImg())
            .movieBgImg(movie.getMovieBgImg())
            .movieDetail(movie.getMovieDetail())
            .movieGenre(movie.getMovieGenre())
            .movieActor(getActorList(movie.getTitle()))
            .avgStarScore(getAvgScoreByMovieId(movie))
            .title(movie.getTitle())
            .movieLikeCount(movieLikeRepository.countMovieLike(movie))
            .movieRunningTime(movie.getMovieRunningTime())
            .movieReleasedDate(movie.getMovieDate())
            .movieDirector(movie.getMovieDirector())
            .movieCountry(movie.getMovieCountry())
            .movieAge(movie.getMovieAudit())
            .top3HasFeature(findTop3Feature(movie))
            .build();
        return singleMoviePageMovieInfoDto;
    }

    private List<String> getActorList(String title) {
        List<Actor> allByMovieTitle = actorRepository.findAllByMovieTitle(title);
        List<String> actorNameList = new ArrayList<>();
        for (int i=0; i<allByMovieTitle.size(); i++) {
            if(i>6){
                break;
            }
            actorNameList.add(allByMovieTitle.get(i).getActorName());
        }
        return actorNameList;
    }

    private Float getAvgScoreByMovieId(Movie movie) {
        Float avgScoreByMovieId = starRepository.findAvgScoreByMovieId(movie.getId());
        if(avgScoreByMovieId == null){
            return 0.0f;
        }

        return starRepository.findAvgScoreByMovieId(movie.getId());
    }

    private MovieFeatureDto findTop3Feature(Movie movie) {  // 여기 코드 너무 고정적임 유동적이질 않음. ㅠㅠ 유동성있도록 기획에게 물어보고 변경해야 할 부분
        Map<String, Integer> featureCountMap = new HashMap<>();
        MovieFeatureDto featureCount = getFeatureCount(featureCountMap, movie);

        return featureCount;
    }

    private MovieFeatureDto getFeatureCount(Map<String, Integer> featureCountMap,
        Movie movie) {
        featureCountMap.put("hasGoodActing", reviewRepository.countHasGoodActing(movie));
        featureCountMap.put("hasGoodScenario", reviewRepository.countHasGoodScenario(movie));
        featureCountMap.put("hasGoodDirecting", reviewRepository.countHasGoodDirecting(movie));
        featureCountMap.put("hasGoodOst", reviewRepository.countHasGoodOST(movie));
        featureCountMap.put("hasGoodVisual", reviewRepository.countHasGoodVisual(movie));
        featureCountMap.put("hasGoodProduction", reviewRepository.countHasGoodProduction(movie));
        featureCountMap.put("hasGoodCharacterCharming", reviewRepository.countHasGoodCharacterCharming(movie));
        featureCountMap.put("hasGoodDiction", reviewRepository.countHasGoodDiction(movie));
        featureCountMap.put("hasGoodStory", reviewRepository.countHasGoodStory(movie));

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(featureCountMap.entrySet());
        Collections.sort(entryList, (entry1, entry2) -> {
            return entry2.getValue().compareTo(entry1.getValue()); // 내림차순 정렬
        });
        List<Map.Entry<String, Integer>> top3Entries = entryList.subList(0, Math.min(3, entryList.size()));
        List<String> topFeatureList = new ArrayList<>();
        // 상위 3개의 엔트리를 활용하거나 출력할 수 있습니다.
        for (Map.Entry<String, Integer> entry : top3Entries) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
            if(value==0){
                continue;
            }
            topFeatureList.add(key);
        }

        MovieFeatureDto top3Feature = MovieFeatureDto.builder()
            .topKeywordList(topFeatureList)
            .build();

        return top3Feature;
    }

    private SingleMoviePageReviewInfoDto madeSingleReviewInfoDto(Review review) {

        int reviewLikeCount = reviewLikeRepository.countReviewLikeByReviewId(review.getReviewId());
        SingleMoviePageReviewInfoDto singleMoviePageReviewInfoDto = SingleMoviePageReviewInfoDto.builder()
            .reviewId(review.getReviewId())
            .reviewTitle(review.getReviewTitle())
            .createdDate(review.getCreatedDate().toLocalDate())
            .reviewMainText(review.getReviewMainText())
            .hasSpoiler(review.getHasSpoiler())
            .commentCount(commentRepository.findAllByReviewId(review.getReviewId()).size())
            .starScore(getStar(review))
            .reviewLikeCount(reviewLikeCount)
            .build();
        return singleMoviePageReviewInfoDto;
    }

    private static Float getStar(Review review) {
        Float star = review.getStar();
        return review.getStar();
    }

    private SingleMoviePageUserInfoDto madeSignUserInfoDto(Review review) {
        User reviewUser = review.getUser();
        SingleMoviePageUserInfoDto singleMoviePageUserInfoDto = SingleMoviePageUserInfoDto.builder()
            .userId(reviewUser.getId())
            .nickName(reviewUser.getNickname())
            .userProfileImg(reviewUser.getProfileImg())
            .build();
        return singleMoviePageUserInfoDto;
    }


}
