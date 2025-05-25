package main.java.exceptions;

public class BookNotAvailableException extends Exception{
    public BookNotAvailableException(){
        super("Book is not available");
    }
}
