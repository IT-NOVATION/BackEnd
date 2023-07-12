package com.ItsTime.ItNovation.experiment;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.service.movie.MovieRepoService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
public class SaveTest {


    @Autowired
    private MovieRepoService repoService;
    @Autowired
    private MovieRepository movieRepository;

    @Test
    @Transactional
    public void 같은_트랜잭션에서_같은객체인지(){
        //given

        Movie originMovie = Movie.builder().
            movieCountry("en").movieGenre("12").movieRunningTime(123).movieDirector("123")
            .movieDetail("fwefsv").movieDate("1234").movieActor("12345")
            .title("helloworld").movieImg("1234").movieBgImg("12345").real_movieId(444332L).build();

        //when
        movieRepository.save(originMovie);
        Optional<Movie> helloworld = movieRepository.findByTitle("helloworld");
        Movie findMovie = helloworld.get();
        //then
        Assertions.assertThat(originMovie).isEqualTo(findMovie); // 같은 영속

    }


    @Test
    @Rollback(value = false)
    @Transactional
    public void 미리DB에저장(){
        Movie originMovie = Movie.builder().
            movieCountry("en").movieGenre("12").movieRunningTime(123).movieDirector("123")
            .movieDetail("fwefsv").movieDate("1234").movieActor("12345")
            .title("helloworld2").movieImg("1234").movieBgImg("12345").real_movieId(444332L).build();

        Map<String, Movie> titleAndMovie= new HashMap<>();
        titleAndMovie.put("helloworld2", originMovie);
        repoService.saveMovie(titleAndMovie); // 기존에 있는 경우 다시 넣지 않음.
    }


    @Test
    @Transactional
    public void 기존db에서불러왔을때_equal하게보는지(){
        //given 기존 db에 저장되어있는 친구 정보
        Movie originMovie = Movie.builder().
            movieCountry("en").movieGenre("12").movieRunningTime(123).movieDirector("123")
            .movieDetail("fwefsv").movieDate("1234").movieActor("12345")
            .title("helloworld2").movieImg("1234").movieBgImg("12345").real_movieId(444332L).build();

        //when
        Optional<Movie> helloworld = movieRepository.findByTitle("helloworld2");
        Movie findMovie = helloworld.get();

        //then
        Assertions.assertThat(originMovie).isEqualTo(findMovie);
        // -> DB에 이후에 먼저 저장되어있는 친구를 찾아서 기존의 친구의 형태와 비교하면 다르게 됨.
        //1차 캐시는 트랜잭션이 끝나기 직전에는 계속 존재하고 있음.
        //flush는 영속성 컨텍스트를 비우는게 아니라 변경내용을 데이터베이스에 동기화 하는 작업임.
    }


    @Test
    @Transactional
    @Rollback(value = false)
    public void db에존재하고있는_친구_가져와서_변경시_기존DB_Record에변경하는지(){
        //given db에 저장되어있는 친구 정보
        Movie originMovie = Movie.builder().
            movieCountry("en").movieGenre("12").movieRunningTime(123).movieDirector("123")
            .movieDetail("fwefsv").movieDate("1234").movieActor("12345")
            .title("helloworld2").movieImg("1234").movieBgImg("12345").real_movieId(444332L).build();

        //when
        Optional<Movie> helloworld = movieRepository.findByTitle("helloworld2");
        Movie findMovie = helloworld.get();
        Long findMovieId = findMovie.getId();
//        findMovie.setMovieCountry("china");
        movieRepository.save(findMovie); // 이렇게 하면 변경이 진행됨.
        //then
        Optional<Movie> checkMovie = movieRepository.findById(findMovieId);
        Movie changeValueMovie = checkMovie.get();
        Assertions.assertThat(changeValueMovie.getId()).isEqualTo(findMovie.getId());
        // -> DB에 이후에 먼저 저장되어있는 친구를 찾아서 기존의 친구의 형태와 비교하면 다르게 됨.
        //1차 캐시는 트랜잭션이 끝나기 직전에는 계속 존재하고 있음.
        //flush는 영속성 컨텍스트를 비우는게 아니라 변경내용을 데이터베이스에 동기화 하는 작업임.

    }

}
