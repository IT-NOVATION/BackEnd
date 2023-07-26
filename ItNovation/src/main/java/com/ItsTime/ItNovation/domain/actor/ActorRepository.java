package com.ItsTime.ItNovation.domain.actor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findAllByMovieTitle(String movieTitle);

    Optional<Actor> findActorByActorName(String actorName);
}
