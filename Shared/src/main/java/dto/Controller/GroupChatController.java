package dto.Controller;

import dto.requests.AddUserToGroupRequest;
import dto.requests.CreateGroupChatRequest;
import dto.requests.GetGroupRequest;
import dto.responses.AddUserToGroupResponse;
import dto.responses.CreateGroupChatResponse;
import dto.responses.GetGroupResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface GroupChatController extends Remote {
    List<GetGroupResponse> getGroups(GetGroupRequest request) throws RemoteException, SQLException, ClassNotFoundException;
    CreateGroupChatResponse createGroupChat(CreateGroupChatRequest request) throws RemoteException, SQLException, ClassNotFoundException;
    AddUserToGroupResponse addUserToGroupChat(AddUserToGroupRequest request) throws RemoteException, SQLException, ClassNotFoundException;

}
