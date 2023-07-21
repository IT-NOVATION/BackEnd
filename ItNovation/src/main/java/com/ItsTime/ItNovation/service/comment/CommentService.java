package com.ItsTime.ItNovation.service.comment;


import com.ItsTime.ItNovation.domain.comment.Comment;
import com.ItsTime.ItNovation.domain.comment.CommentRepository;
import com.ItsTime.ItNovation.domain.comment.dto.CommentReadDto;
import com.ItsTime.ItNovation.domain.comment.dto.CommentReadResponseDto;
import com.ItsTime.ItNovation.domain.comment.dto.CommentWriteRequestDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.grade.GradeService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity writeComment(CommentWriteRequestDto commentDto, String email) {
        try {
            User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
            Comment comment = Comment.builder()
                .commentText(commentDto.getCommentText())
                .review(getReview(commentDto))
                .user(findUser)
                .build();
            commentRepository.save(comment);
            return ResponseEntity.status(200).body("성공적으로 저장되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    private Review getReview(CommentWriteRequestDto commentDto) {
        Review review = reviewRepository.findById(commentDto.getReviewId())
            .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없습니다."));
        return review;
    }


    @Transactional
    public ResponseEntity readComment(int page, Long reviewId) {
        try {

            Pageable pageable = PageRequest.of(page - 1, 15);
            List<Comment> newestByComment = commentRepository.findByNewestComment(reviewId, pageable);
            List<CommentReadDto> commentReadDtoList = new ArrayList<>();
            int lastPage = getLastPage(reviewId);
            validatePageRequest(page, lastPage);
            CommentReadResponseDto responseDto = getCommentReadResponseDto(
                page, newestByComment, commentReadDtoList, lastPage);

            return ResponseEntity.status(200).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    private void validatePageRequest(int page, int lastPage) {
        log.info(String.valueOf(lastPage));
        log.info(String.valueOf(page));
        if (lastPage < page) {
            throw new IllegalArgumentException("잘못된 페이지를 호출하였습니다!");
        }
    }

    private CommentReadResponseDto getCommentReadResponseDto(int page,
        List<Comment> newestByComment, List<CommentReadDto> commentReadDtoList, int lastPage) {
        for (int i = 0; i < newestByComment.size(); i++) {
            Comment nowComment = newestByComment.get(i);
            CommentReadDto readDto = buildReadDto(nowComment);
            commentReadDtoList.add(readDto);
        }

        CommentReadResponseDto responseDto = CommentReadResponseDto.builder()
            .commentList(commentReadDtoList)
            .lastPage(lastPage)
            .nowPage(page)
            .firstPage(1)
            .build();
        return responseDto;
    }

    private CommentReadDto buildReadDto(Comment nowComment) {
        CommentReadDto readDto = CommentReadDto.builder()
            .commentText(nowComment.getCommentText())
            .commentId(nowComment.getId())
            .createDate(getCreatedDate(nowComment))
            .build();
        return readDto;
    }

    private String getCreatedDate(Comment nowComment) {
        LocalDateTime createdDate = nowComment.getCreatedDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String formattedDateTime = createdDate.format(formatter);

        return formattedDateTime;
    }

    private int getLastPage(Long reviewId) {
        if (commentRepository.findAllByReviewId(reviewId).size() % 15 == 0) {
            return commentRepository.findAllByReviewId(reviewId).size() / 15;
        }
        return commentRepository.findAll().size() / 15 + 1;
    }

    @Transactional
    public ResponseEntity deleteComment(Long commentId) {
        try {
            commentRepository.deleteById(commentId);

            //GradeService.checkGrade()
            return ResponseEntity.status(200).body("삭제 성공했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("삭제에 실패했습니다.");
        }
    }
}
