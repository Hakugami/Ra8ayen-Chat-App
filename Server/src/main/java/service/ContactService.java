package service;

import Mapper.UserContactMapper;
import dao.UserContactsDao;
import dao.UserDao;
import dao.impl.UserContactsDaoImpl;
import dao.impl.UserDaoImpl;
import dto.Controller.ContactsController;
import dto.requests.DeleteUserContactRequest;
import dto.requests.GetContactsRequest;
import dto.responses.DeleteUserContactResponse;
import dto.responses.GetContactsResponse;
import model.entities.User;
import model.entities.UserContacts;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactService implements ContactsController {
    @Override
    public List<GetContactsResponse> getContacts(GetContactsRequest getContactsRequest) throws RemoteException, SQLException, ClassNotFoundException {

        UserContactMapper userContactMapper = new UserContactMapper();
        User userContacts = userContactMapper.UserContactFromRequestGet(getContactsRequest);

        UserContactsDao userContactsDao = new UserContactsDaoImpl();
        UserDao userDao = new UserDaoImpl();

        List<UserContacts> listOfUserContact=userContactsDao.getContactById(userContacts);

        List<GetContactsResponse> listOfUserContactResponse = new ArrayList<>();
        for(UserContacts userContacts1:listOfUserContact){
            User testUser = userDao.get(userContacts1.getFriendID());
            if(testUser!=null){
                listOfUserContactResponse.add(userContactMapper.ContactsResponseFromUserContact(userContacts1,testUser));
            }
        }
        return listOfUserContactResponse;
    }

    @Override
    public DeleteUserContactResponse deleteContact(DeleteUserContactRequest deleteUserContactRequest) throws RemoteException, SQLException, ClassNotFoundException {
        UserContactMapper userContactMapper = new UserContactMapper();

        UserContacts userContacts = userContactMapper.UserContactFromRequestDelete(deleteUserContactRequest);

        UserContactsDao userContactsDao = new UserContactsDaoImpl();

        userContactsDao.delete(userContacts);

        return new DeleteUserContactResponse();

    }
}
