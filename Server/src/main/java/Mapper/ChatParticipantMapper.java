package Mapper;

import dto.requests.AcceptFriendRequest;
import dto.requests.AddUserToGroupRequest;
import model.entities.ChatParticipant;

public class ChatParticipantMapper {
    public ChatParticipant AddUserToGroupRequestToChatParticipant(AddUserToGroupRequest request) {
        ChatParticipant chatParticipant = new ChatParticipant();
        chatParticipant.setChatId(request.getChatID());
        chatParticipant.setParticipantUserId(request.getUserID());
        return chatParticipant;
    }
}
