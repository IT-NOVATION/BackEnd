package com.ItsTime.ItNovation.controller.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.service.movie.MovieCrawlService;
import com.ItsTime.ItNovation.service.movie.MovieRepoService;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MovieController {

    @Autowired
    private final MovieCrawlService movieCrawlService;
    @Autowired
    private final MovieRepoService movieRepoService;


    @GetMapping("/movies") // 테스트 하실때는 SecurityConfig에 /movies url 추가후 진행하세요!
    public String getMovieName(Model model) {
        Map<String, Movie> titleAndMovie = movieCrawlService.getTitleAndMovie(); // 이 부분 무조건 고쳐야 함. 동기적으로 데이터 가져와서 스케줄링 같은 작업으로 일정 주기에 끌어오는 방법 고안.
        saveMovie(titleAndMovie);
        System.out.println("hi");
        model.addAttribute("movieInfo", titleAndMovie);
        return "movie"; // 여기까지 2분 32초
    }

    @GetMapping("/movies/winter") // 겨울왕국 배경이미지 테스트
    public String getBackgroundImg(Model model) {
        String bgImg = movieCrawlService.findBgImg();
        System.out.println("hi");
        model.addAttribute("bgImg", bgImg);
        return "movieBgImgTest";
    }


    private void saveMovie(Map<String, Movie> titleAndMovie){
        System.out.println("system");
        int index=0;

        for (Entry<String, Movie> stringMovieEntry : titleAndMovie.entrySet()) {
            System.out.println(stringMovieEntry.getKey());
            movieRepoService.save(stringMovieEntry.getValue());
        }

//        for (String s :
//            entries) {
//            Movie saveMovie = titleAndMovie.get(s);
//            index+=1;
//            System.out.println(saveMovie.getTitle());
//            movieRepoService.save(saveMovie);
//        }
        System.out.println(index);
    }

}
