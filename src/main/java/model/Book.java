package java.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Book {
    private int id;
    private String title;
    private int authorId;
    private Integer publicationYear;
    private String genre;
    private Integer pages;
    private boolean isAvailable;
    private LocalDateTime createdAt;

    public Book(int id, String title, int authorId, Integer publicationYear, String genre, Integer pages, boolean isAvailable, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.pages = pages;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authorId=" + authorId +
                ", publicationYear=" + publicationYear +
                ", genre='" + genre + '\'' +
                ", pages=" + pages +
                ", isAvailable=" + isAvailable +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && authorId == book.authorId && isAvailable == book.isAvailable && Objects.equals(title, book.title) && Objects.equals(publicationYear, book.publicationYear) && Objects.equals(genre, book.genre) && Objects.equals(pages, book.pages) && Objects.equals(createdAt, book.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authorId, publicationYear, genre, pages, isAvailable, createdAt);
    }
}
