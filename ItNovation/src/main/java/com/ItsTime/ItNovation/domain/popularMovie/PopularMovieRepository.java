package com.ItsTime.ItNovation.domain.popularMovie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularMovieRepository extends JpaRepository<PopularMovie, Long> {
    PopularMovie save(PopularMovie popularMovie);

}
