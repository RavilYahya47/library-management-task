package main.java.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Book not found!";
    }
}
