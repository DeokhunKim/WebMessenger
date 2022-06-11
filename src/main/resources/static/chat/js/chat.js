
<!-- 이벤트 관련 -->

    let currentRoomId = "";

    // 유저 리스트 클릭
    function userListClick(classId) {
    if(currentRoomId == classId){
    return 0;
}

    // chat 전부 삭제 및 갱신 요청
    document.getElementById('messageWindow').innerText = null;
    var json = JSON.stringify({
    roomId:classId, writer:userName, message:'a', time:'a', messageType:'CREATE_CHATLIST'}) ;
    sendMessage(json);
};

    // 유저 리스트 클릭 이벤트 갱신
    var resetFunction = function (){
    var classname = document.getElementsByClassName("list-group-item list-group-item-action border-0");
    for (var i = 0; i < classname.length; i++) {
    var user = document.getElementById(classname[i].id);
    user.onclick = function (event){
    userListClick(event.currentTarget.id);
}
}
};

    // Add 버튼
    $("#addBtn").on("click", function() {
        var inputMessageBox = document.getElementById("inputMember");
        let msg = inputMessageBox.value;
        var json = JSON.stringify({
            roomId:'', writer:userName, message:msg, time:new Date().getTime(),messageType:'ADD_ROOM'}) ;
        sendMessage(json);
        inputMessageBox.value = null;
});

    // Send 버튼
    $("#sendBtn").on("click", function() {
    sendMessageBoxEvent();
});

    // 메세지 보내기
    function sendMessageBoxEvent(){
    var inputMessageBox = document.getElementById("inputMessage");
    let msg = inputMessageBox.value;
    var json = JSON.stringify({
    roomId:currentRoomId, writer:userName, message:msg, time:new Date().getTime(),messageType:'WRITE'}) ;
    sendMessage(json);
    inputMessageBox.value = null;
};



<!-- 소켓 핸들러 관련 -->

    var sock = new SockJS('http://localhost:8003/Chat');
    sock.onmessage = onMessage;
    sock.onclose = onClose;
    sock.onopen = onOpen;
    var userName = '[[${userName}]]';


    function sendMessage(str) {
    sock.send(str);
}
    //서버에서 메시지를 받았을 때
    function onMessage(msg) {
    var obj = JSON.parse(msg.data);

    if (obj.messageType == 'CREATE_ROOMLIST') {
    addRoomListByStr(obj.message);
    resetFunction();
}
    else if (obj.messageType == 'CREATE_CHATLIST') {
    addChatByStr(obj.message);
    var classname = document.getElementsByClassName("list-group-item list-group-item-action border-0");
    currentRoomId = obj.roomId;
}
    else if (obj.messageType == 'WRITE') {
    if (obj.roomId == currentRoomId) {
    addChatByStr(obj.message);
}
}
}
    //채팅앱에 나갔을때
    function onClose(evt) {
}
    //채팅앱에 접속 했을때
    function onOpen(evt) {
    var json = JSON.stringify({
    roomId:currentRoomId, writer:'', message:'', time:new Date().getTime(),messageType:'CONNECT'}) ;
    sendMessage(json);
}




<!-- append 관련 -->


    function addRoomListByStr(str) {
    $("#roomListWindow").append(str);
}

    function addChatByStr(str) {
    $("#messageWindow").prepend(str);
}
