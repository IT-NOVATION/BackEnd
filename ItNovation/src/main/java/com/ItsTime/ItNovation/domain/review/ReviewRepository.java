package com.ItsTime.ItNovation.domain.review;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.user.User;
import java.util.List;

import com.ItsTime.ItNovation.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findById(Long ReviewId);


    Optional<Review> findByUser(User user);

    @Query("Select r from Review r where r.user.id=:userId order by r.createdDate desc")
    List<Review> findNewestReviewByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("Select r from Review r where r.user.id=:userId order by r.createdDate desc")
    List<Review> findNewestReviewByUserIdWithNoPageable(@Param("userId") Long userId);


    @Query("SELECT AVG(r.star) FROM Review r WHERE r.movie.id = :movieId")
    Float findAvgScoreByMovieId(@Param("movieId") Long movieId);

    @Query("select r from Review r where r.movie = :movie order by (select count(rl) from ReviewLike rl where rl.review = r and rl.reviewLike = true ) desc")
    List<Review> findAllByMovie(@Param("movie") Movie findMovie);

    @Query("select count(r) from Review r where r.movie = :movie and r.hasGoodStory = true")
    int countHasGoodStory(@Param("movie") Movie movie);

    @Query("select count(r) from Review r where r.movie = :movie and r.hasGoodProduction = true")
    int countHasGoodProduction(@Param("movie") Movie movie);

    @Query("select count(r) from Review r where r.movie = :movie and r.hasGoodScenario = true")
    int countHasGoodScenario(@Param("movie") Movie movie);

    @Query("select count(r) from Review r where r.movie = :movie and r.hasGoodDirecting = true")
    int countHasGoodDirecting(@Param("movie") Movie movie);

    @Query("select count(r) from Review r where r.movie = :movie and r.hasGoodOst = true")
    int countHasGoodOST(@Param("movie") Movie movie);

    @Query("select count(r) from Review r where r.movie = :movie and r.hasGoodVisual = true")
    int countHasGoodVisual(@Param("movie") Movie movie);

    @Query("select count(r) from Review r where r.movie = :movie and r.hasGoodActing = true")
    int countHasGoodActing(@Param("movie") Movie movie);

    @Query("select count(r) from Review r where r.movie = :movie and r.hasGoodCharterCharming = true")
    int countHasGoodCharacterCharming(@Param("movie") Movie movie);

    @Query("select count(r) from Review r where r.movie = :movie and r.hasGoodDiction = true")
    int countHasGoodDiction(@Param("movie") Movie movie);

    Long countByMovieId(Long movieId);


    @Query("SELECT r.user FROM Review r ORDER BY r.createdDate DESC")
    List<User> findNewestReview(Pageable pageable);

    Optional<Review> findByUserAndMovie(User user,Movie movie);

    Long countByUserAndMovie(User user, Movie movie);
}