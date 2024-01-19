package Mapper;

import dto.requests.LoginRequest;
import model.entities.User;
import org.mapstruct.Mapper;


@Mapper
public interface LoginMapper {
    public User requestToEntity(LoginRequest request);
    public LoginRequest entityToRequest(User user);
}
