package pl.edu.agh.tai.mongo.spring.social;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import pl.edu.agh.tai.mongo.spring.security.User;

public final class SimpleConnectionSignUp implements ConnectionSignUp {
    private MongoTemplate template;

    public SimpleConnectionSignUp(MongoTemplate template) {
        this.template = template;
    }

    public String execute(Connection<?> connection) {
        UserProfile profile = connection.fetchUserProfile();

        User user = new User(
                connection.createData().getProviderUserId(),
                profile.getUsername(),
                "SocialPassword",
                profile.getFirstName(),
                profile.getLastName()
                , SignInProvider.TWITTER);
        user.setRole(2);
        template.save(user);
        return user.getUserId();
    }

}
