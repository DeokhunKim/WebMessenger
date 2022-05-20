package com.example.WebMessenger.chat.domain.session;


import com.example.WebMessenger.chat.domain.member.Member;
import com.example.WebMessenger.chat.domain.member.MemberGrade;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class SessionMemberForm {
    private String userName;
    private MemberGrade grade;

    public SessionMemberForm(Member member) {
        this.userName = member.getUserName();
        this.grade = member.getGrade();
    }

    public SessionMemberForm(String userName, MemberGrade grade) {
        this.userName = userName;
        this.grade = grade;
    }
}
