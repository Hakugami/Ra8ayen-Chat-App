package Mapper;

import model.entities.Notification;

public class InvitationMapper {
    public Notification addContactRequestToNotification(int senderID, int receiverID) {
        Notification notification = new Notification();
        notification.setSenderId(senderID);
        notification.setReceiverId(receiverID);
        return notification;
    }
}
