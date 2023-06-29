package com.ItsTime.ItNovation.domain.movie;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);
    List<Movie> findByTitleContaining(String keyword);//검색된 영화 배열 방식으로 끌어오기 위해 추가
}
