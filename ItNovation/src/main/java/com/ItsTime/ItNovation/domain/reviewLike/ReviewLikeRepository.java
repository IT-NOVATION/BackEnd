package com.ItsTime.ItNovation.domain.reviewLike;

import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {


    @Override
    Optional<ReviewLike> findById(Long id);

    @Query("SELECT rl.user FROM ReviewLike rl " +
        "WHERE rl.reviewLike = true " +
        "AND DATE(rl.createdDate) = CURRENT_DATE " +
        "GROUP BY rl.user " +
        "ORDER BY COUNT(rl.user) DESC")
    List<User> findTopUsersWithTodayDate(Pageable pageable);


    @Query("SELECT r FROM Review r " +
        "WHERE r.user.id = :userId " +
        "ORDER BY (SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review = r AND rl.reviewLike = true AND DATE(rl.createdDate) = CURRENT_DATE) DESC")
    List<Review> findTopReviewsByUserId(@Param("userId") Long userId, Pageable pageable);


    @Query("SELECT r FROM Review r " +
        "WHERE r.user.id = :userId " +
        "ORDER BY (SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review = r AND rl.reviewLike = true AND DATE(rl.createdDate) = CURRENT_DATE) DESC")
    List<Review> bestReviewsByUserId(@Param("userId") Long userId);



    @Query("select rl from ReviewLike rl where rl.user.id = :userId AND rl.review.reviewId= :reviewId")
    Optional<ReviewLike> findReviewLikeByReviewIdAndUserId(@Param("userId") Long userId, @Param("reviewId") Long reviewId);



    @Query("select count(*) from ReviewLike rl where rl.review.reviewId=:reviewId and rl.reviewLike=true")
    int countReviewLikeByReviewId(@Param("reviewId") Long reviewId);

}
