package com.example.WebMessenger.chat.websocket;

import com.example.WebMessenger.chat.bot.ChatBotManager;
import com.example.WebMessenger.chat.domain.ChatDto;
import com.example.WebMessenger.chat.domain.ChatLogDto;
import com.example.WebMessenger.chat.domain.RoomInfoDto;
import com.example.WebMessenger.chat.domain.member.MemberGrade;
import com.example.WebMessenger.chat.domain.session.SessionMemberForm;
import com.example.WebMessenger.chat.session.ChatSessionConst;
import com.example.WebMessenger.chat.storage.manager.ChatRepositoryManager;
import com.example.WebMessenger.chat.websocket.html.HtmlCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.WebMessenger.chat.websocket.html.HtmlCreator.MyChat;
import static com.example.WebMessenger.chat.websocket.html.HtmlCreator.OtherChat;


/**
 * 채팅 브라우저와 주고 받는 socket 처리 하는 handler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class  ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatRepositoryManager repository;
    @Autowired
    private ChatBotManager chatBotConfig;

    //TODO private
    public List<WebSocketSession> sessions = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 세션에 접속 했을 때 동작 하는 메소드
     * 1. 접속한 사용자의 session 등록
     * 2. 접속한 사용자의 member 등록
     * 3. 접속한 사용자에게 챗봇 대화방 할당
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 세션 등록
        synchronized (sessions) {
            sessions.add(session);
        }

        //log.info("socket connect: {} , size:{}", session, sessions.size());

        // 멤버 등록
        repository.saveMember(getUserName(session), getUserGrade(session));

        // 챗봇 설정
        chatBotConfig.initDefaultChatBotToUser(getUserName(session), getUserGrade(session));
    }

    /**
     * 웹 브라우저를 통해 메세지를 받았을 때 동작하는 메소드
     * 메세지의 SocketMessageType 따라서 필요한 동작을 수행한다
     */
    @Override
    protected synchronized void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        SocketMessageForm msg = objectMapper.readValue(message.getPayload(), SocketMessageForm.class);
        switch (msg.getMessageType()) {
            case CONNECT:
                handleTextMessage_CONNECT(session);
                break;
            case LEAVE:
                break;
            case WRITE:
                handleTextMessage_WRITE(session, msg);
                break;
            case ADD_ROOM:
                handleTextMessage_ADD_ROOM(session, msg);
                break;
            case CREATE_CHATLIST:
                ShandleTextMessage_CREATE_CHATLIST(session, msg.getRoomId());
                break;
        }
    }

    /**
     * 누군가 처음으로 Socket에 접속했을 때 수행
     * 1. 대화방 목록을 조회하여 대화방 div 를 생성하여 브라우저에 전달
     * 2. 맨위 대화방을 찾아서 그 대화방 내용 div 를 생성하여 브라우저에 전달

     */
    private void handleTextMessage_CONNECT(WebSocketSession session) throws IOException {
        List<RoomInfoDto> roomList = repository.getRoomListByUserName(getUserName(session));
        if (roomList == null || roomList.size() == 0) {
            return;
        }

        // step 1
        SocketMessageForm roomListForm = new SocketMessageForm(
                "", "", createRoomDiv(session), "", SocketMessageType.CREATE_ROOMLIST);
        String roomListRequestStr = objectMapper.writeValueAsString(roomListForm);
        session.sendMessage(new TextMessage(roomListRequestStr));

        // step 2
        SocketMessageForm chatListForm = new SocketMessageForm(
                roomList.get(0).getId(), "", createChatDiv(session, roomList.get(0).getId()), "", SocketMessageType.CREATE_CHATLIST);
        String chatListRequestStr = objectMapper.writeValueAsString(chatListForm);
        session.sendMessage(new TextMessage(chatListRequestStr));
    }

    /**
     * 클라이언트에서 메세지를 보냈을때 수행
     * 1. 받은 메세지를 Repository 에 저장
     * 2. 메세지 송신자에게 채팅장 추가 html 전송
     * 3. 메세지 수신자들에게 메세지 수신 알림 및 채팅창 추가 html 전송
     * 3-1. 수신자: [현재 세선에 접속중인 사람] 중 [해당 메세지의 room 참가자]
     * 4. 챗봇에게 보냈으면 챗봇 답장 처리
     */
    private void handleTextMessage_WRITE(WebSocketSession session, SocketMessageForm msg) throws IOException {

        ChatDto chat = new ChatDto(msg.getWriter(), msg.getMessage(), msg.getTime());

        // step 1
        repository.saveChat(chat, msg.getRoomId());

        // step 2 TODO : 이걸 그냥 웹 브라우저에서 처리할까???
        SocketMessageForm returnMessageForm = new SocketMessageForm(
                msg.getRoomId(), msg.getWriter(), HtmlCreator.getChat(chat, MyChat), msg.getTime(), SocketMessageType.WRITE);
        String returnMessageStr = objectMapper.writeValueAsString(returnMessageForm);
        session.sendMessage(new TextMessage(returnMessageStr));


        // step 3 TODO : 동일한 계정이 여러곳에 접속해있을때?? 각 세션이 다른데??
        SocketMessageForm sendMessageForm = new SocketMessageForm(
                msg.getRoomId(), msg.getWriter(), HtmlCreator.getChat(chat, OtherChat), msg.getTime(), SocketMessageType.WRITE);
        String sendMessageStr = objectMapper.writeValueAsString(sendMessageForm);
        List<String> membersInRoom = repository.getRoomInfoByRoomId(msg.getRoomId()).getMembers();

        /* 오류가 나서 아래로 수정
        for (WebSocketSession s : sessions) {
            String member = membersInRoom.stream()
                    .filter(m -> m == getUserName(s) && !m.equals(msg.getWriter()))
                    .findFirst().orElse(null);
            if (member != null && !chatBotConfig.isBotName(member)) {
                s.sendMessage(new TextMessage(sendMessageStr));
            }
        }
         */

        for (WebSocketSession s : sessions) {
            for (String member : membersInRoom) {
                if ( s != null && getUserName(s).equals(member) && !member.equals(msg.getWriter()) ) {
                    if (!chatBotConfig.isBotName(member)) {
                        s.sendMessage(new TextMessage(sendMessageStr));
                    }
                }
            }
        }

        // 챗봇 자동응답
        for (String member : membersInRoom) {
            if (chatBotConfig.getAutoMessage() // 자동 답장 옵션이 켜져있는 상태에서
                    && chatBotConfig.isBotName(member) // 봇에게 메세지를 보냈을 때
                    && getUserGrade(session) != MemberGrade.CHATBOT //봇끼리 대화 못하게
                ) {

                ChatDto botChat = new ChatDto(member, chatBotConfig.getRandomBotMessage(), String.valueOf(System.currentTimeMillis()));
                repository.saveChat(botChat, msg.getRoomId());

                SocketMessageForm botChatMessageForm = new SocketMessageForm(
                        msg.getRoomId(), member, HtmlCreator.getChat(botChat, OtherChat), botChat.getTime(), SocketMessageType.WRITE);
                String botMessageStr = objectMapper.writeValueAsString(botChatMessageForm);
                session.sendMessage(new TextMessage(botMessageStr));
            }
        }
    }

    /**
     * 누군가가 대화방을 생성했을 때 수행된다
     * 1. 유효성 검사
     * 1-1. 요청한 아이디의 유저가 실존하는지 확인
     * 1-2. 이미 만들어진 방이 있는지 확인
     * 2. 해당 유저의 room 개설
     * 3. 만들어진 대화방 갱신 요청
     * 3-1. 나에게
     * 3-2. 상대에게
     */
    private void handleTextMessage_ADD_ROOM(WebSocketSession session, SocketMessageForm msg) throws IOException {
        final String otherMember = msg.getMessage();

        //1-1.
        if (false == repository.existsByUserName(otherMember)) {
            return;
        }

        //1-2.
        List<RoomInfoDto> roomListByUserName = repository.getRoomListByUserName(msg.getWriter());
        for (RoomInfoDto roomInfoDto : roomListByUserName) {
            for (String member : roomInfoDto.getMembers()) {
                if (otherMember.equals(member)) {
                    return;
                }
            }
        }

        //2.
        String RoomId = repository.createNewRoom(msg.getWriter()+", "+otherMember +" 대화방",
                new ArrayList<String>(Arrays.asList(msg.getWriter(), otherMember) ) );

        //3.
        RoomInfoDto roomInfo = repository.getRoomInfoByRoomId(RoomId);
        SocketMessageForm roomInfoForm = new SocketMessageForm(
                "", "", HtmlCreator.getRoomByRoomInfo(roomInfo), "", SocketMessageType.CREATE_ROOMLIST);
        String roomRequestStr = objectMapper.writeValueAsString(roomInfoForm);

        //3-1.
        session.sendMessage(new TextMessage(roomRequestStr));
        //3-2.
        for (WebSocketSession webSocketSession : sessions) {
            if (getUserName(webSocketSession).equals(otherMember)) {
                webSocketSession.sendMessage(new TextMessage(roomRequestStr));
            }
        }
      }

    /**
     * 웹 브라우저에서 사용자가 대화방을 변경했을 때 최근 채팅 목록을 띄워 줄 수 있도록 메세지를 만들어 전송한다
     */
    public void ShandleTextMessage_CREATE_CHATLIST(WebSocketSession session, String roomId) throws IOException {
        SocketMessageForm chatListForm = new SocketMessageForm(
                roomId, "", createChatDiv(session, roomId), "", SocketMessageType.CREATE_CHATLIST);
        String chatListRequestStr = objectMapper.writeValueAsString(chatListForm);
        session.sendMessage(new TextMessage(chatListRequestStr));

    }

    private String createRoomDiv(WebSocketSession session) {
        List<RoomInfoDto> roomList = repository.getRoomListByUserName(getUserName(session));
        String divStr = "";
        for (RoomInfoDto roomInfo : roomList) {
            divStr += HtmlCreator.getRoomByRoomInfo(roomInfo);
        }

        return divStr;
    }

    private String createChatDiv(WebSocketSession session, String roomId) {
        ChatLogDto chatLog = repository.getRecentRoomChatLogByRoomId(roomId);
        String divStr = "";
        for (ChatDto chat : chatLog.getChatLogList()) {
            // 내가 쓴 채팅이면
            if (chat.getWriter().equals(getUserName(session))) {
                divStr += HtmlCreator.getChat(chat, true);
            }
            // 상대가 쓴 채팅이면
            else {
                divStr += HtmlCreator.getChat(chat, false);
            }
        }
        return divStr;
    }

    /**
     * 사용자가 채팅웹을 떠나 세션이 종료되었을 때 수행
     * 1. 현재 접속한 세션 리스트에서 삭제
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        synchronized (sessions) {
            sessions.remove(session);
        }
        //log.info("socket disconnect: {}", session);
    }

    private String getUserName(WebSocketSession session) {
        return ((SessionMemberForm) session.getAttributes().get(ChatSessionConst.HTTP_SESSION))
                .getUserName();
    }

    private MemberGrade getUserGrade(WebSocketSession session) {
        return ((SessionMemberForm) session.getAttributes().get(ChatSessionConst.HTTP_SESSION))
                .getGrade();
    }

}
