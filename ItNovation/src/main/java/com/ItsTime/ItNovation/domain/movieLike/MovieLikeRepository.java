package com.ItsTime.ItNovation.domain.movieLike;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.star.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {



    @Query("select count(ml) from MovieLike ml where ml.movie = :movie")
    int countMovieLike(@Param("movie") Movie movie);
}
