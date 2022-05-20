package com.example.WebMessenger.chat.storage.entity.mongo;


import com.example.WebMessenger.chat.domain.member.MemberGrade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@Document(collection = "member")
public class Member {

    @Id
    private String id;
    private String userName;
    private MemberGrade grade;
    private List<String> roomList = new ArrayList<>();

    public Member(String userName, MemberGrade grade) {
        this.userName = userName;
        this.grade = grade;
    }

    public Member() {
    }
}
