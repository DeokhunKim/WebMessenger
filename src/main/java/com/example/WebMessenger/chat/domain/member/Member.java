package com.example.WebMessenger.chat.domain.member;

import lombok.Getter;
import lombok.Setter;

import static com.example.WebMessenger.chat.domain.member.MemberGrade.ROLE_USER;


@Getter @Setter
public class Member {
    private long sid;
    private String userName;
    private String password;
    private MemberGrade grade;

    public Member(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.grade = ROLE_USER;
    }

    public Member(String userName, String password, MemberGrade grade) {
        this.userName = userName;
        this.password = password;
        this.grade = grade;
    }
}
