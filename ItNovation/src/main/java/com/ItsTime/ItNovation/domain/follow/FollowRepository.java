package com.ItsTime.ItNovation.domain.follow;

import com.ItsTime.ItNovation.domain.user.User;

import java.util.ArrayList;
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


    //TODO: FollowState와 연관안되어있는 user도 가져오게 쿼리 수정필요

    @Query("SELECT u FROM User u left JOIN FollowState f ON u.id = f.targetUser.id GROUP BY u.id ORDER BY COUNT(f.id) DESC")
    List<User> findTop3PushUsersByTargetUserCount(Pageable pageable);



    @Query("select count(*) from FollowState f where f.pushUser.id=:userId")

    Long countByFollowingUserId(@Param("userId") Long userId);


    @Query("SELECT f.pushUser FROM FollowState f WHERE f.targetUser.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT f.targetUser FROM FollowState f where f.pushUser.id=:userId")
    List<User> findFollowingsByUserId(@Param("userId") Long userId);


}
