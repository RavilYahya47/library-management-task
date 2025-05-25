package main.java.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Author not found!";
    }
}
