package com.ItsTime.ItNovation.config.auth.dto;

import com.ItsTime.ItNovation.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    static final long serialVersionUID = 1L;
    private String name;
    private String email;

    public SessionUser(User user) {
        this.name = user.getNickname();
        this.email = user.getEmail();

    }

}
