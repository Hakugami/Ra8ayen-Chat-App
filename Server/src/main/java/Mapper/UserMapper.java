package Mapper;

import dao.impl.UserDaoImpl;
import dto.Model.UserModel;
import model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface UserMapper {
    public User modelToEntity(UserModel user);
    public UserModel entityToModel(User user);
    default UserModel phoneToModel(String phoneNumber) {
        UserDaoImpl userDao = new UserDaoImpl();
        return entityToModel(userDao.getUserByPhoneNumber(phoneNumber));
    }
    @Named("idToModel")
    default  UserModel idToModel(int id) {
        UserDaoImpl userDao = new UserDaoImpl();
        return entityToModel(userDao.get(id));
    }
}
