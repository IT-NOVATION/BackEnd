package com.ItsTime.ItNovation.service.movielog;


import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movieLike.MovieLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieLikeService {
    private final MovieLikeRepository movieLikeRepository;
    @Transactional
    public List<Movie> UserInterestedMovieList(Long userId){
        return movieLikeRepository.myInterestedMovie(userId);
    }

}
