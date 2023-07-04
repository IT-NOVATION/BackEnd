package com.ItsTime.ItNovation.domain.user;

import com.ItsTime.ItNovation.domain.BaseTimeEntity;
import com.ItsTime.ItNovation.domain.review.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String nickname;
    private String introduction;

    private String bgImg;
    private String profileImg;
    private String refreshToken;
    private String grade;

    //ReviewLike 엔티티 클래스가 Review 엔티티 클래스의 reviewId 필드를 참조하여 연관관계를 맺고 있다는 것
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    // 유저 권한 설정 메소드
    //TODO: role admin으로 해도되나
    public void authorizeUser() {
        this.role = Role.USER;
    }



    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }


    @Builder
    public User(String email, String password, Role role, String nickname, String introduction,   String refreshToken,String bgImg,String profileImg,String grade) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.introduction = introduction;
        this.refreshToken = refreshToken;
        this.bgImg = bgImg;
        this.profileImg=profileImg;
        this.grade=grade;
    }

    public User update(String nickname,String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
        return this;
    }


    public User update(String nickname, String introduction,String profileImg,String bgImg) {
        this.nickname = nickname;
        this.introduction = introduction;
        this.profileImg = profileImg;
        this.bgImg = bgImg;
        return this;
    }

    public User update (String password){
        this.password = password;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }


}
