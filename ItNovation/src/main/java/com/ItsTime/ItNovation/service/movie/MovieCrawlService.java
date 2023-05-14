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


    private final String BASIC_Movie_URL =
        "https://api.themoviedb.org/3/movie/";


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
        for (int i = 1; i < 10; i++) {
            String url = "https://api.themoviedb.org/3/discover/movie" + "?api_key=" + API_KEY
                + "&page=" + i + "&language=ko-KR";
            Map<String, List<Map<String, Object>>> res1 = restTemplate.getForObject(url, Map.class); //여기에서도 끌고 올 수 있음. backdropPath 끌고 올 수 있음.
            List<Map<String, Object>> results1 = res1.get("results");

            for (Map<String, Object> movieInfoMap : results1) {

                String title = (String)movieInfoMap.get("original_title");
                String posterPath = (String)movieInfoMap.get("poster_path");
                String backdropPath = (String)movieInfoMap.get("backdrop_path");
                Integer movieId = (Integer)movieInfoMap.get("id");
                String originalLanguage = (String)movieInfoMap.get("original_language");
                String overView = (String)movieInfoMap.get("overView");
                String releaseDate = (String)movieInfoMap.get("release_date");

                posterPath = "https://www.themoviedb.org/t/p/original/" + posterPath;
                backdropPath = "https://www.themoviedb.org/t/p/original/" + backdropPath;

                // 이제 긁어올것 장르, 배우, 감독, 나라, 러닝타임,

                movieDetails(movieId);
                System.out.println("========================");
                movieCredit(movieId);

                //[adult, backdrop_path, id, original_language, overview, popularity, release_date, title,  vote_average, vote_count]
            }
        }
        return titleAndMovie;
    }

    private void movieCredit(Integer movieId) {
        String movieUrl = String.format("https://api.themoviedb.org/3/movie/%d/credits?api_key=%s&language=ko-KR",
            movieId, API_KEY);
        log.info(movieUrl);
        RestTemplate restTemplate2 = new RestTemplate();
        Map<String, List<Map<String, Object>>> movieCredits= restTemplate2.getForObject(movieUrl, Map.class);
        List<Map<String, Object>> cast = movieCredits.get("cast");
        // 배우 테이블을 만들어야 할듯
        System.out.println("movieDetail = " +  cast);
    }

    private void movieDetails(Integer movieId) {
        String movieUrl = String.format("https://api.themoviedb.org/3/movie/%d?api_key=%s&language=ko-KR", movieId, API_KEY);
        log.info(movieUrl);
        RestTemplate restTemplate1 = new RestTemplate();
        Map<String, List<Map<String, Object>>> movieDetail= restTemplate1.getForObject(movieUrl, Map.class);
        System.out.println("movieDetail = " + movieDetail);
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





/* 이 코드 참고해서 위에 map같이 더럽게 한 거 수정해보기.
import org.springframework.http.ResponseEntity;
    import org.springframework.web.client.RestTemplate;
    import org.json.JSONObject;

public class TmdbDirectorFetcher {
    public String fetchDirector(int movieId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=<your-api-key>&language=en-US";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String json = responseEntity.getBody();
        JSONObject jsonObject = new JSONObject(json);
        JSONObject credits = jsonObject.getJSONObject("credits");
        JSONArray crew = credits.getJSONArray("crew");
        for (int i = 0; i < crew.length(); i++) {
            JSONObject member = crew.getJSONObject(i);
            if (member.getString("job").equals("Director")) {
                return member.getString("name");
            }
        }
        return null;
    }
}*/
