package com.ItsTime.ItNovation.service.singleMoviePage;


import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.MovieFeatureDto;
import com.ItsTime.ItNovation.domain.movie.dto.SingleMoviePageMovieInfoDto;
import com.ItsTime.ItNovation.domain.movieLike.MovieLikeRepository;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.SingleMoviePageReviewInfoDto;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLikeRepository;
import com.ItsTime.ItNovation.domain.singleMoviePage.SingleMoviePageResponseDto;
import com.ItsTime.ItNovation.domain.singleMoviePage.SingleMoviePageReviewAndUserDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.dto.SingleMoviePageUserInfoDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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



    @Transactional
    public ResponseEntity getReviewInformationAboutMovie(Long movieId) {

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

            SingleMoviePageResponseDto movieAndReviewAndUserInfoDto = SingleMoviePageResponseDto.builder()
                .movie(madeSingleMovieDto(findMovie))
                .reviewAndUserInfoList(singleMoviePageReviewAndUserDtos)
                .build();

            return ResponseEntity.status(200).body(movieAndReviewAndUserInfoDto);
        }catch (IllegalArgumentException e){
         return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(400).body("서버에서 오류가 발생했습니다.");
        }





    }

    private SingleMoviePageMovieInfoDto madeSingleMovieDto(Movie movie) {
        SingleMoviePageMovieInfoDto singleMoviePageMovieInfoDto = SingleMoviePageMovieInfoDto.builder()
            .movieImg(movie.getMovieImg())
            .movieActor(movie.getMovieActor())
            .movieDetail(movie.getMovieDetail())
            .movieGenre(movie.getMovieGenre())
            .avgStarScore(4.0f)
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
            .starScore(review.getStar())
            .reviewLikeCount(reviewLikeCount)
            .build();
        return singleMoviePageReviewInfoDto;
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
