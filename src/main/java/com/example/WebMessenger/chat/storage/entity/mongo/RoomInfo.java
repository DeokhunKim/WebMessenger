package com.example.WebMessenger.chat.storage.entity.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;


@Getter @Setter
@Document(collection = "roomInfo")
public class RoomInfo {

    @Id
    private String id;
    private String roomId;
    private String roomName;
    private Set<String> members = new HashSet<>();


    public RoomInfo(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }
}
