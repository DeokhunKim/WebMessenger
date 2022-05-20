package com.example.WebMessenger.chat.storage.entity.normal;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MemberRoomInfo {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne @JoinColumn(name = "roomId")
    private RoomInfo roomInfo;

    public MemberRoomInfo(Member member, RoomInfo roomInfo) {
        this.member = member;
        this.roomInfo = roomInfo;
    }

    public MemberRoomInfo() {
    }
}
