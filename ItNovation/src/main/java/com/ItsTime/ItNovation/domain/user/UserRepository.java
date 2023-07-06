package com.ItsTime.ItNovation.domain.user;

import java.util.List;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByRefreshToken(String refreshToken);

    @Query("SELECT u FROM User u WHERE LOWER(u.nickname) LIKE CONCAT('%', :nickname, '%') ORDER BY CASE WHEN u.nickname = :nickname THEN 1 ELSE 0 END DESC")
    List<User> findByNickname(@Param("nickname") String nickname);
}
