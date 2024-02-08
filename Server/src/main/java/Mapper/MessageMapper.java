package Mapper;

import dto.Model.MessageModel;
import dto.requests.GetMessageRequest;
import dto.requests.SendMessageRequest;
import model.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class})
public interface MessageMapper {


    Message sendRequestToEntity(SendMessageRequest request);
    SendMessageRequest entityToSendRequest(Message message);
    @Mapping(source = "chatId", target = "receiverId")
    Message getMessageRequestToEntity(GetMessageRequest request);
    GetMessageRequest entityToGetMessageRequest(Message message);
    Message modelToEntity(MessageModel message);
    @Mapping(source = "senderId", target = "sender", qualifiedByName = "idToModel")
    @Mapping(source = "receiverId", target = "receiver", qualifiedByName = "idToModel")
    MessageModel entityToModel(Message message);
}
