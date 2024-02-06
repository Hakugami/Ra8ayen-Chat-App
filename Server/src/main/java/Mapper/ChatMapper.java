package Mapper;

import dto.requests.AcceptFriendRequest;
import dto.requests.CreateGroupChatRequest;
import dto.responses.GetContactChatResponse;
import dto.responses.GetGroupResponse;
import model.entities.Chat;

public class ChatMapper {
    public GetGroupResponse chatToGroupResponse(Chat chat){
        GetGroupResponse getGroupResponse = new GetGroupResponse();
        getGroupResponse.setGroupId(chat.getChatId());
        getGroupResponse.setGroupName(chat.getName());
        getGroupResponse.setGroupAdminId(chat.getAdminId());
        getGroupResponse.setGroupPicture(chat.getChatImage());
        return getGroupResponse;
    }

    public GetContactChatResponse chatToGetContactChatResponse(Chat chat) {
        GetContactChatResponse getContactChatResponse = new GetContactChatResponse();
        getContactChatResponse.setChatID(chat.getChatId());
        getContactChatResponse.setChatImage(null);
        getContactChatResponse.setChatName(null);
        return getContactChatResponse;
    }

    public Chat createGroupChatRequestToChat(CreateGroupChatRequest createGroupChatRequest) {
        Chat chat = new Chat();
        chat.setName(createGroupChatRequest.getGroupName());
        chat.setAdminId(createGroupChatRequest.getAdminID());
        chat.setChatImage(createGroupChatRequest.getGroupImage());
        return chat;
    }
    public Chat chatFromAcceptFriendRequest(AcceptFriendRequest acceptFriendRequest){
        Chat chat = new Chat();
        chat.setName("");
        return chat;
    }
}
