package pl.edu.agh.tai.secuirty.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.edu.agh.tai.model.User;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = getUserDetail(username);
        System.out.println(username);
        return user;
    }



    public User getUserDetail(String username) {
        MongoOperations mongoOperation = (MongoOperations) mongoTemplate;
        User user = mongoOperation.findOne(
                new Query(Criteria.where("_id").is(username)),
                User.class);
        System.out.println(user.toString());
        return user;
    }

}