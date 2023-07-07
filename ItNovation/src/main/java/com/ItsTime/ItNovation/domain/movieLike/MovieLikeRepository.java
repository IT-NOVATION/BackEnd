package com.ItsTime.ItNovation.domain.movieLike;

import com.ItsTime.ItNovation.domain.star.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieLikeRepository extends JpaRepository<Star, Long> {

}
