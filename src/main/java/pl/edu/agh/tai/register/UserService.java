package pl.edu.agh.tai.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.tai.model.User;
import pl.edu.agh.tai.register.IUserService;
import pl.edu.agh.tai.register.UserDTO;
import pl.edu.agh.tai.utils.exceptions.EmailExistsException;

@Service
public class UserService implements IUserService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Transactional
    @Override
    public User registerNewUserAccount(UserDTO accountDto) throws EmailExistsException {
        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException("There is an account with that email address:"
                    + accountDto.getEmail());
        }
        User user = new User();
        user.setUsername(accountDto.getUsername());
        user.setId(accountDto.getUsername());
        user.setFirstName(accountDto.getFirstname());
        user.setLastName(accountDto.getLastname()   );
        user.setPassword(accountDto.getPassword());
        user.setEmail(accountDto.getEmail());
        user.setSignInProvider(accountDto.getSignInProvider());
        user.setRole(2);
        System.out.println("Save user DTO:" + accountDto);
        mongoTemplate.save(user);
        return user;
    }

    private boolean emailExist(String email) {
        User user = getUserDetail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public User getUserDetail(String email) {
        MongoOperations mongoOperation = (MongoOperations) mongoTemplate;
        User user = mongoOperation.findOne(
                new Query(Criteria.where("email").is(email)),
                User.class);
        return user;
    }
}