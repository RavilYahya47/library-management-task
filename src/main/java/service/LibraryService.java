package main.java.service;

import main.java.DAO.AuthorDAO;
import main.java.DAO.BookDAO;
import main.java.model.Book;
import main.java.exception.BookNotFoundException;
import main.java.exception.BookNotAvailableException;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LibraryService {
    private AuthorDAO authorDAO;
    private BookDAO bookDAO;

    public LibraryService(AuthorDAO authorDAO, BookDAO bookDAO) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
    }

    public List<Book> findBooksByAuthor(String authorName) throws SQLException {
        return bookDAO.findByAuthorName(authorName);
    }

    public List<Book> findAvailableBooks() throws SQLException {
        return bookDAO.findAvailableBooks();
    }

    public void borrowBook(int bookId) throws SQLException, BookNotFoundException, BookNotAvailableException {
        Optional<Book> optionalBook = bookDAO.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Kitab tapılmadı");
        }
        Book book = optionalBook.get();
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Kitab artıq icarədədir");
        }
        book.setAvailable(false);
        bookDAO.update(book);
    }

    public void returnBook(int bookId) throws SQLException, BookNotFoundException {
        Optional<Book> optionalBook = bookDAO.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Kitab tapılmadı");
        }
        Book book = optionalBook.get();
        if (book.isAvailable()) {
            throw new BookNotFoundException("Kitab icarədə deyil");
        }
        book.setAvailable(true);
        bookDAO.update(book);
    }

    public Map<String, Long> getBookStatisticsByGenre() throws SQLException {
        List<Book> books = bookDAO.findAll();
        return books.stream()
                .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));
    }

    public List<Book> findBooksBy(Predicate<Book> condition) throws SQLException {
        return bookDAO.findAll().stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public Optional<Book> findLongestBook() throws SQLException {
        return bookDAO.findAll()
                .stream()
                .max(Comparator.comparing(Book::getPages));
    }

    public List<Book> findBooksAfterYear(int year) throws SQLException {
        return bookDAO.findAll()
                .stream()
                .filter(book -> book.getPublicationYear() != null && book.getPublicationYear() > year)
                .collect(Collectors.toList());
    }

    public List<Book> filterBooksAfterYear(int year) throws SQLException {
        return bookDAO.findAll().stream()
                .filter(book -> book.getPublicationYear() != null && book.getPublicationYear() > year)
                .collect(Collectors.toList());
    }

    public Optional<Book> findBookWithMostPages() throws SQLException {
        return bookDAO.findAll().stream()
                .max(Comparator.comparingInt(Book::getPages));
    }

    public Map<String, List<Book>> groupBooksByGenre() throws SQLException {
        return bookDAO.findAll().stream()
                .collect(Collectors.groupingBy(Book::getGenre));
    }

    public List<Book> findBooksByGenre(String genre) throws SQLException {
        return bookDAO.findByGenre(genre);
    }
}