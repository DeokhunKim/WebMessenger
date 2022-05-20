package com.example.WebMessenger.chat.storage.entity.normal;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Chat {

    @Id @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "roomId")
    private RoomInfo roomInfo;
    private String writer;
    private String time;
    private String message;

    public Chat(RoomInfo roomInfo, String writer, String time, String message) {
        this.roomInfo = roomInfo;
        this.writer = writer;
        this.time = time;
        this.message = message;
    }

    public Chat() {
    }
}
