package dto.Controller;

import dto.requests.AddUserToGroupRequest;
import dto.requests.CreateGroupChatRequest;
import dto.responses.AddUserToGroupResponse;
import dto.responses.CreateGroupChatResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface GroupChatController extends Remote {
    CreateGroupChatResponse createGroupChat(CreateGroupChatRequest request) throws RemoteException, SQLException, ClassNotFoundException;
    AddUserToGroupResponse addUserToGroupChat(AddUserToGroupRequest request) throws RemoteException, SQLException, ClassNotFoundException;

}
