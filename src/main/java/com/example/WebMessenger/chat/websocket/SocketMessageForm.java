package com.example.WebMessenger.chat.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SocketMessageForm {
    private String roomId;
    private String writer;
    private String message;
    private String time;
    private SocketMessageType messageType;

    public SocketMessageForm(String roomId, String writer, String message, String time, SocketMessageType messageType) {
        this.roomId = roomId;
        this.writer = writer;
        this.message = message;
        this.time = time;
        this.messageType = messageType;
    }

    public SocketMessageForm() {
    }
}
