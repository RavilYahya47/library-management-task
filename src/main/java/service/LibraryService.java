package main.java.service;

import main.java.dao.AuthorDao;
import main.java.dao.BookDao;
import main.java.exception.*;
import main.java.model.Author;
import main.java.model.Book;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {
    private final AuthorDao authorDAO;
    private final BookDao bookDAO;

    public LibraryService(AuthorDao authorDAO, BookDao bookDAO) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
    }

    // Butun kitablari gostermek
    public List<Book> findAll() {
        try {
            return bookDAO.findAll();
        } catch (SQLException e) {
            throw new DBConnectionException("Data bazaya qosularken xeta bas verdi");
        }
    }

    // Verilmiş müəllif adına görə kitabları tap
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
            } else {
                throw new AuthorNotFoundException("Muellif tapilmadi: " + authorName);
            }

        } catch (SQLException e) {
            throw new DBConnectionException("Muellife gore kitab tapilarken xeta bas verdi");
        }
    }

    // Movcud kitablar
    public List<Book> findAvailableBooks() {
        try {
            return bookDAO.findAll().stream()
                    .filter(Book::isAvailable)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DBConnectionException("Movcud kitablari alarken xeta bas verdi");
        }
    }

    // Kitabı icareye goturmek
    public void borrowBook(int bookId) {
        try {
            Book book = bookDAO.findById(bookId)
                    .orElseThrow(() -> new BookNotFoundException("Kitab tapilmadi: ID = " + bookId));

            if (!book.isAvailable()) {
                throw new BookNotAvailableException("Kitab movcud deyil: " + book.getTitle());
            }

            book.setAvailable(false);
            bookDAO.update(book);

        } catch (SQLException e) {
            throw new DBConnectionException("Kitab icareye verilen zaman xeta bas verdi");
        }
    }

    // Kitabi geri qaytarmaq
    public void returnBook(int bookId) {
        try {
            Book book = bookDAO.findById(bookId)
                    .orElseThrow(() -> new BookNotFoundException("Kitab tapilmadi: ID = " + bookId));

            book.setAvailable(true);
            bookDAO.update(book);

        } catch (SQLException e) {
            throw new DBConnectionException("Kitab geri qaytarilarken xeta bas verdi");
        }
    }

    // Janr uzre kitab statistikasi
    public Map<String, Long> getBookStatisticsByGenre() {
        try {
            List<Book> allBooks = bookDAO.findAll();

            return allBooks.stream()
                    .collect(Collectors.groupingBy(
                            Book::getGenre,
                            Collectors.counting()
                    ));
        } catch (SQLException e) {
            throw new DBConnectionException("Kitab statistikasi alınarken xeta bas verdi");
        }
    }

    // En uzun kitab
    public Optional<Book> findLongestBook() {
        try {
            return bookDAO.findAll().stream()
                    .max(Comparator.comparing(Book::getPages));
        } catch (SQLException e) {
            throw new DBConnectionException("En uzun kitab axtarilarken xeta bas verdi");
        }
    }

    // Mueyyen ilden sonra cap olunmus kitablar
    public List<Book> findBooksPublishedAfter(int year) {
        try {
            return bookDAO.findAll().stream()
                    .filter(book -> book.getPublicationYear() != null && book.getPublicationYear() > year)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DBConnectionException("İline gore filtrlenmis kitablar alınarken xeta bas verdi");
        }
    }
}
