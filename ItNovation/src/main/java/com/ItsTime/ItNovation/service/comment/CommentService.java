package com.ItsTime.ItNovation.service.comment;


import com.ItsTime.ItNovation.common.exception.BadRequestException;
import com.ItsTime.ItNovation.common.exception.ErrorCode;
import com.ItsTime.ItNovation.common.exception.NotFoundException;
import com.ItsTime.ItNovation.domain.comment.Comment;
import com.ItsTime.ItNovation.domain.comment.CommentRepository;
import com.ItsTime.ItNovation.domain.comment.dto.CommentReadDto;
import com.ItsTime.ItNovation.domain.comment.dto.CommentReadResponseDto;
import com.ItsTime.ItNovation.domain.comment.dto.CommentUserInfoDto;
import com.ItsTime.ItNovation.domain.comment.dto.CommentWriteRequestDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.user.Grade;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.grade.GradeService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    private final GradeService gradeService;

    @Transactional
    public ResponseEntity writeComment(CommentWriteRequestDto commentDto, String email) {
        try {
            User findUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
            Comment comment = Comment.builder()
                .commentText(commentDto.getCommentText())
                .review(getReview(commentDto))
                .user(findUser)
                .build();
            commentRepository.save(comment);
            gradeService.validateGrade(findUser);
            return ResponseEntity.status(HttpStatus.OK).body("성공적으로 저장되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    private Review getReview(CommentWriteRequestDto commentDto) {
        Review review = reviewRepository.findById(commentDto.getReviewId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));
        return review;
    }


    @Transactional
    public ResponseEntity readComment(int page, Long reviewId) {
        try {
            Pageable pageable = PageRequest.of(page - 1, 15);
            List<Comment> newestByComment = commentRepository.findByNewestComment(reviewId, pageable);
            log.info(String.valueOf(newestByComment.size()));
            List<CommentReadDto> commentReadDtoList = new ArrayList<>();
            int lastPage = getLastPage(reviewId);
            if(lastPage==0){
                lastPage=1;
            }
            validatePageRequest(page, lastPage);
            CommentReadResponseDto responseDto = getCommentReadResponseDto(
                page, reviewId, newestByComment, commentReadDtoList, lastPage);

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private void validatePageRequest(int page, int lastPage) {
        log.info(String.valueOf(lastPage));
        log.info(String.valueOf(page));
        if (lastPage < page) {
            throw new IllegalArgumentException("잘못된 페이지를 호출하였습니다!");
        }
    }

    private CommentReadResponseDto getCommentReadResponseDto(int page, Long reviewId,
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
            .totalCommentCount(commentRepository.findAllByReviewId(reviewId).size())
            .firstPage(1)
            .build();
        return responseDto;
    }

    private CommentReadDto buildReadDto(Comment nowComment) {
        CommentReadDto readDto = CommentReadDto.builder()
            .commentText(nowComment.getCommentText())
            .commentId(nowComment.getId())
            .createDate(getCreatedDate(nowComment))
            .commentUserInfo(buildCommentUserInfo(nowComment))
            .build();
        return readDto;
    }

    private CommentUserInfoDto buildCommentUserInfo(Comment nowComment) {
        User user = nowComment.getUser();
        return CommentUserInfoDto.builder()
            .userId(user.getId())
            .profileImg(user.getProfileImg())
            .nickname(user.getNickname())
            .build();
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
        return commentRepository.findAllByReviewId(reviewId).size() / 15 + 1;
    }

    @Transactional
    public ResponseEntity deleteComment(Long commentId) {
        try {
            User user = extractCommentUser(commentId);
            commentRepository.deleteById(commentId);
            gradeService.validateGrade(user);
            return ResponseEntity.status(200).body("삭제 성공했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("삭제에 실패했습니다.");
        }
    }

    private User extractCommentUser(Long commentId) {
        User user = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("해당 댓글 없습니다.")).getUser();
        return user;
    }
}
