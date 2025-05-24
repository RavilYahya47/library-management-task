package main.java.service;

import main.java.dao.AuthorDAO;
import main.java.model.Author;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class AuthorService {
    private static AuthorService service;
    private AuthorDAO dao;

    public static AuthorService of(AuthorDAO dao) {
        if (service == null) {
            service = new AuthorService(dao);
        }

        return service;
    }

    private AuthorService(AuthorDAO dao) {
        this.dao = dao;
    }

    public void filterBy(Predicate<Author> filter) throws SQLException {
        List<Author> authors = dao.findAll().stream().filter(filter).toList();

        if (!authors.isEmpty()) {
            authors.forEach(System.out::println);
        }
        else {
            System.out.println("No authors found");
        }
    };

    public void getAuthors() {
    }
}
