package com.ItsTime.ItNovation.domain.comment;

import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.user.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query("SELECT c FROM Comment c WHERE c.review.reviewId = :reviewId ORDER BY c.createdDate DESC")
    List<Comment> findByNewestComment(@Param("reviewId")Long reviewId, Pageable pageable);


    @Query("select c from Comment  c where c.review.reviewId=:reviewId")
    List<Comment> findAllByReviewId(@Param("reviewId") Long reviewId);

    @Query("select count(*) from Comment c where c.user.id = :userId")
    int countByCommentByUserId(@Param("userId") Long userId);




}
