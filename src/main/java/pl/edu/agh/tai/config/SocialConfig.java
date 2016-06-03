package pl.edu.agh.tai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.edu.agh.tai.mongo.spring.social.MongoConnectionTransformers;
import pl.edu.agh.tai.mongo.spring.social.MongoUsersConnectionRepository;
import pl.edu.agh.tai.mongo.spring.social.SimpleConnectionSignUp;

import java.util.List;
import java.util.Set;

@EnableSocial
public class SocialConfig implements SocialConfigurer{
    @Autowired
    MongoOperations mongo;

    @Autowired
    MongoTemplate template;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(new TwitterConnectionFactory(
                "jclGnViab6SyfJhBSNbjvVuND",
                "deC6X31o9bujwPmu21sChsezu3bN7hCTYIkHFzd6Tia7o8NhQ5"
        ));
        cfConfig.addConnectionFactory(new FacebookConnectionFactory(
                "821997981265909",
                "ab299023994d4c5cb499616366ba43af"
        ));
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        MongoUsersConnectionRepository mongoUsersConnectionRepository = new MongoUsersConnectionRepository(mongo, connectionFactoryLocator,
                new MongoConnectionTransformers(connectionFactoryLocator, Encryptors.noOpText()));
        mongoUsersConnectionRepository.setConnectionSignUp(new SimpleConnectionSignUp(template));
        return mongoUsersConnectionRepository;
    }
}