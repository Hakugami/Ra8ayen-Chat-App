package Mapper;

import dto.Model.MessageModel;
import dto.requests.GetMessageRequest;
import dto.requests.SendMessageRequest;
import model.entities.Message;
import org.mapstruct.Mapper;
@Mapper
public interface MessageMapper {
    Message sendRequestToEntity(SendMessageRequest request);
    SendMessageRequest entityToSendRequest(Message message);
    Message getMessageRequestToEntity(GetMessageRequest request);
    GetMessageRequest entityToGetMessageRequest(Message message);
    Message modelToEntity(MessageModel message);
}
