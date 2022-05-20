package com.example.WebMessenger.chat.storage.entity.mongo;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter @Setter
@Document(collection = "chat")
public class Chat {

    private String roomId;
    private String writer;
    private String time;
    private String message;

    public Chat(String roomId, String writer, String time, String message) {
        this.roomId = roomId;
        this.writer = writer;
        this.time = time;
        this.message = message;
    }
}
