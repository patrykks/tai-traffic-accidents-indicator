package pl.edu.agh.tai.utils.exceptions;


public class EmailExistsException extends Throwable {

    public EmailExistsException(final String message) {
        super(message);
    }

}