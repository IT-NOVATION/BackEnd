package com.ItsTime.ItNovation.controller.movie;

import com.ItsTime.ItNovation.entity.movie.Movie;
import com.ItsTime.ItNovation.entity.movie.dto.MovieSaveRequestDto;
import com.ItsTime.ItNovation.service.movie.MovieCrawlService;
import com.ItsTime.ItNovation.service.movie.MovieRepoService;
import java.util.Map;
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


    @GetMapping("/movie")
    public String getMovieName(Model model) {
        Map<String, String> movieAndPoster = movieCrawlService.getMovieAndPoster(); // 이 부분 무조건 고쳐야 함. 동기적으로 데이터 가져와서 스케줄링 같은 작업으로 일정 주기에 끌어오는 방법 고안.

        saveMovie(movieAndPoster);

        System.out.println("hi");
        model.addAttribute("movieInfo", movieAndPoster);
        return "movie";
    }

    private void saveMovie(Map<String, String> movieAndPoster) {
        for (String s : movieAndPoster.keySet()) {
            Movie saveMovie = new MovieSaveRequestDto(s, movieAndPoster.get(s)).toEntity();
            movieRepoService.save(saveMovie);
        }
    }

}
