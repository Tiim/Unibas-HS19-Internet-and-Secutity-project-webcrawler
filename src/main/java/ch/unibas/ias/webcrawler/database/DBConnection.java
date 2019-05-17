package ch.unibas.ias.webcrawler.database;

import java.sql.*;

public class DBConnection {

    private Connection connection;

    public DBConnection() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:test", "sa", "");
        init();
    }

    private void init() throws SQLException {
        String tablePage = "CREATE TABLE IF NOT EXISTS page (" +
                "url VARCHAR(500),"+
                "html CLOB,"+
                "header VARCHAR(2048),"+
                "date TIMESTAMP)";

        String tableQueue = "CREATE TABLE IF NOT EXISTS queue (" +
                "id IDENTITY"+
                "url VARCHAR(500))";

        Statement stmt = connection.createStatement();
        stmt.execute(tablePage);
        Statement stmt2 = connection.createStatement();
        stmt2.execute(tableQueue);
    }

    public PreparedStatement newStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
