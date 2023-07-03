package com.ItsTime.ItNovation.domain.star;

import org.springframework.data.jpa.repository.JpaRepository;
public interface StarRepository extends JpaRepository<Star, Long> {
    Float findAvgScoreByMovieId(Long movieId);
}

