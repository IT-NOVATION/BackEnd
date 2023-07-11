package com.ItsTime.ItNovation.domain.star;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long starId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Float score;

    @Builder
    public Star( Movie movie, User user, Float score) {
        this.movie = movie;
        this.user = user;
        this.score = score;
    }

    public void updateScore(Float score) {
        this.score = score;//이미 해당 영화에 대한 평가를 한 적이 있다면 수정합니다.
    }
}
