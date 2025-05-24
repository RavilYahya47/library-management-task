package main.java.exceptions;

public class BookNotFoundException extends Exception{
    public BookNotFoundException(){
        super("Book not found");
    }
}
