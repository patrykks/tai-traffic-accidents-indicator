package pl.edu.agh.tai.secuirty.exceptions;


public class EmailExistsException extends Throwable {

    public EmailExistsException(final String message) {
        super(message);
    }

}