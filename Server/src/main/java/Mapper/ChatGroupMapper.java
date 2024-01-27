package Mapper;

import dto.requests.CreateGroupChatRequest;
import dto.responses.CreateGroupChatResponse;
import dto.responses.GetGroupResponse;
import model.entities.Chat;

public class ChatGroupMapper {
    public GetGroupResponse chatToGroupResponse(Chat chat){
        GetGroupResponse getGroupResponse = new GetGroupResponse();
        getGroupResponse.setGroupId(chat.getChatId());
        getGroupResponse.setGroupName(chat.getName());
        getGroupResponse.setGroupAdminId(chat.getAdminId());
        getGroupResponse.setGroupPicture(chat.getChatImage());
        return getGroupResponse;
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
}
