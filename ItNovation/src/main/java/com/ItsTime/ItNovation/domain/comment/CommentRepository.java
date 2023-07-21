package com.ItsTime.ItNovation.domain.comment;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query("SELECT c FROM Comment c ORDER BY c.createdDate DESC")
    List<Comment> findByNewestComment(Pageable pageable);


}
