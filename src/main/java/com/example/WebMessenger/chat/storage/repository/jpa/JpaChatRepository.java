package com.example.WebMessenger.chat.storage.repository.jpa;

import com.example.WebMessenger.chat.storage.entity.normal.Chat;
import com.example.WebMessenger.chat.storage.entity.normal.Member;
import com.example.WebMessenger.chat.storage.entity.normal.RoomInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaChatRepository extends JpaRepository<RoomInfo, String> {

    @Query("select r from RoomInfo r")
    List<RoomInfo> testQuery();

    @Query("select m from Member m")
    List<Member> testQuery2();

    @Query("SELECT c FROM Chat c WHERE c.roomInfo.roomId = :roomId ORDER BY c.time DESC")
    Page<Chat> GetRoomChatPageByRoomId(@Param("roomId") String roomId, Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.userName = :userName")
    Member getMemberByUserName(@Param("userName") String userName);

    @Query("SELECT r FROM RoomInfo r JOIN r.memberList rm WHERE rm.member.userName = :userName")
    List<RoomInfo> getRoomListByUserName(@Param("userName") String userName);

    // MEMBER
    // MEMBER_ROOM_INFO
    // ROOM_INFO
    // CHAT


}
