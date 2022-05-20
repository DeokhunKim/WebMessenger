package com.example.WebMessenger.chat.storage.repository.mongo;

import com.example.WebMessenger.chat.storage.entity.mongo.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByRoomId(String roomId);

}
