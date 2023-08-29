package com.ItsTime.ItNovation.domain.reviewLike;

import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {


    @Override
    Optional<ReviewLike> findById(Long id);

    @Query("SELECT rl.review.user FROM ReviewLike rl " +
        "WHERE rl.reviewLike = true " +
        "AND DATE(rl.createdDate) = :today " +
        "GROUP BY rl.review.user " +
        "ORDER BY COUNT(rl.review.user.id) DESC")
    List<User> findTopUsersWithYesterdayDate(@Param("today")LocalDate date,Pageable pageable);


    @Query("SELECT r FROM Review r " +
        "WHERE r.user.id = :userId " +
        "ORDER BY (SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review = r AND rl.reviewLike = true AND DATE(rl.createdDate) = :today) DESC")
    List<Review> findTopReviewsByUserId(@Param("today") LocalDate date, @Param("userId") Long userId, Pageable pageable);


    @Query("SELECT r FROM Review r " +
        "WHERE r.user.id = :userId " +
        "ORDER BY (SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review = r AND rl.reviewLike = true AND DATE(rl.createdDate) = :today) DESC")
    List<Review> bestReviewsByUserId(@Param("today") LocalDate date, @Param("userId") Long userId);



    @Query("select rl from ReviewLike rl where rl.user.id = :userId AND rl.review.reviewId= :reviewId")
    Optional<ReviewLike> findReviewLikeByReviewIdAndUserId(@Param("userId") Long userId, @Param("reviewId") Long reviewId);



    @Query("select count(*) from ReviewLike rl where rl.review.reviewId=:reviewId and rl.reviewLike=true")
    int countReviewLikeByReviewId(@Param("reviewId") Long reviewId);

    @Query("select rl.reviewLike from ReviewLike rl where rl.user = :user and rl.review = :review")
    Optional<Boolean> isUserLike(@Param("user") User user, @Param("review") Review review);

    @Query("select rl.user from ReviewLike rl where rl.review.reviewId = :reviewId and rl.reviewLike=true")
    List<User> findAllUserByReviwId(@Param("reviewId")Long reviewId);
}
