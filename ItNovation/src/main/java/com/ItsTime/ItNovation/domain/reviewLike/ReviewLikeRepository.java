package com.ItsTime.ItNovation.domain.reviewLike;

import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
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
    List<User> findTop3UsersWithTodayDate(Pageable pageable);


    @Query("SELECT r FROM Review r " +
        "WHERE r.user.id = :userId " +
        "ORDER BY (SELECT COUNT(rl) FROM ReviewLike rl WHERE rl.review = r AND rl.reviewLike = true AND DATE(rl.createdDate) = CURRENT_DATE) DESC")
    List<Review> findTop2ReviewsByUserId(@Param("userId") Long userId, Pageable pageable);



    @Query("select rl from ReviewLike rl where rl.user.id = :userId AND rl.review.reviewId= :reviewId")
    Optional<ReviewLike> findReviewLikeByReviewIdAndUserId(@Param("userId") Long userId, @Param("reviewId") Long reviewId);

}
