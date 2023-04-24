# ItNovation Back-END


###   저희는 [feature branch workflow](https://gmlwjd9405.github.io/2017/10/27/how-to-collaborate-on-GitHub-1.html)에 따라 개발을 진행합니다

1. git clone 명령으로 중앙 원격 저장소를 복제해 자신의 로컬 저장소를 만듭니다
   
   `git clone [중앙 remote repository URL]`

2. 새로운 기능을 추가하고 싶다면 feature/xxx 형태의 브랜치를 main에서 분기합니다

   `git checkout -b feature/login`

3. 새로 만든 브랜치에 대한 새로운 기능에 대한 내용을 커밋합니다

   `git add .`

   `git commit -m “[ 커밋 컨벤션에 따라 작성한다 ]”`

    자세한 커밋 컨벤션은 아래를 참고해주세요!
4. 커밋을 완료했다면, 자신이 작업한 내용을 포함한 브랜치를 중앙 원격 저장소에 올립니다

    `git push -origin feature/login branch`
5. PR 요청 후 프로젝트 관리자는 문제가 없다면 main브랜치에 병합합니다.


###  저희는 다음과 같은 commit convention에 따라 개발을 진행합니다

#### 커밋메시지 형식
    type(scope) : subject
    blank line
    body
    blank line
    footer
    
📌 Type 제목 부분
1. 속성별 Type
* feat: 새로운 기능, 특징 추가
* fix: 수정, 버그 수정
* docs: 문서에 관련된 내용, 문서 수정
* style: 코드 포맷, 세미콜론 누락, 코드 변경이 없을 경우
* refactor: 리팩토링
* test: 테스트 코드 수정, 누락된 테스트를 추가할 때, 리팩토링 테스트 추가
* chore: 빌드 업무 수정, 패키지 매니저 수정

2. 범위를 나타내는 scope

   생략가능

3. 제목을 간단하게 설명하는 subject

* 제목은 최대 50글자가 넘지 않도록 한다.
* 마침표 및 특수기호는 사용하지 않는다. 끝에 점(.) 없음 
* 영어로 작성시 첫 글자를 대문자로 쓰지 않는다. 
* 영문으로 표기하는 경우 동사(원형)를 가장 앞에 둔다.
* 제목은 개조식 구문으로 작성한다. (간결하고 요점적인 서술)

📌 body 본문부분

📌 footer 꼬릿말부분


📌 gitmoji 활용

   예시 `git commit -m ":art: style(scope): subject"`
   
   [자세한 내용은 링크를 참조](https://velog.io/@ninto_2/%EC%BB%A4%EB%B0%8B-%EC%BB%A8%EB%B2%A4%EC%85%98)