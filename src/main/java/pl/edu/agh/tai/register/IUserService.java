package pl.edu.agh.tai.register;

import pl.edu.agh.tai.model.User;
import pl.edu.agh.tai.register.UserDTO;
import pl.edu.agh.tai.utils.exceptions.EmailExistsException;

public interface IUserService {
    User registerNewUserAccount(UserDTO accountDto)
            throws EmailExistsException;
}
