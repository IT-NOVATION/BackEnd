package com.ItsTime.ItNovation.domain.movieLike;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.star.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {

    @Query("select count(ml) from MovieLike ml where ml.movie = :movie")
    int countMovieLike(@Param("movie") Movie movie);


    Optional <MovieLike> findByUserIdAndMovieId(Long userId, Long movieId);

    @Query("select count(ml) from MovieLike ml where ml.user.id=:userId and ml.movie.id=:movieId")
    int MovieLikeCountByUserIdAndMovieId(@Param("userId") Long userId, @Param("movieId") Long movieId);

    @Query("select ml.movie from MovieLike ml where ml.user.id=:userId")
    List<Movie> myInterestedMovie(@Param("userId") Long userId);
}
