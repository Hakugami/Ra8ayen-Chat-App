package Mapper;

import dto.requests.UpdateUserRequest;
import model.entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface UpdateUser {

        public User updateUserRequestToEntity(UpdateUserRequest updateUserRequest);
        public UpdateUserRequest entityToUpdateUserRequest(User user);
}
