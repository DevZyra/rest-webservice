package pl.devzyra.restwebservice.exceptions;

public class UserServiceException extends RuntimeException {
    private static final long serialVersionUID = 5830736345590172388L;

    public UserServiceException(String message) {
        super(message);
    }
}
