package com.ItsTime.ItNovation.controller.movie;

import com.ItsTime.ItNovation.service.movie.MovieService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MovieController {

    @Autowired // new 로 생성해서 스프링 컨테이너에 등록되어있는 @Service 것을 들고와야 한다. DI 활용해야 다른 어노테이션들이 작동한다.   -> 오류 기록하기 나중에 !
    private final MovieService movieService; // autowired 해서 생성하지 않고 new 로 생성하면 @Value 어노테이션이 작동이 되지 않는다.

    //getMovieAndPoster() 메서드를 호출할 때 TmdbApiClient 클래스의 인스턴스를 생성하지 않았거나, 생성한 인스턴스가 스프링 컨테이너에서 관리되지 않는 경우입니다.
    //https://kimcoder.tistory.com/415?category=967734 출처
    @GetMapping("/movie")
    public String getMovieName(Model model) {
        //TmdbApiClient tmdbApiClient1 = new TmdbApiClient(); // 이렇게 호출해서 사용하면 절대로 안에 있는 스프링 관련된 친구들이 작동하지를 않는다.

        Map<String, String> movieAndPoster = movieService.getMovieAndPoster();
        System.out.println("hi");
        model.addAttribute("movieInfo", movieAndPoster);
        return "movie";
    }

}
