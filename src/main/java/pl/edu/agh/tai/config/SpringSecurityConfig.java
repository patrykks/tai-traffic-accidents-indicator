package pl.edu.agh.tai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;
import pl.edu.agh.tai.secuirty.mongo.CustomUserDetailsService;
import pl.edu.agh.tai.social.SimpleSocialUserDetailsService;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
@Import({CustomUserDetailsService.class})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    public SpringSecurityConfig() {
        super();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/map/accidents/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/register").permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/index.html")
                .and()
                .formLogin()
                .loginPage("/login.html")
                .failureUrl("/login-error.html")
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403.html")
                .and()
                .apply(new SpringSocialConfigurer()
                        .postLoginUrl("/")
                        .alwaysUsePostLoginUrl(true));
    }

    @Autowired
    public void configAuthBuilder(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SocialUserDetailsService socialUserDetailsService() {
        return new SimpleSocialUserDetailsService(userDetailsService);
    }
}