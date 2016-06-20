package pl.edu.agh.tai.secuirty.register;

import org.hibernate.validator.constraints.NotEmpty;
import pl.edu.agh.tai.model.enums.SignInProvider;
import pl.edu.agh.tai.secuirty.register.validators.PasswordMatches;
import pl.edu.agh.tai.secuirty.register.validators.ValidEmail;

import javax.validation.constraints.NotNull;

@PasswordMatches
public class UserDTO {

    @NotNull
    @NotEmpty( message = "Must not be empty" )
    private String username;

    @NotNull
    @NotEmpty( message = "Must not be empty" )
    private String password;

    @NotNull
    @NotEmpty( message = "Must not be empty" )
    private String matchingPassword;

    private String firstname;

    private String lastname;

    @ValidEmail
    @NotNull
    @NotEmpty( message = "Must not be empty" )
    private String email;

    private SignInProvider signInProvider;

    public UserDTO(String id, String username, String password, String firstName, String lastname, SignInProvider signInProvider, String matchingPassword, String email) {
        this.username = username;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.firstname = firstName;
        this.lastname = lastname;
        this.email = email;
        signInProvider = SignInProvider.TAI;
    }

    public UserDTO() {
        signInProvider = signInProvider.TAI;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }


    public String getFirstname() {
        return firstname;
    }


    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }


    public String getLastname() {
        return lastname;
    }


    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public SignInProvider getSignInProvider() {
        return signInProvider;
    }


    public void setSignInProvider(SignInProvider signInProvider) {
        this.signInProvider = signInProvider;
    }


    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", matchingPassword='" + matchingPassword + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", signInProvider=" + signInProvider +
                '}';
    }
}
