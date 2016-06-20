package pl.edu.agh.tai.secuirty.register;

import pl.edu.agh.tai.model.User;
import pl.edu.agh.tai.secuirty.exceptions.EmailExistsException;

public interface IUserService {
    User registerNewUserAccount(UserDTO accountDto)
            throws EmailExistsException;
}
