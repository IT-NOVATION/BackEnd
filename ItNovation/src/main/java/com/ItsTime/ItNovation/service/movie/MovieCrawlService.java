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
        crawlMovieInfo(restTemplate, titleAndMovie);
        System.out.println(titleAndMovie.toString());
        return titleAndMovie;
    }

    private void crawlMovieInfo(RestTemplate restTemplate, Map<String, Movie> titleAndMovie) {
        for (int i = 1; i < 10; i++) {
            String url = "https://api.themoviedb.org/3/discover/movie" + "?api_key=" + API_KEY
                + "&page=" + i + "&language=ko-KR";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//여기에서도 끌고 올 수 있음. backdropPath 끌고 올 수 있음.
            String json = responseEntity.getBody();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            nowPagesMovieCrawl(titleAndMovie, results);
        }
    }

    private void nowPagesMovieCrawl(Map<String, Movie> titleAndMovie, JSONArray results) {
        final String posterBasicPath= "https://www.themoviedb.org/t/p/original/";
        for (int j=0; j< results.length(); j++ ) {
            JSONObject movieObject = results.getJSONObject(j);
            Map<String, String> movie_Info = new HashMap<>();
            String title = movie_Info.put("title", movieObject.getString("original_title"));
            movieDiscoverCrawl(posterBasicPath, movieObject, movie_Info);
            Integer movieId = movieObject.getInt("id");
            // 이제 긁어올것 장르, 배우, 감독, 나라, 러닝타임,
            long real_movieId = movieId.longValue();
            movieCreditCrawl(movieId, movie_Info);
            movieDetails(movieId, movie_Info);
            movie_Info.put("country", movieObject.getString("original_language"));
            Movie movie = setMovie(real_movieId, movie_Info);
            log.info("put this movie", title);
            titleAndMovie.put(title, movie);
        }
    }

    private static void movieDiscoverCrawl(String posterBasicPath, JSONObject movieObject,
        Map<String, String> movie_Info) {
        movie_Info.put("movieImg", posterBasicPath + movieObject.get("poster_path").toString());
        movie_Info.put("movieBgImg", posterBasicPath + movieObject.get("backdrop_path").toString());
        movie_Info.put("originalLanguage", movieObject.get("original_language").toString());
        movie_Info.put("movieDetail", movieObject.get("overview").toString());
        Object releaseDate = movieObject.get("release_date");
        System.out.println(releaseDate);
        movie_Info.put("movieDate", movieObject.get("release_date").toString());
    }

    private static Movie setMovie(long real_movieId, Map<String, String> movieInfo) {
        Movie movie = Movie.builder().
            title(movieInfo.get("title")).
            movieImg(movieInfo.get("movieImg")).
            movieBgImg(movieInfo.get("movieBgImg")).
            movieActor(movieInfo.get("movieActor")).
            movieCountry(movieInfo.get("country")).
            movieDate(movieInfo.get("movieDate")).
            movieGenre(movieInfo.get("genre")).
            movieDirector(movieInfo.get("movieDirector")).
            movieRunningTime(Integer.parseInt(movieInfo.get("movieRunningTime"))).
            movieDetail(movieInfo.get("movieDetail")).
            real_movieId(real_movieId).build();
        return movie;
    }

    private void movieCreditCrawl(Integer movieId, Map<String, String> movieInfo) {
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
                movieInfo.put("movieDirector", member.getString("name"));
            }
            if (member.getString("known_for_department").equals("Acting")) {
                movieInfo.put("movieActor", member.getString("name"));
            }

        }
    }

    private void movieDetails(Integer movieId, Map<String, String> movieInfo) {
        String movieUrl = String.format(
            "https://api.themoviedb.org/3/movie/%d?api_key=%s&language=ko-KR", movieId,
            API_KEY);
        log.info(movieUrl);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> creditEntity = restTemplate.getForEntity(movieUrl, String.class);
        String json2 = creditEntity.getBody();
        JSONObject jsonObject = new JSONObject(json2); //  Json으로 값들 바꾸고 영화 엔티티에 Actor, Pd 정보들 추가
        Integer runtime = jsonObject.getInt("runtime");
        JSONArray genres = jsonObject.getJSONArray("genres");
        JSONObject member = genres.getJSONObject(0);
        String genre = member.getString("name");
        System.out.println(movieInfo.get("title") + "= "+ runtime.toString());
        movieInfo.put("movieRunningTime", runtime.toString());
        movieInfo.put("genre", genre);
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

