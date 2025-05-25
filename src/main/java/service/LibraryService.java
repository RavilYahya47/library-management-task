package main.java.service;

import main.java.dao.AuthorDao;
import main.java.dao.BookDao;
import main.java.dao.impl.AuthorDaoImpl;
import main.java.dao.impl.BookDaoImpl;
import main.java.model.Author;
import main.java.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LibraryService {
    private final AuthorDao authorDao;
    private final BookDao bookDao;

    public LibraryService() {
        this.authorDao = new AuthorDaoImpl();
        this.bookDao = new BookDaoImpl();
    }

    public LibraryService(AuthorDao authorDao, BookDao bookDao) {
        this.authorDao = authorDao;
        this.bookDao = bookDao;
    }

    public List<Book> findBooksByAuthor(String authorName) throws SQLException {
        if(authorName == null || authorName.trim().isEmpty()){
            throw new IllegalArgumentException("Author name cannot be null");
        }

        return bookDao.findBookByAuthor(authorName.trim());
    }

    public List<Book> findAvailableBooks() throws SQLException {
        return bookDao.findAvailableBooks(0); // 0-limitsiz demekdir
    }

    public List<Book> findAvailableBooks(int limit) throws SQLException {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit cannot be negative");
        }

        return bookDao.findAvailableBooks(limit);
    }

    public void borrowBook(int bookId) throws SQLException {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }

        bookDao.borrowBook(bookId);
        System.out.println("Kitab uğurla icarəyə verildi!");
    }

    public void returnBook(int bookId) throws SQLException {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be positive");
        }

        bookDao.returnBook(bookId);
        System.out.println("Kitab uğurla qaytarıldı!");
    }

    public Map<String, Long> getBookStatisticsByGenre() throws SQLException {
        return bookDao.getBookStatisticsByGenre();
    }

    public void printBookStatistics() throws SQLException {
        Map<String, Long> statistics = getBookStatisticsByGenre();

        System.out.println("\n=== Janr üzrə Kitab Statistikası ===");
        if (statistics.isEmpty()) {
            System.out.println("Heç bir kitab tapılmadı.");
            return;
        }

        statistics.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry ->
                        System.out.printf("%-20s: %d kitab%n", entry.getKey(), entry.getValue())
                );
    }

    public List<Author> getAllAuthors() throws SQLException {
        return authorDao.findAll();
    }

    public Optional<Author> getAuthorById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Author ID must be positive");
        }

        return authorDao.findById(id);
    }

    public Author saveAuthor(Author author) throws SQLException {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        return authorDao.save(author);
    }

    public void updateAuthor(Author author) throws SQLException {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        if (author.getId() <= 0) {
            throw new IllegalArgumentException("Author ID must be positive");
        }
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        authorDao.update(author);
    }

    public void deleteAuthor(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Author ID must be positive");
        }

        authorDao.deleteById(id);
    }

    public void printAllAuthors() throws SQLException {
        List<Author> authors = getAllAuthors();

        System.out.println("\n=== Bütün Müəlliflər ===");
        if (authors.isEmpty()) {
            System.out.println("Heç bir müəllif tapılmadı.");
            return;
        }

        authors.forEach(author ->
                System.out.printf("ID: %d | Ad: %s | Doğum ili: %s | Milliyyət: %s%n",
                        author.getId(),
                        author.getName(),
                        author.getBirthYear() != null ? author.getBirthYear().toString() : "Naməlum",
                        author.getNationality() != null ? author.getNationality() : "Naməlum"
                )
        );
    }

    public void printBooks(List<Book> books, String title) {
        System.out.println("\n=== " + title + " ===");
        if (books.isEmpty()) {
            System.out.println("Heç bir kitab tapılmadı.");
            return;
        }

        books.forEach(book ->
                System.out.printf("ID: %d | Başlıq: %s | Müəllif ID: %d | İl: %s | Janr: %s | Status: %s%n",
                        book.getId(),
                        book.getTitle(),
                        book.getAuthorId(),
                        book.getPublicationYear() != null ? book.getPublicationYear().toString() : "Naməlum",
                        book.getGenre() != null ? book.getGenre() : "Naməlum",
                        book.isAvailable() ? "Mövcud" : "İcarədə"
                )
        );
    }

    public List<Book> findBooksPublishedAfter(int year) throws SQLException {
        return findAvailableBooks().stream()
                .filter(book -> book.getPublicationYear() != null && book.getPublicationYear() > year)
                .collect(Collectors.toList());
    }

    public Book saveBook(Book book) throws SQLException {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        if (book.getAuthorId() <= 0) {
            throw new IllegalArgumentException("Author ID must be positive");
        }

        return bookDao.save(book);
    }

    public Optional<Book> findBookWithMostPages() throws SQLException {
        return findAvailableBooks().stream()
                .filter(book -> book.getPages() != null)
                .max((book1, book2) -> Integer.compare(book1.getPages(), book2.getPages()));
    }

    public Map<String, List<Book>> groupBooksByGenre() throws SQLException {
        return findAvailableBooks().stream()
                .filter(book -> book.getGenre() != null)
                .collect(Collectors.groupingBy(Book::getGenre));
    }

}
