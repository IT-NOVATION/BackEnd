package com.ItsTime.ItNovation.domain.movie;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitle(String title);
    Optional<Movie> findById(Long id);

}
