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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    @Transactional
    public ResponseEntity writeComment(CommentWriteRequestDto commentDto) {
        try {
            Comment comment = Comment.builder()
                .commentText(commentDto.getCommentText())
                .review(getReview(commentDto))
                .user(getUser(commentDto))
                .build();
            commentRepository.save(comment);
            return ResponseEntity.status(200).body("성공적으로 저장되었습니다.");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    private User getUser(CommentWriteRequestDto commentDto) {
        User user = userRepository.findById(commentDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        return user;
    }

    private Review getReview(CommentWriteRequestDto commentDto) {
        Review review = reviewRepository.findById(commentDto.getReviewId())
            .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없습니다."));
        return review;
    }


    @Transactional
    public ResponseEntity readComment(int page) {
        try {
            Pageable pageable = PageRequest.of(page - 1, 5);
            List<Comment> newestByComment = commentRepository.findByNewestComment(pageable);
            List<CommentReadDto> commentReadDtoList = new ArrayList<>();
            int lastPage = getLastPage();
                CommentReadResponseDto responseDto = getCommentReadResponseDto(
                    page, newestByComment, commentReadDtoList, lastPage);
                return ResponseEntity.status(200).body(responseDto);
        }catch (Exception e){
            return ResponseEntity.status(400).body("댓글을 읽는데 오류가 발생했습니다.");
        }
    }

    private CommentReadResponseDto getCommentReadResponseDto(int page,
        List<Comment> newestByComment, List<CommentReadDto> commentReadDtoList, int lastPage) {
        for(int i=0; i< newestByComment.size(); i++){
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

    private int getLastPage() {
        if(commentRepository.findAll().size()%5==0){
            return commentRepository.findAll().size()/5;
        }
        return commentRepository.findAll().size()/5+1;
    }

    @Transactional
    public ResponseEntity deleteComment(Long commentId) {
        try {
            commentRepository.deleteById(commentId);
            return ResponseEntity.status(200).body("삭제 성공했습니다.");
        }catch (Exception e){
            return ResponseEntity.status(400).body("삭제에 실패했습니다.");
        }
    }
}
