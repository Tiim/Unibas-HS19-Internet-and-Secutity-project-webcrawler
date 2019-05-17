package ch.unibas.ias.webcrawler.database;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class H2Queue implements UrlQueue {

    private PreparedStatement pushStmt;
    private PreparedStatement existsStmt;
    private PreparedStatement pullStmt;

    public H2Queue(DBConnection conn) {
        try {
            pushStmt = conn.newStatement("INSERT INTO queue VALUES (?)");
            existsStmt = conn.newStatement("SELECT url FROM queue WHERE url = ?");
            pullStmt = conn.newStatement("SELECT url FROM queue WHERE id = ?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void push(URL url) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int crawled() {
        return 0;
    }

    @Override
    public URL poll() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
