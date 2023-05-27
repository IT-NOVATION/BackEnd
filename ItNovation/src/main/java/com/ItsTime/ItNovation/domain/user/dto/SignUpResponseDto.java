package com.ItsTime.ItNovation.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@ToString
@Getter
//@NoArgsConstructor와 생성자 위에 @Builder같이 사용시 에러 안남
//@NoArgsConstructor와 클래스 위에 @Builder같이 사용시 에러 남
//@Builder은 @NoArgsConstrator 어노테이션이나 다른 생성자가 존재하지 않을 때 전체 파라미터를 받는 생성자 자동으로 만들어준다
//그러나 @NoArgsConstructor(access = AccessLevel.PROTECTED)이 있을 경우 protected로 생성자를 만들어준다
// 기본 생성자가 public으로 생성되는 것을 막을 수 있다

// @NoArgsConstructor는 기본생성자를 추가해준다는 의미이고, 이미 다른 생성자가 존재할 경우 기본 생성자를 추가할 수 없다
// 따라서 파라미터를 받는 생성자가 있는데 @NoArgsConstructor를 사용시 에러가 난다. 이경우 RequiredArgsConstructor을 사용하자

//자바는 기본생성자가 반드시 필요하기에 이럴 경우 NoArgsConstructor를 사용하자
//기본생성자는 변수를 초기화 하는 역할도 한다

//@NoArgsConstructor(access = AccessLevel.PROTECTED)는 protected로 생성자를 만듬, 기본 생성자가 public으로 생성되는 것 막음
//@NoArgsConstructor(force = true)는 강제로 인자가 없는 생성자를 생성


//https://cobbybb.tistory.com/14
//https://mangkyu.tistory.com/137?category=761302
public class SignUpResponseDto {
    private final boolean success;
    private final String msg;
    private final Long userId;
    @Builder
    public SignUpResponseDto(boolean success, String msg, Long userId) {
        this.success = success;
        this.msg = msg;
        this.userId = userId;
    }
}
