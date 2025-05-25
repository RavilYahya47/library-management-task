package main.java.service;

import main.java.dao.AuthorDao;
import main.java.dao.BookDao;
import main.java.exception.BookNotAvailableException;
import main.java.exception.BookNotFoundException;
import main.java.model.Author;
import main.java.model.Book;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LibraryService {
    private final AuthorDao authorDAO;
    private final BookDao bookDAO;

    public LibraryService(AuthorDao authorDAO, BookDao bookDAO) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
    }

    // Verilmis muellif adina gore kitablari tapmaq
    public List<Book> findBooksByAuthor(String authorName) throws SQLException {
        // Butun muellifleri getiririk
        List<Author> allAuthors = authorDAO.findAll();

        // Ada gore muellif tapmaq
        Optional<Author> foundAuthor = allAuthors.stream()
                .filter(author -> author.getName().equalsIgnoreCase(authorName))
                .findFirst();

        // Eger muellif varsa, onun id-si ile kitablari tapiriq
        if (foundAuthor.isPresent()) {
            int authorId = foundAuthor.get().getId();

            return bookDAO.findAll().stream()
                    .filter(book -> book.getAuthorId() == authorId)
                    .collect(Collectors.toList());
        }

        // Eger muellif tapilmasa, bos siyahi qaytaririq
        return Collections.emptyList();
    }

    // Movcud kitablar
    public List<Book> findAvailableBooks() throws SQLException {
        return bookDAO.findAll().stream()
                .filter(Book::isAvailable) // .filter(Book::isAvailable) də işləyər
                .collect(Collectors.toList());
    }

    // Kitab icareye goturulur
    public void borrowBook(int bookId) throws SQLException {
        Book book = bookDAO.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Kitab tapılmadı"));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Kitab mövcud deyil");
        }

        book.setAvailable(false);
        bookDAO.update(book);
    }

    // Kitab geri qaytarılır
    public void returnBook(int bookId) throws SQLException {
        Book book = bookDAO.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Kitab tapilmadi!"));

        book.setAvailable(true);
        bookDAO.update(book);
    }

    // Janr uzre kitab sayini qaytarmaq
    public Map<String, Long> getBookStatistics() throws SQLException {
        List<Book> allBooks = bookDAO.findAll();

        // Janra gore qruplasdirib sayini hesablamaq
        Stream<Book> bookStream = allBooks.stream();

        Map<String, Long> genreCount = bookStream.collect(
                Collectors.groupingBy(
                        Book::getGenre,
                        Collectors.counting()
                )
        );

        return genreCount;

    }
}
