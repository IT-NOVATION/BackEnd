package com.ItsTime.ItNovation.service.top;


import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.MovieFeatureDto;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopKeywordService {


    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;

    public ResponseEntity findTop3Feature(Long movieId) {  // 여기 코드 너무 고정적임 유동적이질 않음. ㅠㅠ 유동성있도록 기획에게 물어보고 변경해야 할 부분
        Map<String, Integer> featureCountMap = new ConcurrentHashMap<>();

        try {
            Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 존재하지 않습니다."));

            MovieFeatureDto featureCount = getFeatureCount(featureCountMap, movie);
            return ResponseEntity.status(200).body(featureCount);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
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
        log.info(featureCountMap.toString());
        List<Entry<String, Integer>> entryList = new ArrayList<>(featureCountMap.entrySet());
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

}
