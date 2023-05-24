package com.ItsTime.ItNovation.domain.user;

import com.ItsTime.ItNovation.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private SocialType socialType;
    private String imgUrl;
    private String socialId;
    private String refreshToken;


    // 유저 권한 설정 메소드
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
    public User(String email, String password, Role role, String nickname, String introduction, SocialType socialType, String socialId, String refreshToken,String imgUrl) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.introduction = introduction;
        this.socialType = socialType;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
        this.imgUrl = imgUrl;
    }

    public User update(String nickname,String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }


}
