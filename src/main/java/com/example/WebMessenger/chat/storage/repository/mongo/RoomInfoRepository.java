package com.example.WebMessenger.chat.storage.repository.mongo;

import com.example.WebMessenger.chat.storage.entity.mongo.RoomInfo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomInfoRepository extends MongoRepository<RoomInfo, String> {
    RoomInfo findByRoomId(String roomId);
    ObjectId findIdByRoomId(String roomId);
}
