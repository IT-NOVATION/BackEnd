package com.ItsTime.ItNovation.service.grade;


import com.ItsTime.ItNovation.domain.comment.CommentRepository;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {


    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    public void validateGrade(User findUser) {

        commentRepository.findAllByUserId(findUser.getId());


    }
}
