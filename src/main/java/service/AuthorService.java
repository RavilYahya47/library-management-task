package main.java.service;

import main.java.dao.AuthorDAO;
import main.java.model.Author;

import java.util.ArrayList;
import java.util.List;

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

    public List<Author> getAuthors() {
        return new ArrayList<Author>();
    }

    public List<Author> getAuthorsByName(String name) {
        return new ArrayList<Author>();
    }

    public List<Author> getAuthorsByNationality(String nationality) {
        return new ArrayList<Author>();
    }
}
