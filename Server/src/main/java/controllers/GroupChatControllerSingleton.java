package controllers;

import Mapper.ChatMapper;
import Mapper.ChatParticipantMapper;
import dto.Controller.GroupChatController;
import dto.requests.AddUserToGroupRequest;
import dto.requests.CreateGroupChatRequest;
import dto.requests.GetGroupRequest;
import dto.responses.AddUserToGroupResponse;
import dto.responses.CreateGroupChatResponse;
import dto.responses.GetGroupResponse;
import model.entities.Chat;
import model.entities.ChatParticipant;
import service.ContactService;
import service.GroupService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GroupChatControllerSingleton extends UnicastRemoteObject implements GroupChatController {
    private static GroupChatControllerSingleton instance;
    private final ChatMapper chatMapper;
    private final ChatParticipantMapper chatParticipantMapper;
    private final GroupService groupService;
    private final ContactService contactService;
    private GroupChatControllerSingleton() throws RemoteException {
        super();
        chatMapper = new ChatMapper();
        chatParticipantMapper = new ChatParticipantMapper();
        groupService = new GroupService();
        contactService = new ContactService();
    }

    public static GroupChatControllerSingleton getInstance() throws RemoteException {
        if (instance == null) {
            instance = new GroupChatControllerSingleton();
        }
        return instance;
    }

    @Override
    public List<GetGroupResponse> getGroups(GetGroupRequest request) throws RemoteException {
        List<Chat> groupsEntities = groupService.getUserGroups(request.getUserId());
        List<GetGroupResponse> groups = new ArrayList<>();
        for (Chat groupEntity : groupsEntities) {
            groups.add(chatMapper.chatToGroupResponse(groupEntity));
        }
        return groups;
    }

    @Override
    public CreateGroupChatResponse createGroupChat(CreateGroupChatRequest request) throws RemoteException {
        Chat chat = chatMapper.createGroupChatRequestToChat(request);
        List<Integer> friends = contactService.getFriends(request.getFriendsPhoneNumbers(), request.getAdminID());
        CreateGroupChatResponse createGroupChatResponse = new CreateGroupChatResponse();
        int chatId = groupService.createGroup(chat);
        boolean isCreated = chatId != -1;
        if (chatId != -1) {
            for (Integer friend : friends) {
                ChatParticipant chatParticipant = new ChatParticipant();
                chatParticipant.setChatId(chatId);
                chatParticipant.setParticipantUserId(friend);
                groupService.addUserToGroup(chatParticipant);
            }
        }
        createGroupChatResponse.setCreated(isCreated);
        createGroupChatResponse.setResponses(request.getFriendsPhoneNumbers());
        return createGroupChatResponse;
    }

    @Override
    public AddUserToGroupResponse addUserToGroupChat(AddUserToGroupRequest request) throws RemoteException {
        ChatParticipant chatParticipant = chatParticipantMapper.AddUserToGroupRequestToChatParticipant(request);
        AddUserToGroupResponse addUserToGroupResponse = new AddUserToGroupResponse();
        addUserToGroupResponse.setAdded(groupService.addUserToGroup(chatParticipant));
        return addUserToGroupResponse;
    }
}