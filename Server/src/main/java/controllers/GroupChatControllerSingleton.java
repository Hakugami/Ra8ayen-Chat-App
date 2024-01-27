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
    private GroupChatControllerSingleton() throws RemoteException {
        super();
        chatMapper = new ChatMapper();
        chatParticipantMapper = new ChatParticipantMapper();
        groupService = new GroupService();
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
        CreateGroupChatResponse createGroupChatResponse = chatMapper.chatToCreateGroupChatResponse(chat);
        boolean isCreated = groupService.createGroup(chat);
        createGroupChatResponse.setCreated(isCreated);
        createGroupChatResponse.setErrorMessage("");
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