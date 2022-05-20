package com.example.WebMessenger.chat.storage.entity.normal;

import com.example.WebMessenger.chat.domain.member.MemberGrade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
public class Member {

    @Id @GeneratedValue @Column(name = "member_id")
    private Long id;
    private String userName;
    private MemberGrade grade;

    @OneToMany(mappedBy = "member")
    private List<MemberRoomInfo> memberRoomInfoList = new ArrayList<>();

    public Member(String userName, MemberGrade grade) {
        this.userName = userName;
        this.grade = grade;
    }

    public Member() {
    }
}
