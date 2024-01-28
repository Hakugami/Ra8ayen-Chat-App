package Mapper;

import dto.requests.AcceptFriendRequest;
import dto.requests.CreateGroupChatRequest;
import dto.responses.CreateGroupChatResponse;
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

    public CreateGroupChatResponse chatToCreateGroupChatResponse(Chat chat){
        CreateGroupChatResponse createGroupChatResponse = new CreateGroupChatResponse();
        createGroupChatResponse.setGroupId(chat.getChatId());
        createGroupChatResponse.setGroupName(chat.getName());
        createGroupChatResponse.setGroupAdminId(chat.getAdminId());
        createGroupChatResponse.setGroupPicture(chat.getChatImage());
        return createGroupChatResponse;
    }

    public Chat chatFromAcceptFriendRequest(AcceptFriendRequest acceptFriendRequest){
        Chat chat = new Chat();
        chat.setName(acceptFriendRequest.getUserModel().getUserName());
        return chat;
    }
}
