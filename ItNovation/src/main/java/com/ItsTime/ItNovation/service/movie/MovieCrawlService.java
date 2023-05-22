package com.ItsTime.ItNovation.service.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        Map<String, Movie> titleAndMovie = getTitleAndMovie(restTemplate);

        return titleAndMovie;
    }

    public String findBgImg() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String bgImgUrl = getBgImgUrl(restTemplate);
        return bgImgUrl;
    }


    private Map<String, Movie> getTitleAndMovie(
        RestTemplate restTemplate) {  // 이 기능은 반드시 따로 빼서 스케줄러 돌려서 일정 주기마다 하기로 진행
        Map<String, Movie> titleAndMovie = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            String url = "https://api.themoviedb.org/3/discover/movie" + "?api_key=" + API_KEY
                + "&page=" + i + "&language=ko-KR";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//여기에서도 끌고 올 수 있음. backdropPath 끌고 올 수 있음.
            String json = responseEntity.getBody();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int j=0; j<results.length(); j++ ) {
                JSONObject movieObject = results.getJSONObject(i);
                String title =  movieObject.getString("original_title");
                String posterPath = movieObject.getString("poster_path");
                String backdropPath = movieObject.getString("backdrop_path");
                Integer movieId = movieObject.getInt("id");
                String originalLanguage = movieObject.getString("original_language");
                String overView = movieObject.getString("overview");
                System.out.println(overView);
                String releaseDate = movieObject.getString("release_date");
                posterPath = "https://www.themoviedb.org/t/p/original/" + posterPath;
                backdropPath = "https://www.themoviedb.org/t/p/original/" + backdropPath;
                // 이제 긁어올것 장르, 배우, 감독, 나라, 러닝타임,
                long real_movieId = movieId.longValue();
                movieDetails(movieId);
                System.out.println("========================");
                Map<String, String> actorAndDirector = movieCredit(movieId);
                String director = actorAndDirector.get("Director");
                String actor = actorAndDirector.get("Actor");
                System.out.println("movie name & director = " + title + director);
                System.out.println("movie name & actor = " +title + actor);
                Map<String, String> runtimeAndGenre = movieDetails(movieId);
                String genre = runtimeAndGenre.get("genre");
                String runtime = runtimeAndGenre.get("runtime");
                int runt = Integer.parseInt(runtime);
                String country = runtimeAndGenre.get("country");
                Movie movie = Movie.builder().
                    title(title).
                    movieImg(posterPath).
                    movieBgImg(backdropPath).
                    movieActor(actor).
                    movieCountry(originalLanguage).
                    movieDate(releaseDate).
                    movieGenre(genre).
                    movieDirector(director).
                    movieRunningTime(runt).
                    movieDetail(overView).
                    real_movieId(real_movieId).build();

                titleAndMovie.put(title, movie);
            }
        }
        System.out.println(titleAndMovie.toString());
        return titleAndMovie;
    }

    private Map<String, String> movieCredit(Integer movieId) {
        String movieUrl = String.format(
            "https://api.themoviedb.org/3/movie/%d/credits?api_key=%s&language=ko-KR",
            movieId, API_KEY);
        log.info(movieUrl);
        RestTemplate restTemplate2 = new RestTemplate();
        Map<String, String> actorAndDirector = new HashMap<>();
        ResponseEntity<String> creditEntity = restTemplate2.getForEntity(movieUrl, String.class);
        String json2 = creditEntity.getBody();
        JSONObject jsonObject = new JSONObject(json2); //  Json으로 값들 바꾸고 영화 엔티티에 Actor, Pd 정보들 추가

        //JSONObject credits = jsonObject.getJSONObject("credits");
        JSONArray crew = jsonObject.getJSONArray("crew");
        for (int i = 0; i < crew.length(); i++) {
            JSONObject member = crew.getJSONObject(i);
            if (member.getString("job").equals("Director")) {
                actorAndDirector.put("Director", member.getString("name"));
            }
            if (member.getString("known_for_department").equals("Acting")) {
                actorAndDirector.put("Actor", member.getString("name"));
            }

        }
        return actorAndDirector;
    }

    private Map<String, String> movieDetails(Integer movieId) {
        String movieUrl = String.format(
            "https://api.themoviedb.org/3/movie/%d?api_key=%s&language=ko-KR", movieId,
            API_KEY);
        log.info(movieUrl);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> creditEntity = restTemplate.getForEntity(movieUrl, String.class);
        String json2 = creditEntity.getBody();
        JSONObject jsonObject = new JSONObject(json2); //  Json으로 값들 바꾸고 영화 엔티티에 Actor, Pd 정보들 추가
        Map<String, String> runtimeAndGenre = new HashMap<>();
        Integer runtime = jsonObject.getInt("runtime");
        JSONArray genres = jsonObject.getJSONArray("genres");

        JSONObject member = genres.getJSONObject(0);
        String genre = member.getString("name");
        runtimeAndGenre.put("runtime", runtime.toString());
        runtimeAndGenre.put("genre", genre);
        return runtimeAndGenre;
    }

    private String getBgImgUrl(RestTemplate restTemplate) {
        String url = "https://api.themoviedb.org/3/movie/109445/images?api_key=" + API_KEY;
        Map<String, List<Map<String, String>>> forObject = restTemplate.getForObject(url,
            Map.class);
        String fileUrl = forObject.get("backdrops").get(0).get("file_path");
        System.out.println("map " + forObject.get("backdrops").get(3).get("file_path"));
        return "https://www.themoviedb.org/t/p/original/" + fileUrl;
    }

    private void total_Pages(RestTemplate restTemplate) {
        Map<String, List<Map<String, Object>>> res = restTemplate.getForObject(
            BASE_URL + API_KEY,
            Map.class);
        Object totalPages = res.get("total_pages");
        log.info("totalpages= " + totalPages);
    }

}

