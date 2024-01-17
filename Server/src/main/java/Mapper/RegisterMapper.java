package Mapper;

import dto.requests.RegisterRequest;
import model.entities.User;
import org.mapstruct.Mapper;
@Mapper
public interface RegisterMapper {
    public User requestToEntity(RegisterRequest request);
    public  RegisterRequest entityToRequest(User user);
}
