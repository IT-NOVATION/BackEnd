package com.ItsTime.ItNovation.service.movie;

import com.ItsTime.ItNovation.domain.actor.Actor;
import com.ItsTime.ItNovation.domain.actor.ActorRepository;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.MoviePopularDto;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieCrawlService {

    private final PopularMovieRepository popularMovieRepository;
    private final MovieRepository movieRepository;
    private final StarRepository starRepository;
    private final ActorRepository actorRepository;


    @Value("${ttkey}")
    public String API_KEY;

    @Value("${kfkey}")
    public String KF_API_KEY;

    private final String BASE_URL =
        "https://api.themoviedb.org/3/discover/movie?api_key=";


    private final String BASIC_Movie_URL =
        "https://api.themoviedb.org/3/movie/";

    private final String KOFI_MOVIE_INFO_URL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=";


    private String KOFI_URL = "https://kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key=";

    private static int flag = 1;


    @Transactional
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
     * JsonObject 겹치는 부분 발생해서 JsonNode로 변경해야 될듯
     */

    private void crawlMovieInfo(RestTemplate restTemplate, Map<String, Movie> titleAndMovie) {
        for (int i = 1; i < 5; i++) {
            String url = "https://api.themoviedb.org/3/discover/movie" + "?api_key=" + API_KEY
                // 현재 한국에서 상영중인 영화로 변경
                + "&page=" + i + "&language=ko-KR" + "&region=KR"
                + "&sort_by=popularity.desc&include_adult=true&include_video=false"; // api 버전 변경에 다른
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//여기에서도 끌고 올 수 있음. backdropPath 끌고 올 수 있음.
            String json = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(json);
                JsonNode results = jsonNode.get("results");
                nowPagesMovieCrawl(titleAndMovie, results);
            } catch (Exception e) {
                continue;
            }
        }
    }

    private void crawlMovieAudit(Map<String, String> movieInfo, String title)
        throws JsonProcessingException {

        String KOFI_URL_GET_NAME = KOFI_URL + KF_API_KEY + "&movieNm=" + title;
        log.info("KOFI_URL_GET_NAME = " + KOFI_URL_GET_NAME);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(KOFI_URL_GET_NAME,
            String.class);
        String kofi_json = responseEntity.getBody();

        log.info("kofi_json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(kofi_json);
        log.info(jsonNode.toString());
        JsonNode movieListJson = jsonNode.get("movieListResult");

        JsonNode movieInfoJson = movieListJson.get("movieList");

        getMovieInfoKorea(movieInfo, movieInfoJson.get(0).get("movieCd").asText()); //찐 정보 얻어오기
        // movieInfo.put("audit", movieAudit);

    }

    private void getMovieInfoKorea(Map<String, String> movieInfo, String movieCd)
        throws JsonProcessingException {

        log.info("====In getMovieAudit====");
        String AUDIT_URL = KOFI_MOVIE_INFO_URL + KF_API_KEY + "&movieCd=" + movieCd;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> creditEntity = restTemplate.getForEntity(AUDIT_URL, String.class);
        String movieInfoEntity = creditEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode1 = objectMapper.readTree(movieInfoEntity);
        if (flag == 1) {
            log.info(jsonNode1.toString());
            flag = 0;
        }
        String audits;
        try {
            JsonNode movieInfoJson = jsonNode1.get("movieInfoResult").
                get("movieInfo");

            movieInfo.put("title", movieInfoJson.get("movieNm").asText());
            movieInfo.put("movieRunningTime", movieInfoJson.get("showTm").asText());
            movieInfo.put("country", movieInfoJson.get("nations").get(0).get("nationNm").asText());
            movieInfo.put("movieDirector",
                movieInfoJson.get("directors").get(0).get("peopleNm").asText());
            movieInfo.put("genre", movieInfoJson.get("genres").get(0).get("genreNm").asText());
            movieInfo.put("audit", movieInfoJson.get("audits").get(0).get("watchGradeNm").asText());
            movieInfo.put("originalLanguage",
                movieInfoJson.get("nations").get(0).get("nationNm").asText());

            log.info(movieInfo.toString());

            log.info("====== actor ======");
            JsonNode actors = movieInfoJson.get("actors");
            for (JsonNode actor : actors) {
                log.info("====== in for actor ======");
                log.info(actor.toString());
                log.info(movieInfo.get("title"));
                Actor build = Actor.builder()
                    .actorName(actor.get("peopleNm").asText())
                    .movieTitle(movieInfo.get("title"))
                    .build();
                actorSave(build);
            }
        } catch (Exception e) {
            audits = null;
        }

        // log.info(audits);
    }

    private void actorSave(Actor build) {
        if(actorRepository.findActorByActorName(build.getActorName()).isPresent()){
            return;
        }
        actorRepository.save(build);
    }


    /**
     * 여기부터 진짜 크롤링
     *
     * @param titleAndMovie
     * @param results
     */
    private void nowPagesMovieCrawl(Map<String, Movie> titleAndMovie, JsonNode results)
        throws JsonProcessingException {
        final String posterBasicPath = "https://www.themoviedb.org/t/p/original";
        for (JsonNode movieNode : results) {
            Map<String, String> movie_Info = new HashMap<>();
            movie_Info.put("title", movieNode.get("title").asText());
            try {
                crawlMovieAudit(movie_Info, movie_Info.get("title"));
                movieDiscoverCrawl(posterBasicPath, movieNode, movie_Info);
                Integer movieId = movieNode.get("id").asInt();
                // 이제 긁어올것 장르, 배우, 감독, 나라, 러닝타임,
                long real_movieId = movieId.longValue();
                Movie movie = setMovie(real_movieId, movie_Info);
                titleAndMovie.put(movie.getTitle(), movie);
            }catch (Exception e){
                log.info(e.getMessage());
            }

        }
    }

    private static void movieDiscoverCrawl(String posterBasicPath, JsonNode movieNode,
        Map<String, String> movie_Info) {
        movie_Info.put("movieImg", posterBasicPath + movieNode.get("poster_path").asText());
        movie_Info.put("movieBgImg", posterBasicPath + movieNode.get("backdrop_path").asText());
        movie_Info.put("movieDetail", movieNode.get("overview").asText());
        movie_Info.put("movieDate", movieNode.get("release_date").asText());
    }

    private Movie setMovie(long real_movieId, Map<String, String> movieInfo) {
        log.info("==========\n In setMovie =============");
        log.info(movieInfo.get("title"));
        log.info(movieInfo.get("movieActor"));
        log.info("===== now movieInfo ======");
        log.info(movieInfo.toString());
        Movie movie = getMovie(real_movieId, movieInfo);
        if(movie==null){
            throw new IllegalArgumentException("온전치 못한 영화입니다.");
        }
        return movie;
    }

    private Movie getMovie(long real_movieId, Map<String, String> movieInfo) {
        if(hasEmptyPropertyMovie(movieInfo)){
            return null;
        }
        Movie movie = Movie.builder().
            title(movieInfo.get("title")).
            movieImg(movieInfo.get("movieImg")).
            movieBgImg(movieInfo.get("movieBgImg")).
            movieCountry(movieInfo.get("country")).
            movieDate(movieInfo.get("movieDate")).
            movieGenre(movieInfo.get("genre")).
            movieDirector(movieInfo.get("movieDirector")).
            movieRunningTime(Integer.parseInt(movieInfo.get("movieRunningTime")
                )).
            movieDetail(movieInfo.get("movieDetail")).
            movieAudit(movieInfo.get("audit")).
            real_movieId(real_movieId).build();
        return movie;
    }

    private Boolean hasEmptyPropertyMovie(Map<String, String> movieInfo) {
        try {
            isEmpty(movieInfo.get("title"));
            isEmpty(movieInfo.get("movieImg"));
            isEmpty(movieInfo.get("movieBgImg"));
            isEmpty(movieInfo.get("country"));
            isEmpty(movieInfo.get("movieDate"));
            isEmpty(movieInfo.get("genre"));
            isEmpty(movieInfo.get("movieDirector"));
            isEmpty(movieInfo.get("movieRunningTime"));
            isEmpty(movieInfo.get("movieDetail"));
            isEmpty(movieInfo.get("audit"));
            return false;
        }catch (IllegalArgumentException e){
            log.info(e.getMessage());
            return true;
        }
    }

    private void isEmpty(String content) {
        if(content ==null){
            throw new IllegalArgumentException("빈 값이 존재합니다.");
        }
    }


    public List<MoviePopularDto> getPopularMovies() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        popularMovieRepository.deleteAll();

        List<Map<String, Object>> movies = new ArrayList<>();
        for(int i=1; i<=3; i++) {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY
                    + "&language=ko-KR&region=KR&page="+i,
                String.class);
            String json = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(json);
            JsonNode results = jsonNode.get("results");
            for (JsonNode movieNode : results) {
                try {
                    Map<String, Object> movieInfo = new HashMap<>();
                    log.info("1. movieNode========");
                    log.info(movieNode.toString());
                    buildMovieInfo(movieNode, movieInfo); // 한 영화에 대한 정보
                    movies.add(movieInfo);
                } catch (Exception e) {//Json파싱 오류
                    log.info(e.getMessage());//오류날 시에 Httpstatus 오류를 던져주기
                }
            }
        }

        //log.info(movies.toString());
        log.info("movies size=====" + String.valueOf(movies.size()));
        // PopularMovie 테이블에 저장
        log.info("selected -------===========");
        List<Map<String, Object>> selectedMovies = movies;
        log.info(selectedMovies.toString());
        List<MoviePopularDto> moviePopularDtos = new ArrayList<>();
        int count=0;
        for (Map<String, Object> movieInfo : selectedMovies) {
            if (movieRepository.findByRealMovieId((Long) movieInfo.get("id")).isEmpty()) {
                try {
                    savePopularMovie(movieInfo);
                } catch (Exception e) {
                    continue;
                }
            }
            if(count>=10){
                break;
            }
            count++;

            Movie movie = movieRepository.findByRealMovieId((Long) movieInfo.get("id")).get();
            String title = (String) movieInfo.get("title");
            String movieImg = (String) movieInfo.get("movieImg");
            Double popularity = (Double) movieInfo.get("popularity");

            MoviePopularDto moviePopularDto = MoviePopularDto.builder()
                .movieId(movie.getId())
                .movieTitle(title)
                .movieImg(movieImg)
                .popularity(popularity.intValue())
                .starScore(getAvgScoreByMovieId(movie.getId()))
                .build();

            PopularMovie moviePopular = PopularMovie.builder()
                .title(title)
                .movieImg(movieImg)
                .popularity(popularity)
                .movieRunningTime(movie.getMovieRunningTime())
                .real_movieId(movie.getReal_movieId())
                .movieAudit(movie.getMovieAudit())
                .movieDate(movie.getMovieDate())
                .movieGenre(movie.getMovieGenre())
                .movieCountry(movie.getMovieCountry())
                .movieBgImg(movie.getMovieBgImg())
                .movieDbId(movie.getId())
                .build();

            popularMovieRepository.save(moviePopular);
            moviePopularDtos.add(moviePopularDto);
        }

        return moviePopularDtos;
    }

    private Float getAvgScoreByMovieId(Long movieId) {
        Float avgScoreByMovie = starRepository.findAvgScoreByMovieId(movieId);
        if (avgScoreByMovie == null) {
            return 0.0f;
        }

        return starRepository.findAvgScoreByMovieId(movieId);
    }


    private void creditPopularMovie(Map<String, Object> movieInfo, String title)
        throws JsonProcessingException {

        String KOFI_URL_GET_NAME = KOFI_URL + KF_API_KEY + "&movieNm=" + title;
        log.info("3. KOFI_URL_GET_NAME = " + KOFI_URL_GET_NAME);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(KOFI_URL_GET_NAME,
            String.class);
        String kofi_json = responseEntity.getBody();

        log.info("4. kofi_json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(kofi_json);
        log.info(jsonNode.toString());
        JsonNode movieListJson = jsonNode.get("movieListResult");

        JsonNode movieInfoJson = movieListJson.get("movieList");
        log.info("movieInfoJson ====" + movieInfoJson.toString());

        String movieCd = movieInfoJson.get(0).get("movieCd").asText();
        if(movieCd==null){
            throw new IllegalArgumentException("넘겨가");
        }


        getPopularMovieInfoKorea(movieInfo,
            movieInfoJson.get(0).get("movieCd").asText()); //찐 정보 얻어오기

        // movieInfo.put("audit", movieAudit);

    }


    private void getPopularMovieInfoKorea(Map<String, Object> movieInfo, String movieCd)
        throws JsonProcessingException {
        log.info("5. ====In getMovieAudit====");
        String AUDIT_URL = KOFI_MOVIE_INFO_URL + KF_API_KEY + "&movieCd=" + movieCd;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> creditEntity = restTemplate.getForEntity(AUDIT_URL, String.class);
        String movieInfoEntity = creditEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode1 = objectMapper.readTree(movieInfoEntity);
        if (flag == 1) {
            log.info(jsonNode1.toString());
            flag = 0;
        }
        String audits;
        log.info("6. json=========");
        log.info(jsonNode1.toString());
        try {
            JsonNode movieInfoJson = jsonNode1.get("movieInfoResult").
                get("movieInfo");

            log.info("7. movieInfoJson=========");
            log.info(movieInfoJson.toString());
            movieInfo.put("title", movieInfoJson.get("movieNm").asText());
            movieInfo.put("movieRunningTime", movieInfoJson.get("showTm").asText());
            movieInfo.put("country", movieInfoJson.get("nations").get(0).get("nationNm").asText());
            movieInfo.put("movieDirector",
                movieInfoJson.get("directors").get(0).get("peopleNm").asText());
            movieInfo.put("genre", movieInfoJson.get("genres").get(0).get("genreNm").asText());
            movieInfo.put("audit", movieInfoJson.get("audits").get(0).get("watchGradeNm").asText());
            movieInfo.put("originalLanguage",
                movieInfoJson.get("nations").get(0).get("nationNm").asText());

            log.info("8. movieInfo============");
            log.info(movieInfo.toString());

            log.info("====== actor ======");
            JsonNode actors = movieInfoJson.get("actors");
            for (JsonNode actor : actors) {
                log.info("====== in for actor ======");
                log.info(actor.toString());
                log.info(movieInfo.get("title").toString());
                Actor build = Actor.builder()
                    .actorName(actor.get("peopleNm").asText())
                    .movieTitle(movieInfo.get("title").toString())
                    .build();
                actorSave(build);
            }
        } catch (Exception e) {
            audits = null;
        }

        // log.info(audits);
    }

    private void buildMovieInfo(JsonNode movieNode, Map<String, Object> movieInfo)
        throws JsonProcessingException {

        movieInfo.put("id", movieNode.get("id").asLong());
        movieInfo.put("title", movieNode.get("title").asText());

        String title = movieNode.get("title").asText();
        log.info("2. title ======== ");
        log.info(title);
        creditPopularMovie(movieInfo, title);

        //getPopularMovieInfoKorea(movieInfo,title);

        movieInfo.put("movieImg",
            "https://www.themoviedb.org/t/p/original" + movieNode.get("poster_path").asText());
        movieInfo.put("popularity", movieNode.get("popularity").asDouble());
        movieInfo.put("movieBgImg",
            "https://www.themoviedb.org/t/p/original" + movieNode.get("backdrop_path").asText());
        movieInfo.put("movieDetail", movieNode.get("overview").asText());
        movieInfo.put("movieDate", movieNode.get("release_date").asText());

        log.info(movieInfo.toString());
    }

    private void savePopularMovie(Map<String, Object> movieInfo) throws JsonProcessingException {
        Map<String, String> convertMovieInfo = new HashMap<>();
        convertMovieInfo(movieInfo, convertMovieInfo);

        Integer real_movieId = Integer.parseInt(convertMovieInfo.get("id"));


        convertMovieInfo.put("country", movieInfo.get("originalLanguage").toString());
        Movie movie = setMovie(real_movieId, convertMovieInfo);
        if(movie==null){
            log.info("영화 안 emptyProperties 발생!");
            return;
        }
        movieRepository.save(movie);

    }

    private static void convertMovieInfo(Map<String, Object> movieInfo,
        Map<String, String> convertMovieInfo) {
        log.info("convert ---------=====");
        log.info(movieInfo.toString());
        convertMovieInfo.put("id", movieInfo.get("id").toString());
        convertMovieInfo.put("title", movieInfo.get("title").toString());
        convertMovieInfo.put("movieImg", movieInfo.get("movieImg").toString());
        convertMovieInfo.put("movieBgImg", movieInfo.get("movieBgImg").toString());
        convertMovieInfo.put("originalLanguage", movieInfo.get("originalLanguage").toString());
        convertMovieInfo.put("movieDetail", movieInfo.get("movieDetail").toString());
        convertMovieInfo.put("movieDate", movieInfo.get("movieDate").toString());
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


    public List<MoviePopularDto> isPopularMoviesInTable() throws JsonProcessingException {
        List<PopularMovie> all = popularMovieRepository.findAll();
        log.info(String.valueOf(all.size()));
        if(all.size()>0){
            List<PopularMovie> setConvertMovie = all.subList(0, 10);
            return convertPopularToMoviePopularDto(setConvertMovie);
        }
        return getPopularMovies();
    }

    private List<MoviePopularDto> convertPopularToMoviePopularDto(List<PopularMovie> setConvertMovie) {
       List<MoviePopularDto> converted = new ArrayList<>();
        for (PopularMovie popularMovie : setConvertMovie) {
            MoviePopularDto convertDto = MoviePopularDto.builder()
                .starScore(getAvgScoreByMovieId(popularMovie.getMovieDbId()))
                .movieTitle(popularMovie.getTitle())
                .movieId(popularMovie.getMovieDbId())
                .popularity(popularMovie.getPopularity().intValue())
                .movieImg(popularMovie.getMovieImg())
                .build();

            converted.add(convertDto);
        }
        return converted;
    }
}

