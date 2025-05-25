package main.java.service;

import main.java.dao.AuthorDAO;
import main.java.dao.BookDAO;
import main.java.dao.impl.AuthorDAOImpl;
import main.java.dao.impl.BookDAOImpl;
import main.java.model.Book;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LibraryService {
    private AuthorDAO authorDAO;
    private BookDAO bookDAO;

    public LibraryService() {
        this.authorDAO = new AuthorDAOImpl();
        this.bookDAO = new BookDAOImpl();
    }

    public AuthorDAO getAuthorDAO() {
        return authorDAO;
    }

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    public List<Book> findBooksByAuthor(String authorName) throws SQLException {
        return authorDAO.findAll().stream()
                .filter(author -> author.getName().equalsIgnoreCase(authorName))
                .findFirst()
                .map(author -> {
                    try {
                        return bookDAO.findAll().stream()
                                .filter(book -> book.getAuthorId() == author.getId())
                                .toList();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        return Collections.<Book>emptyList();
                    }
                })
                .orElse(Collections.emptyList());
    }

    public List<Book> findAvailableBooks() throws SQLException {
        List<Book> availableBooks =
                bookDAO.findAll()
                .stream()
                .filter(Book::isAvailable)
                .toList();
        return availableBooks;
    }

    public void borrowBook(int bookId) throws SQLException {
        Optional<Book> book = bookDAO.findById(bookId);
        if(book.isEmpty()){
            System.out.println("Book not found!");
        } else{
            Book borrowedBook = book.get();
            if(!borrowedBook.isAvailable()){
                System.out.println("Book not available!");
                return;
            }
            borrowedBook.setAvailable(false);
            bookDAO.update(borrowedBook);
            System.out.println("Book borrowed successfully!");
        }
    }

    public void returnBook(int bookId) throws SQLException {
        Optional<Book> book = bookDAO.findById(bookId);
        if (book.isEmpty()) {
            System.out.println("Book not found.");
        } else {
            Book returnedBook = book.get();
            if (!returnedBook.isAvailable()) {
                returnedBook.setAvailable(true);
                bookDAO.update(returnedBook);
                System.out.println("Book returned successfully.");
            } else {
                System.out.println("Book was not borrowed. Cannot be returned!");
            }
        }
    }

    public Map<String, Long> getBookStatisticsByGenre() throws SQLException {
        Map<String, Long> booksByGenre = bookDAO.findAll()
                .stream()
                .collect(Collectors.groupingBy(book -> book.getGenre(), Collectors.counting()));
        return booksByGenre;
    }
}