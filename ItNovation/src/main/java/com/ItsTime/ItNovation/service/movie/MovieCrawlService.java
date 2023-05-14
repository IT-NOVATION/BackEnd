package com.ItsTime.ItNovation.service.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MovieCrawlService {

    @Value("${ttkey}")
    public String API_KEY;


    private final String BASE_URL =
        "https://api.themoviedb.org/3/discover/movie?api_key=";


    public Map<String, Movie> getTitleAndMovie() {
        //여기 참고 https://developers.themoviedb.org/3/movies/get-movie-images

        log.info(BASE_URL + API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        total_Pages(restTemplate);
        Map<String, Movie> titleAndMovie = getTitleAndPoster(restTemplate);

        return titleAndMovie;
    }

    public String findBgImg(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String bgImgUrl = getBgImgUrl(restTemplate);
        return bgImgUrl;
    }




    private Map<String, Movie> getTitleAndPoster(RestTemplate restTemplate) {  // 이 기능은 반드시 따로 빼서 스케줄러 돌려서 일정 주기마다 하기로 진행
        Map<String, Movie> titleAndMovie = new HashMap<>();
        for (int i = 1; i < 400; i++) {
            String url = "https://api.themoviedb.org/3/discover/movie" + "?api_key=" + API_KEY
                + "&page=" + i;
            Map<String, List<Map<String, Object>>> res1 = restTemplate.getForObject(url, Map.class); //여기에서도 끌고 올 수 있음. backdropPath 끌고 올 수 있음.
            List<Map<String, Object>> results1 = res1.get("results");

            for (Map<String, Object> movieInfoMap : results1) {

                Object originalTitle = movieInfoMap.get("original_title");
                Object posterPath = movieInfoMap.get("poster_path");
                Object backdropPath = movieInfoMap.get("backdrop_path");

                posterPath = "https://www.themoviedb.org/t/p/original/" + posterPath;
                backdropPath = "https://www.themoviedb.org/t/p/original/" + backdropPath;

                Movie movie= Movie.builder().
                    adult((Boolean) movieInfoMap.get("adult")).
                    backdrop_path((String)backdropPath).
                    original_language((String)movieInfoMap.get("original_language")).
                    movieTitle((String)movieInfoMap.get("original_title")).
                    overview((String) movieInfoMap.get("overview")).
                    release_date((String) movieInfoMap.get("release_date")).
                    movie_unique_id((Integer) movieInfoMap.get("id")).
                    moviePosterUrl((String) posterPath).
                    build();
                //[adult, backdrop_path, id, original_language, overview, popularity, release_date, title,  vote_average, vote_count]


                log.info(movie.toString());
                titleAndMovie.put(movie.getMovieTitle(), movie);
            }
        }
        return titleAndMovie;
    }

    private String getBgImgUrl(RestTemplate restTemplate){
        String url = "https://api.themoviedb.org/3/movie/109445/images?api_key=" + API_KEY;
        Map<String, List<Map<String,String>>> forObject = restTemplate.getForObject(url, Map.class);
        String fileUrl = forObject.get("backdrops").get(0).get("file_path");
        System.out.println("map " + forObject.get("backdrops").get(3).get("file_path"));
        return "https://www.themoviedb.org/t/p/original/" + fileUrl;
    }



    private void total_Pages(RestTemplate restTemplate) {
        Map<String, List<Map<String, Object>>> res = restTemplate.getForObject(BASE_URL + API_KEY,
            Map.class);
        Object totalPages = res.get("total_pages");
        log.info("totalpages= " + totalPages);
    }

}
