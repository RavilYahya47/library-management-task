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

    // Butun kitablari gormek
    public List<Book> findAll() {
        try {
            return bookDAO.findAll();
        } catch (SQLException e) {
            System.err.println("findAll xetasi: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Verilmis muellif adina gore kitablari tapmaq
    public List<Book> findBooksByAuthor(String authorName) {
        try {
            List<Author> allAuthors = authorDAO.findAll();

            Optional<Author> foundAuthor = allAuthors.stream()
                    .filter(author -> author.getName().equalsIgnoreCase(authorName))
                    .findFirst();

            if (foundAuthor.isPresent()) {
                int authorId = foundAuthor.get().getId();

                return bookDAO.findAll().stream()
                        .filter(book -> book.getAuthorId() == authorId)
                        .collect(Collectors.toList());
            }
        } catch (SQLException e) {
            System.err.println("findBooksByAuthor xetasi: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    // Movcud kitablar
    public List<Book> findAvailableBooks() {
        try {
            return bookDAO.findAll().stream()
                    .filter(Book::isAvailable)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            System.err.println("findAvailableBooks xetasi: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Kitab icareye goturulur
    public void borrowBook(int bookId) {
        try {
            Book book = bookDAO.findById(bookId)
                    .orElseThrow(() -> new BookNotFoundException("Kitab tapılmadı"));

            if (!book.isAvailable()) {
                throw new BookNotAvailableException("Kitab mövcud deyil");
            }

            book.setAvailable(false);
            bookDAO.update(book);

        } catch (SQLException e) {
            System.err.println("borrowBook xetasi: " + e.getMessage());
        }
    }

    // Kitab geri qaytarılır
    public void returnBook(int bookId) {
        try {
            Book book = bookDAO.findById(bookId)
                    .orElseThrow(() -> new BookNotFoundException("Kitab tapilmadi!"));

            book.setAvailable(true);
            bookDAO.update(book);

        } catch (SQLException e) {
            System.err.println("returnBook xetasi: " + e.getMessage());
        }
    }

    // Janr uzre kitab sayini qaytarmaq
    public Map<String, Long> getBookStatisticsByGenre() {
        try {
            List<Book> allBooks = bookDAO.findAll();

            Stream<Book> bookStream = allBooks.stream();

            return bookStream.collect(
                    Collectors.groupingBy(
                            Book::getGenre,
                            Collectors.counting()
                    )
            );
        } catch (SQLException e) {
            System.err.println("getBookStatisticsByGenre xetasi: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    // En uzun kitabi tapmaq
    public Optional<Book> findLongestBook() {
        try {
            return bookDAO.findAll().stream()
                    .max(Comparator.comparing(Book::getPages));
        } catch (SQLException e) {
            System.err.println("findLongestBook xetasi: " + e.getMessage());
            return Optional.empty();
        }
    }

    //Mueyyen ilden sonra cap olunan kitablar
    public List<Book> findBooksPublishedAfter(int year) {
        List<Book> books = new ArrayList<>();

        try {
            List<Book> allBooks = bookDAO.findAll();

            books = allBooks.stream()
                    .filter(book -> book.getPublicationYear() != null && book.getPublicationYear() > year)
                    .collect(Collectors.toList());

        } catch (SQLException e) {
            System.err.println("Xəta baş verdi: " + e.getMessage());
        }
        return books;
    }

}
