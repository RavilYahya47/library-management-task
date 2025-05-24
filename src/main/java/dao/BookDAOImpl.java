package main.java.dao;

import java.sql.Connection;

public class BookDAOImpl implements BookDAO {
    private static BookDAOImpl dao;
    private final Connection connection;

    public static BookDAOImpl of(Connection connection){
        if(dao == null){
            dao = new BookDAOImpl(connection);
        }

        return dao;
    }

    private BookDAOImpl(Connection connection) {
        this.connection = connection;
    }
}
