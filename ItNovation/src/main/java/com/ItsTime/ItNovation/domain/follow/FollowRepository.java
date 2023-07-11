package com.ItsTime.ItNovation.domain.follow;

import com.ItsTime.ItNovation.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<FollowState, Long> {


    Optional<FollowState> findByPushUser(User user);

    @Query("Select f from FollowState  f where f.pushUser.id=:pushUser and f.follower.id=:targetUser")
    Optional<FollowState> findByPushUserAndFollowUser(@Param("pushUser") Long pushUserId, @Param("targetUser") Long targetUserId);


    @Query("delete FollowState f where f.pushUser.id=:pushUser and f.follower.id=:targetUser")
    void deleteByPushUserAndFollowUser(@Param("pushUser") Long pushUserId, @Param("targetUser") Long targetUserId);



    Optional<FollowState> findById(Long id);

    @Query("select count(*) from FollowState f where f.follower.id=:userId")
    Long countByFollowedUserId(@Param("userId") Long userId);



}
