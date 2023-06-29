package com.ItsTime.ItNovation.domain.star;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StarRepository extends JpaRepository<Star, Long> {
    @Query("SELECT AVG(s.score) FROM Star s WHERE s.movie.id = :movieId")//여기서 movie.id는 star 객체의 moive_id
    Float findAverageScoreByMovieId(@Param("movieId") Long movieId);
}
