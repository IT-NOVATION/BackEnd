package com.ItsTime.ItNovation.domain.follow;

import com.ItsTime.ItNovation.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FollowState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User pushUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    // @JoinColumn 을 하게 되었을때 name= -> 외래키를 현재 테이블에 어떻게 보이게 할 지 선정하는 이름
    // column property를 따로 지정을 하지 않으면 해당 엔티티의 Id값으로 매핑한다.
    private User targetUser;


    @Builder
    public FollowState(User pushUser, User targetUser) {
        this.pushUser = pushUser;
        this.targetUser = targetUser;
    }


}