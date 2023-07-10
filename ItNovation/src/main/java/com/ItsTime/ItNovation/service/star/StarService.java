package com.ItsTime.ItNovation.service.star;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.star.Star;
import com.ItsTime.ItNovation.domain.star.StarRepository;
import com.ItsTime.ItNovation.domain.star.dto.SingleStarEvaluateRequestDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StarService {
    private final StarRepository starRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;


    public ResponseEntity singleStarEvaluate(@RequestBody SingleStarEvaluateRequestDto singleStarEvaluateRequestDto){
        try{
            User user = userRepository.findById(singleStarEvaluateRequestDto.getUserId())
                    .orElseThrow(()-> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
            Movie movie = movieRepository.findById(singleStarEvaluateRequestDto.getMovieId())
                    .orElseThrow(()-> new IllegalArgumentException("해당 영화가 존재하지 않습니다."));
            Optional<Star> existingStar = starRepository.findByUserAndMovie(user, movie);
            if(existingStar.isPresent()) {//존재하면, 스타 스코어만 업데이트
                Star star = existingStar.get();
                star.updateScore(singleStarEvaluateRequestDto.getStarScore());
                starRepository.save(star);
            }else{//존재하지 않는다면, 새롭게 생성
            Star build = Star.builder()
                    .user(user)
                    .movie(movie)
                    .score(singleStarEvaluateRequestDto.getStarScore())
                    .build();

            starRepository.save(build);
            }
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(200);
    }
}
