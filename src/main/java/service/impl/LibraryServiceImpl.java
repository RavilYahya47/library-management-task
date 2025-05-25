package main.java.service.impl;

import main.java.dao.AuthorDAO;
import main.java.dao.BookDAO;
import main.java.model.Author;
import main.java.model.Book;
import main.java.service.LibraryService;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryServiceImpl implements LibraryService {

    private final AuthorDAO authorDAO;
    private final BookDAO bookDAO;

    // Constructor Dependency Injection
    public LibraryServiceImpl(AuthorDAO authorDAO, BookDAO bookDAO) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
    }

    @Override
    public List<Book> findBooksByAuthor(String authorName) throws SQLException {
        List<Author> authors = authorDAO.findAll();
        Optional<Author> matchedAuthor = authors.stream()
                .filter(author -> author.getName().equalsIgnoreCase(authorName))
                .findFirst();

        if (matchedAuthor.isPresent()) {
            int authorId = matchedAuthor.get().getId();
            return bookDAO.findAll().stream()
                    .filter(book -> book.getAuthorId() == authorId)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Book> findAvailableBooks() throws SQLException {
        return bookDAO.findAll().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public void borrowBook(int bookId) throws SQLException {
        Optional<Book> optionalBook = bookDAO.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (!book.isAvailable()) {
                throw new IllegalStateException("Book is already borrowed.");
            }
            book.setAvailable(false);
            bookDAO.update(book);
        } else {
            throw new NoSuchElementException("Book not found.");
        }
    }

    @Override
    public void returnBook(int bookId) throws SQLException {
        Optional<Book> optionalBook = bookDAO.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (book.isAvailable()) {
                throw new IllegalStateException("Book is already returned.");
            }
            book.setAvailable(true);
            bookDAO.update(book);
        } else {
            throw new NoSuchElementException("Book not found.");
        }
    }

    @Override
    public Map<String, Long> getBookStatisticsByGenre() throws SQLException {
        return bookDAO.findAll().stream()
                .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));
    }
}