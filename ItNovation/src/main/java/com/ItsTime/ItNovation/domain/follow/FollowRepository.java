package com.ItsTime.ItNovation.domain.follow;

import com.ItsTime.ItNovation.domain.user.User;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follower, Long> {


    Optional<Follower> findByPushUser(User user);

    @Query("Select f from Follower  f where f.pushUser.id=:pushUser and f.follower.id=:targetUser")
    Optional<Follower> findByPushUserAndFollowUser(@Param("pushUser") Long pushUserId, @Param("targetUser") Long targetUserId);


    @Query("delete Follower f where f.pushUser.id=:pushUser and f.follower.id=:targetUser")
    void deleteByPushUserAndFollowUser(@Param("pushUser") Long pushUserId, @Param("targetUser") Long targetUserId);



    Optional<Follower> findById(Long id);



}
