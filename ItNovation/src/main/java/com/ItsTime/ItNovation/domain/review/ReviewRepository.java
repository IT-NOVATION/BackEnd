package com.ItsTime.ItNovation.domain.review;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findById(Long ReviewId);



    @Query("Select r from Review r where r.user.id=:userId order by r.createdDate desc")
    List<Review> findNewestReviewByUserId(@Param("userId") Long userId, Pageable pageable);


    @Query("SELECT AVG(r.star) FROM Review r WHERE r.movie.id = :movieId")
    Float findAvgScoreByMovieId(@Param("movieId") Long movieId);
}