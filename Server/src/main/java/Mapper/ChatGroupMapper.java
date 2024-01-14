package Mapper;

import dao.ChatGroupDao;
import dao.UserDao;
import dao.impl.ChatGroupDaoImpl;
import dao.impl.UserDaoImpl;
import model.entities.ChatGroup;

public class ChatGroupMapper {
    private ChatGroupDao chatGroupDao;
    private UserDaoImpl userDao;

    public ChatGroupMapper(){
        chatGroupDao = new ChatGroupDaoImpl();
        userDao = new UserDaoImpl();
    }

    public ChatGroup addChatGroup(String groupName, String phoneNumber){
        ChatGroup chatGroup = new ChatGroup(groupName, userDao.getUserByPhoneNumber(phoneNumber).getUserID());
        chatGroupDao.save(chatGroup);
        return chatGroup;
    }

}
