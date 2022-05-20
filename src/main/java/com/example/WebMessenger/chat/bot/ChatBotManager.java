package com.example.WebMessenger.chat.bot;



import com.example.WebMessenger.chat.domain.ChatDto;
import com.example.WebMessenger.chat.domain.RoomInfoDto;
import com.example.WebMessenger.chat.domain.member.MemberGrade;
import com.example.WebMessenger.chat.domain.session.SessionMemberForm;
import com.example.WebMessenger.chat.storage.manager.ChatRepositoryManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.*;

@Configuration
@Getter @Setter
public class ChatBotManager {

    @Autowired
    private ChatRepositoryManager repository;

    private static final boolean isAutoMessage = true;
    private List<SessionMemberForm> chatBotList = new ArrayList<>();
    private Vector<String> autoMessageList = new Vector<>();

    @PostConstruct //TODO private
    public void initBotData(){
        chatBotList.add(new SessionMemberForm("ChatBot_1", MemberGrade.CHATBOT));
        chatBotList.add(new SessionMemberForm("ChatBot_2", MemberGrade.CHATBOT));
        chatBotList.add(new SessionMemberForm("ChatBot_3", MemberGrade.CHATBOT));

        autoMessageList.add("반가워요.");
        autoMessageList.add("오늘 날씨가 좋네요.");
        autoMessageList.add("저는 놀이공원을 좋아해요.");
        autoMessageList.add("오늘 하루는 어땠나요?.");
        autoMessageList.add("모든지 잘 될거에요.");
        autoMessageList.add("뭐 먹고싶은거 있어요?");
        autoMessageList.add("식사는 맛있게 하셨나요?");
        autoMessageList.add("삶이 있는 한 희망은 있다  -키케로");
        autoMessageList.add("산다는것 그것은 치열한 전투이다.  -로망로랑");
        autoMessageList.add("하루에 3시간을 걸으면 7년 후에 지구를 한바퀴 돌 수 있다. -사무엘존슨");
        autoMessageList.add("언제나 현재에 집중할수 있다면 행복할것이다. -파울로 코엘료");
        autoMessageList.add("진정으로 웃으려면 고통을 참아야하며 , 나아가 고통을 즐길 줄 알아야 해 -찰리 채플린");
        autoMessageList.add("직업에서 행복을 찾아라. 아니면 행복이 무엇인지 절대 모를 것이다 -엘버트 허버드");
        autoMessageList.add("신은 용기있는자를 결코 버리지 않는다 -켄러");
        autoMessageList.add("행복의 문이 하나 닫히면 다른 문이 열린다 그러나 우리는 종종 닫힌 문을 멍하니 바라보다가");
        autoMessageList.add("우리를 향해 열린 문을 보지 못하게 된다  – 헬렌켈러");
        autoMessageList.add("피할수 없으면 즐겨라 – 로버트 엘리엇");
        autoMessageList.add("단순하게 살아라. 현대인은 쓸데없는 절차와 일 때문에 얼마나 복잡한 삶을 살아가는가?-이드리스 샤흐");
        autoMessageList.add("먼저 자신을 비웃어라. 다른 사람이 당신을 비웃기 전에  – 엘사 맥스웰");
        autoMessageList.add("먼저핀꽃은 먼저진다  남보다 먼저 공을 세우려고 조급히 서둘것이 아니다 – 채근담");
        autoMessageList.add("행복한 삶을 살기위해 필요한 것은 거의 없다. -마르쿠스 아우렐리우스 안토니우스");
        autoMessageList.add("절대 어제를 후회하지 마라 . 인생은 오늘의 나 안에 있고 내일은 스스로 만드는 것이다 L.론허바드");
        autoMessageList.add("어리석은 자는 멀리서 행복을 찾고, 현명한 자는 자신의 발치에서 행복을 키워간다  -제임스 오펜하임");
        autoMessageList.add("너무 소심하고 까다롭게 자신의 행동을 고민하지 말라 . 모든 인생은 실험이다 . 더많이 실험할수록 더나아진다  – 랄프 왈도 에머슨");
        autoMessageList.add("한번의 실패와 영원한 실패를 혼동하지 마라  -F.스콧 핏제랄드");
        autoMessageList.add("내일은 내일의 태양이 뜬다");
        autoMessageList.add("피할수 없으면 즐겨라 -로버트 엘리엇");
        autoMessageList.add("대 어제를 후회하지 마라. 인생은 오늘의  내 안에 있고 내일은 스스로 만드는것이다. -L론허바드");
        autoMessageList.add("계단을 밟아야 계단 위에 올라설수 있다, -터키속담");
        autoMessageList.add("오랫동안 꿈을 그리는 사람은 마침내 그 꿈을 닮아 간다, -앙드레 말로");
        autoMessageList.add("좋은 성과를 얻으려면 한 걸음 한 걸음이 힘차고 충실하지 않으면 안 된다, -단테");
        autoMessageList.add("행복은 습관이다,그것을 몸에 지니라 -허버드");
        autoMessageList.add("성공의 비결은 단 한 가지, 잘할 수 있는 일에 광적으로 집중하는 것이다.- 톰 모나건");

        repository.saveMember("ChatBot_1", MemberGrade.CHATBOT);
        repository.saveMember("ChatBot_2", MemberGrade.CHATBOT);
        repository.saveMember("ChatBot_3", MemberGrade.CHATBOT);
        //repository.saveMember("tester1", MemberGrade.USER);
        //repository.saveMember("tester2", MemberGrade.USER);
        //repository.saveMember("tester3", MemberGrade.USER);

        //repository.createNewRoomDirect("Stress Test 1", Arrays.asList("tester1", "ChatBot_1"), "testRoom1" );
        //repository.createNewRoomDirect("Stress Test 2", Arrays.asList("tester2", "ChatBot_2"), "testRoom2"  );
        //repository.createNewRoomDirect("Stress Test 3", Arrays.asList("tester3", "ChatBot_3"), "testRoom3"  );

        //repository.saveMember("tester1", MemberGrade.USER);
        //repository.saveMember("admin", MemberGrade.ADMIN);
        //repository.createNewRoomDirect("admin, tester1 대화방", Arrays.asList("admin", "tester1"),"testRoom4" );

    }

    public void initDefaultChatBotToUser(String userName, MemberGrade grade) {
        // 해당 세션의 사용자의 방 정보를 보고 비어있다면 새로운 유저이기에 봇과의 방을 만들어 준다.
        List<RoomInfoDto> roomListByUserName = repository.getRoomListByUserName(userName);
        if ( ( roomListByUserName != null && roomListByUserName.size() != 0 )
                || grade == MemberGrade.CHATBOT) {
            return;
        }

        for (SessionMemberForm bot : chatBotList) {
            String roomId = repository.createNewRoom(bot.getUserName() + " 대화방", Arrays.asList(userName, bot.getUserName()));
            ChatDto chat = new ChatDto(bot.getUserName(), "안녕하세요.", String.valueOf(System.currentTimeMillis()));

            repository.saveChat(chat, roomId);
        }
    }

    public boolean getAutoMessage(){
        return isAutoMessage;
    }

    public boolean isBotName(String userName) {
        for (SessionMemberForm bot : chatBotList) {
            if (bot.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public String getRandomBotMessage(){
        int c = new Random().nextInt(autoMessageList.size());
        return autoMessageList.get(c);
    }


}
