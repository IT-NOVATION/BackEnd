package com.ItsTime.ItNovation.domain.movieLike;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.star.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {
    Optional <MovieLike> findByUserIdAndMovieId(Long userId, Long movieId);
}
