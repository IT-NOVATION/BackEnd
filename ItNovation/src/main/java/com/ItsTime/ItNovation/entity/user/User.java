package com.ItsTime.ItNovation.entity.user;

import com.ItsTime.ItNovation.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String name;




    @Builder
    public User(Long id, String email,String password, String name, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name =name;
        this.role = role;

    }

    public User update(String name) {
        this.name = name;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }


}
