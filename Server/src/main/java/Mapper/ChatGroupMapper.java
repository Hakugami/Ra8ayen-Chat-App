package Mapper;

import dao.ChatDao;
import dao.impl.ChatDaoImpl;
import dao.impl.UserDaoImpl;
import model.entities.Chat;

public class ChatGroupMapper {
    private ChatDao chatDao;
    private UserDaoImpl userDao;

    public ChatGroupMapper(){
        chatDao = new ChatDaoImpl();
        userDao = new UserDaoImpl();
    }

    public Chat addChatGroup(String groupName, String phoneNumber){
        Chat chat = new Chat(groupName, userDao.getUserByPhoneNumber(phoneNumber).getUserID());
        chatDao.save(chat);
        return chat;
    }

}
