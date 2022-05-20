package com.example.WebMessenger.chat.storage.entity.normal;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class RoomInfo {

    @Id
    private String roomId;
    @OneToMany(mappedBy = "roomInfo")
    private List<Chat> chatList = new ArrayList<>();
    private String roomName;

    @OneToMany(mappedBy = "roomInfo")
    private List<MemberRoomInfo> memberList = new ArrayList<>();

    public RoomInfo(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public RoomInfo() {
    }
    //public void addMember(Member member) {
    //    members.add(member);
    //}
}
