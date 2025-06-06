package main.java.exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Book not available!";
    }
}
