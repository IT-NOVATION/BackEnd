package com.ItsTime.ItNovation.domain.follow;

import com.ItsTime.ItNovation.domain.user.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<FollowState, Long> {


    Optional<FollowState> findByPushUser(User user);

    @Query("Select f from FollowState  f where f.pushUser.id=:pushUser and f.targetUser.id=:targetUser")
    Optional<FollowState> findByPushUserAndFollowUser(@Param("pushUser") Long pushUserId, @Param("targetUser") Long targetUserId);


    @Query("delete FollowState f where f.pushUser.id=:pushUser and f.targetUser.id=:targetUser")
    void deleteByPushUserAndFollowUser(@Param("pushUser") Long pushUserId, @Param("targetUser") Long targetUserId);


    Optional<FollowState> findById(Long id);

    @Query("select count(*) from FollowState f where f.targetUser.id=:userId")
    Long countByFollowedUserId(@Param("userId") Long userId);


    @Query("SELECT f.pushUser FROM FollowState f GROUP BY f.pushUser ORDER BY COUNT(f.targetUser) DESC")
    List<User> findTop3PushUsersByTargetUserCount(Pageable pageable);

    @Query("SELECT f.pushUser FROM FollowState f WHERE f.targetUser.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT f.targetUser FROM FollowState f where f.pushUser.id=:userId")
    List<User> findFollowingsByUserId(@Param("userId") Long userId);


    default List<User> findTop3PushUsersByTargetUserCount() {
        return findTop3PushUsersByTargetUserCount(PageRequest.of(0, 3));
    }

}
