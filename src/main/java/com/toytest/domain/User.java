package com.toytest.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class User extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false, length = 32, unique = true)
    private String username;

    private String password;

    @Column(nullable = false,  unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /* 회원 정보 수정 */
    public void modify(String password, String nickname){
        this.password = password;
        this.nickname = nickname;
    }

    /* 소셜 로그인 시 이미 등록된 회원이라면 수정 날짜만 업데이트 해줘서 기존 데이터를 보존하도록 예외 처리 */
    public User updateModifiedDate() {
        this.onPreUpdate();
        return this;
    }

    public String getRoleValue() {
        return this.role.getValue();
    }
}
