package com.ItsTime.ItNovation.service.movie;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.MoviePopularDto;
import com.ItsTime.ItNovation.domain.movie.dto.MoviePopularRecommendResponseDto;
import com.ItsTime.ItNovation.domain.movie.dto.MovieRecommendDto;
import com.ItsTime.ItNovation.domain.popularMovie.PopularMovie;
import com.ItsTime.ItNovation.domain.popularMovie.PopularMovieRepository;
import com.ItsTime.ItNovation.domain.star.StarRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieCrawlService {

    private final PopularMovieRepository popularMovieRepository;
    private final StarRepository starRepository;
    private final MovieRepository movieRepository;
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
        Map<String, Movie> titleAndMovie = getTitleAndMovies(restTemplate);

        return titleAndMovie;
    }


    private Map<String, Movie> getTitleAndMovies(
        RestTemplate restTemplate) {  // 이 기능은 반드시 따로 빼서 스케줄러 돌려서 일정 주기마다 하기로 진행
        Map<String, Movie> titleAndMovie = new HashMap<>();
        crawlMovieInfo(restTemplate, titleAndMovie);
        return titleAndMovie;
    }


    /**
     *
     * JsonObject 겹치는 부분 발생해서 JsonNode로 변경해야 될듯
     */

    private void crawlMovieInfo(RestTemplate restTemplate, Map<String, Movie> titleAndMovie) {
        for (int i = 1; i < 10; i++) {
            String url = "https://api.themoviedb.org/3/discover/movie" + "?api_key=" + API_KEY // 현재 한국에서 상영중인 영화로 변경
                + "&page=" + i + "&language=ko-KR" + "&region=KR" + "&sort_by=popularity.desc&include_adult=true&include_video=false"; // api 버전 변경에 다른
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//여기에서도 끌고 올 수 있음. backdropPath 끌고 올 수 있음.
            String json = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(json);
                JsonNode results = jsonNode.get("results");
                nowPagesMovieCrawl(titleAndMovie, results);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void nowPagesMovieCrawl(Map<String, Movie> titleAndMovie, JsonNode results)
         {
        final String posterBasicPath= "https://www.themoviedb.org/t/p/original";
        for (JsonNode movieNode : results ) {
            Map<String, String> movie_Info = new HashMap<>();
            movie_Info.put("title", movieNode.get("title").asText());
            movieDiscoverCrawl(posterBasicPath, movieNode, movie_Info);
            Integer movieId = movieNode.get("id").asInt();
            // 이제 긁어올것 장르, 배우, 감독, 나라, 러닝타임,
            long real_movieId = movieId.longValue();
            try {
                movieCreditCrawl(movieId, movie_Info);
                movieDetails(movieId, movie_Info);
            }catch (Exception e){
                e.printStackTrace();
            }
            movie_Info.put("country", movieNode.get("original_language").asText());
            Movie movie = setMovie(real_movieId, movie_Info);
            titleAndMovie.put(movie.getTitle(), movie);

        }
    }

    private static void movieDiscoverCrawl(String posterBasicPath, JsonNode movieNode,
        Map<String, String> movie_Info) {
        movie_Info.put("movieImg", posterBasicPath + movieNode.get("poster_path").asText());
        movie_Info.put("movieBgImg", posterBasicPath + movieNode.get("backdrop_path").asText());
        movie_Info.put("originalLanguage", movieNode.get("original_language").asText());
        movie_Info.put("movieDetail", movieNode.get("overview").asText());
        movie_Info.put("movieDate", movieNode.get("release_date").asText());
        //System.out.println(releaseDate);
        //movie_Info.put("movieDate", movieObject.get("release_date").toString());
    }

    private static Movie setMovie(long real_movieId, Map<String, String> movieInfo) {
        log.info("==========\n In setMovie =============");
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

        System.out.println(movieInfo.get("title"));
        return movie;
    }

    private void movieCreditCrawl(Integer movieId, Map<String, String> movieInfo)
        throws JsonProcessingException {
        String movieUrl = String.format(
            "https://api.themoviedb.org/3/movie/%d/credits?api_key=%s&language=ko-KR",
            movieId, API_KEY);
        log.info(movieUrl);
        RestTemplate restTemplate2 = new RestTemplate();
        ResponseEntity<String> creditEntity = restTemplate2.getForEntity(movieUrl, String.class);
        String json2 = creditEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json2); //  Json으로 값들 바꾸고 영화 엔티티에 Actor, Pd 정보들 추가
        //JSONObject credits = jsonObject.getJSONObject("credits");
        JsonNode crew = jsonNode.get("crew");
        for (JsonNode member : crew) {
            if (member.get("job").asText().equals("Director")) {
                movieInfo.put("movieDirector", member.get("name").asText());
            }
            if (member.get("known_for_department").asText().equals("Acting")) {
                movieInfo.put("movieActor", member.get("name").asText());
            }

        }
    }

    private void movieDetails(Integer movieId, Map<String, String> movieInfo)
        throws JsonProcessingException {
        String movieUrl = String.format(
            "https://api.themoviedb.org/3/movie/%d?api_key=%s&language=ko-KR", movieId,
            API_KEY);
        log.info(movieUrl);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> creditEntity = restTemplate.getForEntity(movieUrl, String.class);
        String json2 = creditEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(json2); //  Json으로 값들 바꾸고 영화 엔티티에 Actor, Pd 정보들 추가
        Integer runtime = jsonNode.get("runtime").asInt();
        JsonNode genresArray = jsonNode.get("genres");
        JsonNode genre = genresArray.get(0);
        String real_genre = genre.get("name").asText();
        System.out.println(movieInfo.get("title") + "= "+ runtime.toString());
        movieInfo.put("movieRunningTime", runtime.toString());
        movieInfo.put("genre", real_genre);
    }

    public List<MoviePopularDto> getPopularMovies(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://api.themoviedb.org/3/movie/popular" + "?api_key=" + API_KEY+"&language=ko-KR", String.class);
        String json = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> movies = new ArrayList<>();

        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            JsonNode results = jsonNode.get("results");
            for (JsonNode movieNode : results) {
                Map<String, Object> movieInfo = new HashMap<>();
                movieInfo.put("id", movieNode.get("id").asLong());
                movieInfo.put("title", movieNode.get("title").asText());
                movieInfo.put("movieImg", movieNode.get("poster_path").asText());
                movieInfo.put("popularity", movieNode.get("popularity").asDouble());
                movies.add(movieInfo);
            }
        } catch (JsonProcessingException e) {//Json파싱 오류
            e.printStackTrace();//오류날 시에 Httpstatus 오류를 던져주기
        }

        // PopularMovie 테이블에 저장
        List<Map<String, Object>> selectedMovies = movies.subList(0, Math.min(10, movies.size()));
        List<MoviePopularDto> moviePopularDtos = new ArrayList<>();
        for (Map<String, Object> movieInfo : selectedMovies) {
            String title = (String) movieInfo.get("title");
            String movieImg = (String) movieInfo.get("movieImg");
            Double popularity = (Double) movieInfo.get("popularity");

            MoviePopularDto moviePopularDto = MoviePopularDto.builder()
                    .movieTitle(title)
                    .movieImg(movieImg)
                    .popularity(popularity.intValue())
                    .build();

            PopularMovie moviePopular = PopularMovie.builder()
                    .title(title)
                    .movieImg(movieImg)
                    .popularity(popularity)
                    .build();
            popularMovieRepository.save(moviePopular);
            moviePopularDtos.add(moviePopularDto);
        }

        return moviePopularDtos;
    }

    public List<MovieRecommendDto> getTopReviewedMovies() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Movie> movies = movieRepository.findTopReviewedMoviesWithLimit(pageable);

        return movies.stream()
                .map(this::mapMovieToResponseDto)
                .collect(Collectors.toList());
    }

    private MovieRecommendDto mapMovieToResponseDto(Movie movie) {
        Float averageStarScore = starRepository.findAvgScoreByMovieId(movie.getId());
        return MovieRecommendDto.builder()
                .movieId(movie.getId())
                .movieTitle(movie.getTitle())
                .movieImg(movie.getMovieImg())
                .starScore(averageStarScore != null ? averageStarScore : 0)
                .build();
    }




}

