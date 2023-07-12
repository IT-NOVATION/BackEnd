package com.ItsTime.ItNovation.domain.star;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StarRepository extends JpaRepository<Star, Long> {
    @Query("SELECT AVG(s.score) FROM Star s WHERE s.movie.id = :movieId")
    Float findAvgScoreByMovieId(@Param("movieId") Long movieId);

    Optional<Star> findByUserAndMovie(User user, Movie movie);
}

