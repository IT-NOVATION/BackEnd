package com.ItsTime.ItNovation.service.grade;


import com.ItsTime.ItNovation.domain.comment.CommentRepository;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.user.Grade;
import com.ItsTime.ItNovation.domain.user.GradeStandardTable;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public void validateGrade(User findUser) {
        int commentCount = commentRepository.countByCommentByUserId(findUser.getId());
        int reviewCount = reviewRepository.countByUserId(findUser.getId());

        log.info("reviewCount = " + String.valueOf(reviewCount));
        log.info("commentCount = " + String.valueOf(commentCount));

        Grade grade = findUser.getGrade();
        if(grade.getKey().equals(Grade.STANDARD.getKey())){
            validateUpgrade(reviewCount, commentCount, findUser);
        }

        if(grade.getKey().equals(Grade.PREMIUM.getKey())){
            validateUpDown(reviewCount, GradeStandardTable.VIP,GradeStandardTable.PREMIUM, commentCount, findUser);
        }

        if(grade.getKey().equals(Grade.VIP.getKey())){
            validateUpDown(reviewCount, GradeStandardTable.SPECIAL,GradeStandardTable.VIP, commentCount, findUser);
        }

        if(grade.getKey().equals(Grade.SPECIAL.getKey())){
            validateDown(findUser, commentCount, reviewCount);
        }
    }

    private void validateUpgrade(int reviewCount, int commentCount,
        User findUser) {
        System.out.println("GradeStandardTable.PREMIUM.getReviewCount() " + GradeStandardTable.PREMIUM.getReviewCount());
        System.out.println("GradeStandardTable.PREMIUM.getCommentCount() " + GradeStandardTable.PREMIUM.getCommentCount());

        if (reviewCount >= GradeStandardTable.PREMIUM.getReviewCount() && commentCount
            >= GradeStandardTable.PREMIUM.getCommentCount()) {
            upgradeState(findUser, GradeStandardTable.STANDARD.getLevel());
        }
    }

    private void validateDown(User findUser, int commentCount, int reviewCount) {
        if(reviewCount < GradeStandardTable.SPECIAL.getReviewCount() || commentCount < GradeStandardTable.SPECIAL.getCommentCount())
        {
            degradeState(findUser, GradeStandardTable.SPECIAL.getLevel());
        }
    }

    private void validateUpDown(int reviewCount, GradeStandardTable up, GradeStandardTable now, int commentCount,
        User findUser) {
        if (reviewCount < now.getReviewCount() || commentCount < now.getCommentCount()) {
            degradeState(findUser, now.getLevel());
            return;
        }
        if (reviewCount >= up.getReviewCount() && commentCount >= up.getCommentCount()) {
            upgradeState(findUser, now.getLevel());
        }
    }

    private void upgradeState(User findUser, int level) {
        User user = findUser.updateGrade(GradeStandardTable.getByLevel(level + 1).getGrade());
        userRepository.save(user);
    }

    private void degradeState(User findUser, int level) {
        User user = findUser.updateGrade(GradeStandardTable.getByLevel(level - 1).getGrade());
        userRepository.save(user);
    }

}
