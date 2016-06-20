package pl.edu.agh.tai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import pl.edu.agh.tai.social.mongo.MongoConnectionTransformers;
import pl.edu.agh.tai.social.mongo.MongoUsersConnectionRepository;
import pl.edu.agh.tai.social.SimpleConnectionSignUp;

@EnableSocial
public class SpringSocialConfig implements SocialConfigurer{
    @Autowired
    MongoOperations mongo;

    @Autowired
    MongoTemplate template;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(new TwitterConnectionFactory(
                env.getProperty("twitter.consumerKey"),
                env.getProperty("twitter.consumerSecret")
        ));
        cfConfig.addConnectionFactory(new FacebookConnectionFactory(
                env.getProperty("facebook.appId"),
                env.getProperty("facebook.appSecret")
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