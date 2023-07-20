package com.ItsTime.ItNovation.service.topFollower;

import com.ItsTime.ItNovation.domain.follow.FollowRepository;
import com.ItsTime.ItNovation.domain.movie.dto.FollowMovieResponseDto;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.TopFollowerReviewResponseDto;
import com.ItsTime.ItNovation.domain.topFollower.TopFollowerResponseDto;
import com.ItsTime.ItNovation.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class TopFollowerService {
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;

    @Transactional
    public List<TopFollowerResponseDto> getTopFollowers() {
        List<User> topPushUsers = followRepository.findTop3PushUsersByTargetUserCount();

        return topPushUsers.stream().map(topPushUser -> {
            List<TopFollowerReviewResponseDto> reviews = reviewRepository.findNewestReviewByUserId(topPushUser.getId(), PageRequest.of(0, 2))
                    .stream()
                    .map(review -> TopFollowerReviewResponseDto.builder()
                            .reviewId(review.getReviewId())
                            .reviewTitle(review.getReviewTitle())
                            .movie(FollowMovieResponseDto.builder()
                                    .movieId(review.getMovie().getId())
                                    .movieImg(review.getMovie().getMovieImg())
                                    .build())
                            .build())
                    .collect(Collectors.toList());

            return TopFollowerResponseDto.builder()
                    .userId(topPushUser.getId())
                    .profileImg(topPushUser.getProfileImg())
                    .nickName(topPushUser.getNickname())
                    .introduction(topPushUser.getIntroduction())
                    .reviews(reviews)
                    .build();
        }).collect(Collectors.toList());
    }
}

